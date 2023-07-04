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

}