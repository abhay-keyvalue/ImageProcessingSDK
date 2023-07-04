package com.imageprocessingsdk.imagetopdf

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import java.util.Locale

class CreatePdfOptions(options: ReadableMap) {
    var outputDirectory: String?
    var outputFilename: String?
    var images: Array<Page?>

    init {
        outputDirectory = options?.getString("outputDirectory") ?: "/storage/emulated/0/Download"
        outputFilename = options?.getString("outputFilename") ?: "output_pdf"

        images = parsePages(getArrayOrThrow(options, "images"))
    }

    private fun getArrayOrThrow(options: ReadableMap, key: String): ReadableArray? {
        require(options.hasKey(key)) { "Required option '$key' not found." }
        return options.getArray(key)
    }

    private fun getInt(options: ReadableMap, key: String): Int? {
        return if (options.hasKey(key)) {
            options.getInt(key)
        } else null
    }

    private fun parsePages(pagesArray: ReadableArray?): Array<Page?> {
        requireNotNull(pagesArray) { "Invalid 'pages' argument. 'pages' cannot be null." }
        val parsedPages = arrayOfNulls<Page>(pagesArray.size())
        for (i in 0 until pagesArray.size()) {
            val pageMap: ReadableMap = pagesArray.getMap(i)
            val imagePath: String? = pageMap.getString("imagePath")
            val width = getInt(pageMap, "width")
            val height = getInt(pageMap, "height")
            val backgroundColor = getInt(pageMap, "backgroundColor")
            val imageFit = parseImageFit(pageMap.getString("imageFit"))
            imagePath?.let {
                parsedPages[i] = Page(it, imageFit, width, height, backgroundColor)
            }
        }
        return parsedPages
    }

    private fun parseImageFit(imageFitValue: String?): ImageFit {
        return try {
            if (imageFitValue == null) ImageFit.NONE else ImageFit.valueOf(
                imageFitValue.uppercase(
                    Locale.getDefault()
                )
            )
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid 'imageFit' value: $imageFitValue")
        }
    }

    class Page(
        var imagePath: String,
        var imageFit: ImageFit,
        var width: Int?,
        var height: Int?,
        var backgroundColor: Int?
    )
}
