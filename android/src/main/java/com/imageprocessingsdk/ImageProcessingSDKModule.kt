package com.imageprocessingsdk

import android.content.ContentResolver
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Promise
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.imageprocessingsdk.imagetopdf.ImageScaling
import com.imageprocessingsdk.imagetopdf.CreatePdfOptions
import com.imageprocessingsdk.imagetopdf.ImageFit
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import com.imageprocessingsdk.opencv.android.Utils
import com.imageprocessingsdk.opencv.core.Core
import com.imageprocessingsdk.opencv.core.Mat
import com.imageprocessingsdk.opencv.core.MatOfDouble
import com.imageprocessingsdk.opencv.imgproc.Imgproc
import java.text.DecimalFormat

class ImageProcessingSDKModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {
      private lateinit var sourceMatImage: Mat

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun isImageBlurred(imageUrl: String, promise: Promise) {
      OpenCVInitializer.initialize()                    
      sourceMatImage = OpenCVInitializer.createMat()!!
      val imageBitMap = ImageUtils.getBitmap(imageUrl, true);
      getSharpnessScoreFromOpenCV(imageBitMap);
    }

    fun getSharpnessScoreFromOpenCV(bitmap: Bitmap): Double {
        val destination = Mat()
        val matGray = Mat()
        Utils.bitmapToMat(bitmap, sourceMatImage)
        Imgproc.cvtColor(sourceMatImage, matGray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.Laplacian(matGray, destination, 3)
        val median = MatOfDouble()
        val std = MatOfDouble()
        Core.meanStdDev(destination, median, std)
        return DecimalFormat("0.00").format(Math.pow(std.get(0, 0)[0], 2.0)).toDouble()
    }


    // PDF
    @ReactMethod
    fun createPdf(optionsMap: ReadableMap, promise: Promise) {
        try {
            val options = CreatePdfOptions(optionsMap)
            val outputDirectory = options.outputDirectory
            val outputFilename = options.outputFilename
            val pages = options.images
            if (pages.isEmpty()) {
                throw Exception("No images provided.")
            }
            val pdfDocument = PdfDocument()
            try {
                for (i in pages.indices) {
                    val config = pages[i]
                    val imagePath = config!!.imagePath
                    val image = getBitmapFromPathOrUri(imagePath)
                        ?: throw Exception("$imagePath cannot be decoded into a bitmap.")
                    val pageWidth = config.width
                    val width = pageWidth ?: image.width
                    val pageHeight = config.height
                    val height = pageHeight ?: image.height
                    val pageInfo: PdfDocument.PageInfo =
                        PdfDocument.PageInfo.Builder(width, height, i + 1)
                            .create()
                    val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
                    var scaledImage = image
                    if (width != image.width || height != image.height) {
                        val imageFit = config.imageFit ?: ImageFit.FILL
                        val size = Point(width, height)
                        scaledImage = ImageScaling.scale(image, size, imageFit)
                    }
                    val canvas: Canvas = page.getCanvas()
                    if (config.backgroundColor != null) {
                        canvas.drawColor(config.backgroundColor!!)
                    }
                    canvas.drawBitmap(scaledImage, 0f, 0f, null)
                    pdfDocument.finishPage(page)
                }
            } catch (e: Exception) {
                Log.e("ImagesPdfModule", e.localizedMessage, e)
                promise.reject("PDF_PAGE_CREATE_ERROR", e.localizedMessage, e)
                pdfDocument.close()
                return
            }
            var outputUri: Uri? = null
            outputUri = try {
                writePdfDocument(pdfDocument, outputDirectory, outputFilename)
            } catch (e: Exception) {
                Log.e("ImagesPdfModule", e.localizedMessage, e)
                promise.reject("PDF_WRITE_ERROR", e.localizedMessage, e)
                pdfDocument.close()
                return
            }
            pdfDocument.close()
            promise.resolve(outputUri!!.path)
        } catch (e: Exception) {
            Log.e("ImagesPdfModule", e.localizedMessage, e)
            promise.reject("PDF_CREATE_ERROR", e.localizedMessage, e)
        }
    }

    @Throws(IOException::class)
    fun writePdfDocument(
        pdfDocument: PdfDocument,
        outputDirectory: String?,
        outputFilename: String?
    ): Uri? {
        var outputStream: OutputStream? = null
        var outputUri: Uri? = null
        try {
            val uri = Uri.parse(outputDirectory)
            val mimeTypePdf = "application/pdf"
            val scheme = uri.scheme
            if (scheme != null && scheme == ContentResolver.SCHEME_CONTENT) {
                // TODO: Fix warnings: Method invocation may produce 'NullPointerException'.
                val dirFile: DocumentFile? = DocumentFile
                    .fromTreeUri(getReactApplicationContext().getApplicationContext(), uri)
                val pdfFile: DocumentFile? = outputFilename?.let {
                    dirFile?.createFile(mimeTypePdf, it)
                }
                outputUri = pdfFile?.getUri()
                if (outputUri != null) {
                    outputStream =
                        getReactApplicationContext()
                            .getContentResolver()
                            .openOutputStream(outputUri)
                } else {
                    throw IOException("Could not create PDF: $outputFilename")
                }
            } else if (scheme == null || scheme == ContentResolver.SCHEME_FILE) {
                outputUri = uri.buildUpon()
                    .appendPath(outputFilename)
                    .build()
                outputStream = FileOutputStream(outputUri.path)
            } else {
                throw UnsupportedOperationException("Unsupported scheme: " + uri.scheme)
            }
            pdfDocument.writeTo(outputStream)
        } finally {
            outputStream?.close()
        }
        return outputUri
    }

    @Throws(IOException::class)
    fun getBitmapFromPathOrUri(pathOrUri: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        try {
            val uri = Uri.parse(pathOrUri)
            val scheme = uri.scheme
            inputStream = if (scheme != null && scheme == ContentResolver.SCHEME_CONTENT) {
                val contentResolver: ContentResolver = getReactApplicationContext()
                    .getContentResolver()
                contentResolver
                    .openInputStream(uri)
            } else if (scheme == null || scheme == ContentResolver.SCHEME_FILE) {
                FileInputStream(uri.path)
            } else {
                throw UnsupportedOperationException("Unsupported scheme: " + uri.scheme)
            }
            if (inputStream != null) {
                bitmap = BitmapFactory
                    .decodeStream(inputStream)
            }
        } finally {
            inputStream?.close()
        }
        return bitmap
    }


    companion object {
        val NAME = "ImageProcessingSDK"
    }
}
