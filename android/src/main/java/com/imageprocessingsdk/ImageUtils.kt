import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface

object ImageUtils {

    fun getBitmap(filePath: String, isMutable: Boolean): Bitmap {
        val exifInterface = ExifInterface(filePath)
        val orientation = exifInterface.getRotationDegrees()

        val bmpOptions = BitmapFactory.Options()
        bmpOptions.inJustDecodeBounds = true

        BitmapFactory.decodeFile(filePath, bmpOptions)

        bmpOptions.inSampleSize = calculateInSampleSize(bmpOptions)
        bmpOptions.inJustDecodeBounds = false
        if(isMutable) {
            bmpOptions.inMutable = true
        }

        val bmp = BitmapFactory.decodeFile(filePath, bmpOptions)

        return fixExifRotation(orientation, bmp)
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int = 2000,
            reqHeight: Int = 2000
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun fixExifRotation(orientation: Int, source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(orientation.toFloat())
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }

    private fun ExifInterface.getRotationDegrees(): Int {
        val orientation: Int =
                getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_TRANSVERSE -> 90
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180
            ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSPOSE -> 270
            ExifInterface.ORIENTATION_UNDEFINED, ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> 0
            else -> 0
        }
    }

}