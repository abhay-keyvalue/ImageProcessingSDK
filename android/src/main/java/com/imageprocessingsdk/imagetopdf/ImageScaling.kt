package com.imageprocessingsdk.imagetopdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point

// TODO: ImagePosition
object ImageScaling {
    @Throws(Exception::class)
    fun scale(image: Bitmap, size: Point, fit: ImageFit): Bitmap {
        return when (fit) {
            ImageFit.NONE -> scaleWithNone(image, size)
            ImageFit.CONTAIN -> scaleWithContain(image, size)
            ImageFit.COVER -> scaleWithCover(image, size)
            ImageFit.FILL -> scaleWithFill(image, size)
            else -> throw Exception("Unknown scale fit: $fit")
        }
    }

    private fun scaleWithNone(bitmap: Bitmap, size: Point): Bitmap {
        // Create background bitmap.
        val newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)

        // Calculate new width and height.
        val scaledWidth = bitmap.width
        val scaledHeight = bitmap.height

        // Apply transformations.
        val matrix = Matrix()
        val translateX = (size.x - scaledWidth) / 2f
        val translateY = (size.y - scaledHeight) / 2f
        matrix.postTranslate(translateX, translateY)

        // Draw the bitmap.
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return newBitmap
    }

    private fun scaleWithContain(bitmap: Bitmap, size: Point): Bitmap {
        // Create background bitmap.
        val newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)

        // Calculate new width and height.
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height
        val targetAspectRatio = size.x.toFloat() / size.y
        val scaledWidth: Int
        val scaledHeight: Int
        if (aspectRatio > targetAspectRatio) {
            scaledWidth = size.x
            scaledHeight = (size.x / aspectRatio).toInt()
        } else {
            scaledWidth = (size.y * aspectRatio).toInt()
            scaledHeight = size.y
        }

        // Apply transformations.
        val matrix = Matrix()
        val scaleX = scaledWidth.toFloat() / width
        val scaleY = scaledHeight.toFloat() / height
        matrix.postScale(scaleX, scaleY)
        val translateX = (size.x - scaledWidth) / 2f
        val translateY = (size.y - scaledHeight) / 2f
        matrix.postTranslate(translateX, translateY)

        // Draw the bitmap.
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return newBitmap
    }

    private fun scaleWithCover(bitmap: Bitmap, size: Point): Bitmap {
        // Create background bitmap.
        val newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)

        // Calculate new width and height.
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height
        val targetAspectRatio = size.x.toFloat() / size.y
        val scaleFactor: Float = if (aspectRatio > targetAspectRatio) {
            size.y.toFloat() / height
        } else {
            size.x.toFloat() / width
        }
        val scaledWidth = Math.round(width * scaleFactor)
        val scaledHeight = Math.round(height * scaleFactor)

        // Apply transformations.
        val matrix = Matrix()
        matrix.postScale(scaleFactor, scaleFactor)
        val translateX = (size.x - scaledWidth) / 2f
        val translateY = (size.y - scaledHeight) / 2f
        matrix.postTranslate(translateX, translateY)

        // Draw the bitmap.
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return newBitmap
    }

    private fun scaleWithFill(bitmap: Bitmap, size: Point): Bitmap {
        // Create background bitmap.
        val newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)

        // Calculate new width and height.
        val width = bitmap.width
        val height = bitmap.height
        val scaledWidth = size.x
        val scaledHeight = size.y

        // Apply transformations.
        val matrix = Matrix()
        val scaleX = scaledWidth.toFloat() / width
        val scaleY = scaledHeight.toFloat() / height
        matrix.postScale(scaleX, scaleY)

        // Draw the bitmap.
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return newBitmap
    }
}
