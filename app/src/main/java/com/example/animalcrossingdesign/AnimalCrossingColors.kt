package com.example.animalcrossingdesign

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class AnimalCrossingQRObject {
    // Todo: Should these be nullable?
    var title: String
    var author: String
    var town: String
    var colorPalettePositions: ByteArray
    var imagePixels: IntArray
    var imagePositionByteData: ByteArray

    companion object {
        // TODO: Put animalCrossingPalettePositionToColorMap and animalCrossingPaletteColorToPositionMap in here

        val animalCrossingPalettePositionToColorMap = mutableMapOf<Byte, Int>(
                0x00.toByte() to 0xffffefff.toInt(),
                0x01.toByte() to 0xffff9aad.toInt(),
                0x02.toByte() to 0xffef559c.toInt(),
                0x03.toByte() to 0xffff65ad.toInt(),
                0x04.toByte() to 0xffff0063.toInt(),
                0x05.toByte() to 0xffbd4573.toInt(),
                0x06.toByte() to 0xffce0052.toInt(),
                0x07.toByte() to 0xff9c0031.toInt(),
                0x08.toByte() to 0xff522031.toInt(),

                0x10.toByte() to 0xffffbace.toInt(),
                0x11.toByte() to 0xffff7573.toInt(),
                0x12.toByte() to 0xffde3010.toInt(),
                0x13.toByte() to 0xffff5542.toInt(),
                0x14.toByte() to 0xffff0000.toInt(),
                0x15.toByte() to 0xffce6563.toInt(),
                0x16.toByte() to 0xffbd4542.toInt(),
                0x17.toByte() to 0xffbd0000.toInt(),
                0x18.toByte() to 0xff8c2021.toInt(),

                0x20.toByte() to 0xffdecfbd.toInt(),
                0x21.toByte() to 0xffffcf63.toInt(),
                0x22.toByte() to 0xffde6521.toInt(),
                0x23.toByte() to 0xffffaa21.toInt(),
                0x24.toByte() to 0xffff6500.toInt(),
                0x25.toByte() to 0xffbd8a52.toInt(),
                0x26.toByte() to 0xffde4500.toInt(),
                0x27.toByte() to 0xffbd4500.toInt(),
                0x28.toByte() to 0xff633010.toInt(),

                0x30.toByte() to 0xffffefde.toInt(),
                0x31.toByte() to 0xffffdfce.toInt(),
                0x32.toByte() to 0xffffcfad.toInt(),
                0x33.toByte() to 0xffffba8c.toInt(),
                0x34.toByte() to 0xffffaa8c.toInt(),
                0x35.toByte() to 0xffde8a63.toInt(),
                0x36.toByte() to 0xffbd6542.toInt(),
                0x37.toByte() to 0xff9c5531.toInt(),
                0x38.toByte() to 0xff8c4521.toInt(),

                0x40.toByte() to 0xffffcfff.toInt(),
                0x41.toByte() to 0xffef8aff.toInt(),
                0x42.toByte() to 0xffce65de.toInt(),
                0x43.toByte() to 0xffbd8ace.toInt(),
                0x44.toByte() to 0xffce00ff.toInt(),
                0x45.toByte() to 0xff9c659c.toInt(),
                0x46.toByte() to 0xff8c00ad.toInt(),
                0x47.toByte() to 0xff520073.toInt(),
                0x48.toByte() to 0xff310042.toInt(),

                0x50.toByte() to 0xffffbaff.toInt(),
                0x51.toByte() to 0xffff9aff.toInt(),
                0x52.toByte() to 0xffde20bd.toInt(),
                0x53.toByte() to 0xffff55ef.toInt(),
                0x54.toByte() to 0xffff00ce.toInt(),
                0x55.toByte() to 0xff8c5573.toInt(),
                0x56.toByte() to 0xffbd009c.toInt(),
                0x57.toByte() to 0xff8c0063.toInt(),
                0x58.toByte() to 0xff520042.toInt(),

                0x60.toByte() to 0xffdeba9c.toInt(),
                0x61.toByte() to 0xffceaa73.toInt(),
                0x62.toByte() to 0xff734531.toInt(),
                0x63.toByte() to 0xffad7542.toInt(),
                0x64.toByte() to 0xff9c3000.toInt(),
                0x65.toByte() to 0xff733021.toInt(),
                0x66.toByte() to 0xff522000.toInt(),
                0x67.toByte() to 0xff311000.toInt(),
                0x68.toByte() to 0xff211000.toInt(),

                0x70.toByte() to 0xffffffce.toInt(),
                0x71.toByte() to 0xffffff73.toInt(),
                0x72.toByte() to 0xffdedf21.toInt(),
                0x73.toByte() to 0xffffff00.toInt(),
                0x74.toByte() to 0xffffdf00.toInt(),
                0x75.toByte() to 0xffceaa00.toInt(),
                0x76.toByte() to 0xff9c9a00.toInt(),
                0x77.toByte() to 0xff8c7500.toInt(),
                0x78.toByte() to 0xff525500.toInt(),

                0x80.toByte() to 0xffdebaff.toInt(),
                0x81.toByte() to 0xffbd9aef.toInt(),
                0x82.toByte() to 0xff6330ce.toInt(),
                0x83.toByte() to 0xff9c55ff.toInt(),
                0x84.toByte() to 0xff6300ff.toInt(),
                0x85.toByte() to 0xff52458c.toInt(),
                0x86.toByte() to 0xff42009c.toInt(),
                0x87.toByte() to 0xff210063.toInt(),
                0x88.toByte() to 0xff211031.toInt(),

                0x90.toByte() to 0xffbdbaff.toInt(),
                0x91.toByte() to 0xff8c9aff.toInt(),
                0x92.toByte() to 0xff3130ad.toInt(),
                0x93.toByte() to 0xff3155ef.toInt(),
                0x94.toByte() to 0xff0000ff.toInt(),
                0x95.toByte() to 0xff31308c.toInt(),
                0x96.toByte() to 0xff0000ad.toInt(),
                0x97.toByte() to 0xff101063.toInt(),
                0x98.toByte() to 0xff000021.toInt(),

                0xa0.toByte() to 0xff9cefbd.toInt(),
                0xa1.toByte() to 0xff63cf73.toInt(),
                0xa2.toByte() to 0xff216510.toInt(),
                0xa3.toByte() to 0xff42aa31.toInt(),
                0xa4.toByte() to 0xff008a31.toInt(),
                0xa5.toByte() to 0xff527552.toInt(),
                0xa6.toByte() to 0xff215500.toInt(),
                0xa7.toByte() to 0xff103021.toInt(),
                0xa8.toByte() to 0xff002010.toInt(),

                0xb0.toByte() to 0xffdeffbd.toInt(),
                0xb1.toByte() to 0xffceff8c.toInt(),
                0xb2.toByte() to 0xff8caa52.toInt(),
                0xb3.toByte() to 0xffaddf8c.toInt(),
                0xb4.toByte() to 0xff8cff00.toInt(),
                0xb5.toByte() to 0xffadba9c.toInt(),
                0xb6.toByte() to 0xff63ba00.toInt(),
                0xb7.toByte() to 0xff529a00.toInt(),
                0xb8.toByte() to 0xff316500.toInt(),

                0xc0.toByte() to 0xffbddfff.toInt(),
                0xc1.toByte() to 0xff73cfff.toInt(),
                0xc2.toByte() to 0xff31559c.toInt(),
                0xc3.toByte() to 0xff639aff.toInt(),
                0xc4.toByte() to 0xff1075ff.toInt(),
                0xc5.toByte() to 0xff4275ad.toInt(),
                0xc6.toByte() to 0xff214573.toInt(),
                0xc7.toByte() to 0xff002073.toInt(),
                0xc8.toByte() to 0xff001042.toInt(),

                0xd0.toByte() to 0xffadffff.toInt(),
                0xd1.toByte() to 0xff52ffff.toInt(),
                0xd2.toByte() to 0xff008abd.toInt(),
                0xd3.toByte() to 0xff52bace.toInt(),
                0xd4.toByte() to 0xff00cfff.toInt(),
                0xd5.toByte() to 0xff429aad.toInt(),
                0xd6.toByte() to 0xff00658c.toInt(),
                0xd7.toByte() to 0xff004552.toInt(),
                0xd8.toByte() to 0xff002031.toInt(),

                0xe0.toByte() to 0xffceffef.toInt(),
                0xe1.toByte() to 0xffadefde.toInt(),
                0xe2.toByte() to 0xff31cfad.toInt(),
                0xe3.toByte() to 0xff52efbd.toInt(),
                0xe4.toByte() to 0xff00ffce.toInt(),
                0xe5.toByte() to 0xff73aaad.toInt(),
                0xe6.toByte() to 0xff00aa9c.toInt(),
                0xe7.toByte() to 0xff008a73.toInt(),
                0xe8.toByte() to 0xff004531.toInt(),

                0xf0.toByte() to 0xffadffad.toInt(),
                0xf1.toByte() to 0xff73ff73.toInt(),
                0xf2.toByte() to 0xff63df42.toInt(),
                0xf3.toByte() to 0xff00ff00.toInt(),
                0xf4.toByte() to 0xff21df21.toInt(),
                0xf5.toByte() to 0xff52ba52.toInt(),
                0xf6.toByte() to 0xff00ba00.toInt(),
                0xf7.toByte() to 0xff008a00.toInt(),
                0xf8.toByte() to 0xff214521.toInt(),

                0x0f.toByte() to 0xffffffff.toInt(),
                0x1f.toByte() to 0xffefefef.toInt(),
                0x2f.toByte() to 0xffdedfde.toInt(),
                0x3f.toByte() to 0xffcecfce.toInt(),
                0x4f.toByte() to 0xffbdbabd.toInt(),
                0x5f.toByte() to 0xffadaaad.toInt(),
                0x6f.toByte() to 0xff9c9a9c.toInt(),
                0x7f.toByte() to 0xff8c8a8c.toInt(),
                0x8f.toByte() to 0xff737573.toInt(),
                0x9f.toByte() to 0xff636563.toInt(),
                0xaf.toByte() to 0xff525552.toInt(),
                0xbf.toByte() to 0xff424542.toInt(),
                0xcf.toByte() to 0xff313031.toInt(),
                0xdf.toByte() to 0xff212021.toInt(),
                0xef.toByte() to 0xff000000.toInt()
        )
        val animalCrossingPaletteColorToPositionMap = animalCrossingPalettePositionToColorMap.entries.associate { it.value to it.key }

        private fun pixelsToQRByteArray(intArrayOfImageColors: IntArray,
                                        colorPalettePositions: ByteArray,
                                        title: String = "title",
                                        author: String = "author",
                                        town: String = "Jolly Isle"): ByteArray {
            // Unique ID
            val _1: Byte = 0xB6.toByte()//0
            val _2: Byte = 0xEC.toByte()//0
            val _3: Byte = 0x44.toByte()//0
            val _4: Byte = 0xC5.toByte()//0
            val _5: Byte = 0x19.toByte()//0
            val _6: Byte = 0x31.toByte()//0
            val _12: Byte = 0
            val _13: Byte = 0
            val _14: Byte = 0
            val _15: Byte = 0

            val _7: Byte = 0xCC.toByte()//0 //todo: NB!!!
            //
            val _8: Byte = 0x0A.toByte()
            val _9: Byte = 9// Panel Type // Todo: Make companion object for this
            val _10: Byte = 0 // Fixed
            val _11: Byte = 0 // Fixed


            var qrByteArray = title.toByteArray()

            while (qrByteArray.size < 42) {
                qrByteArray += 0
            }

            qrByteArray += byteArrayOf(_1, _2)

            qrByteArray += author.toByteArray()
            while (qrByteArray.size < 62) {
                qrByteArray += 0
            }

            qrByteArray += byteArrayOf(_12, _13, _3, _4)

            qrByteArray += town.toByteArray()
            while (qrByteArray.size < 84) {
                qrByteArray += 0
            }

            qrByteArray += byteArrayOf(_14, _15, _5, _6)

            if (BuildConfig.DEBUG && colorPalettePositions.size != 15) {
                error("Assertion failed")
            }

            qrByteArray += colorPalettePositions
            while (qrByteArray.size < 103) {
                qrByteArray += 0
            }

            qrByteArray += byteArrayOf(_7, _8, _9, _10, _11)

            // Pixel color to position byte data
            qrByteArray += pixelsToPositionByteData(intArrayOfImageColors, colorPalettePositions)
            return qrByteArray
        }

        private fun pixelsToPositionByteData(intArrayOfImageColors: IntArray, colorPalettePositions: ByteArray): ByteArray {
            var qrByteArray = ByteArray(0)  // Todo: remove hardcoding
            for (pixelPair in intArrayOfImageColors.toList().chunked(2)) {
                val firstHalfOfByte = colorPalettePositions.indexOf(animalCrossingPaletteColorToPositionMap[pixelPair[0]]!!) shl 4
                val secondHalfOfByte = colorPalettePositions.indexOf(animalCrossingPaletteColorToPositionMap[pixelPair[1]]!!)
                val combinedByte = firstHalfOfByte or secondHalfOfByte
                qrByteArray += combinedByte.toByte()
            }
            return qrByteArray
        }

        fun byteArrayToQRCode(qrByteArray: ByteArray): Bitmap {
            val codeWriter = MultiFormatWriter()
            val width = 897//500
            val height = 897//500
            val qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            try {

                val bitMatrix = codeWriter.encode(qrByteArray.toString(Charsets.ISO_8859_1),
                        //val bitMatrix = codeWriter.encode(getEncoder().encodeToString(qrByteArrary),
                        BarcodeFormat.QR_CODE, width, height )
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        qrBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            } catch (e: WriterException) {
                Log.d(TAG, "generateQRCode: ${e.message}")
            }
            return qrBitmap
        }

        private fun generateQRCodeFromBitmap(bitmap: Bitmap, colorPalettePositions: ByteArray): Bitmap {
            /*
            Generate QR code bitmap from bitmap (NB! given that it is in the AC Color palette!)
             */
            val intArrayOfImageColors = IntArray(bitmap.width*bitmap.height)
            bitmap.getPixels(intArrayOfImageColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val qrByteArray = pixelsToQRByteArray(intArrayOfImageColors, colorPalettePositions)
            val qrBitmap = byteArrayToQRCode(qrByteArray)

            return qrBitmap
        }
    }

    constructor(rawQRBytes: ByteArray) {
        val title: ByteArray = rawQRBytes.sliceArray(0..41)
        val author: ByteArray = rawQRBytes.sliceArray(44..61)
        val town: ByteArray = rawQRBytes.sliceArray(66..83)
        val colorPalettePositions: ByteArray = rawQRBytes.sliceArray(88..102)
        val imageData: ByteArray = rawQRBytes.sliceArray(108 until rawQRBytes.size)

        this.title = title.joinToString(separator = "", transform = { it.toChar().toString() })
        this.author = author.joinToString(separator = "", transform = { it.toChar().toString() })
        this.town = town.joinToString(separator = "", transform = { it.toChar().toString() })
        this.colorPalettePositions = colorPalettePositions
        this.imagePositionByteData = imageData
        //this.imagePixels = getPixelColorsFromBytes(imageData).map { animalCrossingPalettePositionToColorMap[this.colorPalettePositions[it]]!! }.toIntArray()
        this.imagePixels = getPixelColorsFromBytes(imageData).toIntArray()
    }

    constructor(bitmap: Bitmap,
                title: String = "title",
                author: String = "author",
                town: String = "Jolly Isle") {
        this.title = title
        this.author = author
        this.town = town
        val tempIntArray = IntArray(bitmap.width*bitmap.height)
        bitmap.getPixels(tempIntArray,0, bitmap.width,0,0, bitmap.width, bitmap.height);
        this.imagePixels = tempIntArray
        this.colorPalettePositions = this.imagePixels.distinct().map { animalCrossingPaletteColorToPositionMap[it]!! }.toByteArray()
        this.imagePositionByteData = pixelsToPositionByteData(this.imagePixels, this.colorPalettePositions)
    }

    fun toQRBitmap(): Bitmap {
        return byteArrayToQRCode(toQRRawBytes())
    }

    fun toQRRawBytes(): ByteArray {
        /*
        Todo: use this in the constructors and make this a class member
         */
        return pixelsToQRByteArray(imagePixels, colorPalettePositions, title, author, town)
    }



    private fun getPixelColorsFromBytes(bytes: ByteArray): ArrayList<Int> {
        val arrayListOfPixels = ArrayList<Int>()

        for (byte in bytes) {
            val firstHalf = (byte.toInt() shr 4) and 0x0f
            val secondHalf = byte.toInt() and 0x0f
            arrayListOfPixels.add(animalCrossingPalettePositionToColorMap[this.colorPalettePositions[firstHalf]]!!)
            arrayListOfPixels.add(animalCrossingPalettePositionToColorMap[this.colorPalettePositions[secondHalf]]!!)
        }
        return arrayListOfPixels
    }



}
