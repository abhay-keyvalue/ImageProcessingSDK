package com.imageprocessingsdk

import android.content.ContentResolver
import com.example.ndksample.NDKHandler
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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ImageProcessingSDKModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return NAME
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun isImageBlurred(imageUrl: String, promise: Promise) {
        val response = NDKHandler().getBlurredImage(imageUrl)
        promise.resolve(response)
    }


    // PDF
    @ReactMethod
    fun createPdf(optionsMap: ReadableMap, promise: Promise) {
        try {
            val options = CreatePdfOptions(optionsMap)
            val outputDirectory = options.outputDirectory
            val outputFilename = options.outputFilename
            val pages = options.images
            if (pages.size == 0) {
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
                        val imageFit = config.imageFit
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

//    @ReactMethod
//    fun getDocumentsDirectory(promise: Promise) {
//        val docsDir: String =
//            getReactApplicationContext().getExternalFilesDir(null)?.getAbsolutePath()
//        promise.resolve(docsDir)
//    }

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
