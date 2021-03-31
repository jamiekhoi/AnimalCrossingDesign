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
    var colorPalettePositions: List<Byte>
    var imagePixels: IntArray
    var imagePositionByteData: ByteArray

    companion object {
        // TODO: Put animalCrossingPalettePositionToColorMap and animalCrossingPaletteColorToPositionMap in here
    }
    private val animalCrossingPalettePositionToColorMap = mutableMapOf<Byte, Int>()
    private val animalCrossingPaletteColorToPositionMap = mutableMapOf<Int, Byte>()
    init {
        animalCrossingPalettePositionToColorMap[0x00.toByte()] = 0xffffefff.toInt()
        animalCrossingPalettePositionToColorMap[0x01.toByte()] = 0xffff9aad.toInt()
        animalCrossingPalettePositionToColorMap[0x02.toByte()] = 0xffef559c.toInt()
        animalCrossingPalettePositionToColorMap[0x03.toByte()] = 0xffff65ad.toInt()
        animalCrossingPalettePositionToColorMap[0x04.toByte()] = 0xffff0063.toInt()
        animalCrossingPalettePositionToColorMap[0x05.toByte()] = 0xffbd4573.toInt()
        animalCrossingPalettePositionToColorMap[0x06.toByte()] = 0xffce0052.toInt()
        animalCrossingPalettePositionToColorMap[0x07.toByte()] = 0xff9c0031.toInt()
        animalCrossingPalettePositionToColorMap[0x08.toByte()] = 0xff522031.toInt()

        animalCrossingPalettePositionToColorMap[0x10.toByte()] = 0xffffbace.toInt()
        animalCrossingPalettePositionToColorMap[0x11.toByte()] = 0xffff7573.toInt()
        animalCrossingPalettePositionToColorMap[0x12.toByte()] = 0xffde3010.toInt()
        animalCrossingPalettePositionToColorMap[0x13.toByte()] = 0xffff5542.toInt()
        animalCrossingPalettePositionToColorMap[0x14.toByte()] = 0xffff0000.toInt()
        animalCrossingPalettePositionToColorMap[0x15.toByte()] = 0xffce6563.toInt()
        animalCrossingPalettePositionToColorMap[0x16.toByte()] = 0xffbd4542.toInt()
        animalCrossingPalettePositionToColorMap[0x17.toByte()] = 0xffbd0000.toInt()
        animalCrossingPalettePositionToColorMap[0x18.toByte()] = 0xff8c2021.toInt()

        animalCrossingPalettePositionToColorMap[0x20.toByte()] = 0xffdecfbd.toInt()
        animalCrossingPalettePositionToColorMap[0x21.toByte()] = 0xffffcf63.toInt()
        animalCrossingPalettePositionToColorMap[0x22.toByte()] = 0xffde6521.toInt()
        animalCrossingPalettePositionToColorMap[0x23.toByte()] = 0xffffaa21.toInt()
        animalCrossingPalettePositionToColorMap[0x24.toByte()] = 0xffff6500.toInt()
        animalCrossingPalettePositionToColorMap[0x25.toByte()] = 0xffbd8a52.toInt()
        animalCrossingPalettePositionToColorMap[0x26.toByte()] = 0xffde4500.toInt()
        animalCrossingPalettePositionToColorMap[0x27.toByte()] = 0xffbd4500.toInt()
        animalCrossingPalettePositionToColorMap[0x28.toByte()] = 0xff633010.toInt()

        animalCrossingPalettePositionToColorMap[0x30.toByte()] = 0xffffefde.toInt()
        animalCrossingPalettePositionToColorMap[0x31.toByte()] = 0xffffdfce.toInt()
        animalCrossingPalettePositionToColorMap[0x32.toByte()] = 0xffffcfad.toInt()
        animalCrossingPalettePositionToColorMap[0x33.toByte()] = 0xffffba8c.toInt()
        animalCrossingPalettePositionToColorMap[0x34.toByte()] = 0xffffaa8c.toInt()
        animalCrossingPalettePositionToColorMap[0x35.toByte()] = 0xffde8a63.toInt()
        animalCrossingPalettePositionToColorMap[0x36.toByte()] = 0xffbd6542.toInt()
        animalCrossingPalettePositionToColorMap[0x37.toByte()] = 0xff9c5531.toInt()
        animalCrossingPalettePositionToColorMap[0x38.toByte()] = 0xff8c4521.toInt()

        animalCrossingPalettePositionToColorMap[0x40.toByte()] = 0xffffcfff.toInt()
        animalCrossingPalettePositionToColorMap[0x41.toByte()] = 0xffef8aff.toInt()
        animalCrossingPalettePositionToColorMap[0x42.toByte()] = 0xffce65de.toInt()
        animalCrossingPalettePositionToColorMap[0x43.toByte()] = 0xffbd8ace.toInt()
        animalCrossingPalettePositionToColorMap[0x44.toByte()] = 0xffce00ff.toInt()
        animalCrossingPalettePositionToColorMap[0x45.toByte()] = 0xff9c659c.toInt()
        animalCrossingPalettePositionToColorMap[0x46.toByte()] = 0xff8c00ad.toInt()
        animalCrossingPalettePositionToColorMap[0x47.toByte()] = 0xff520073.toInt()
        animalCrossingPalettePositionToColorMap[0x48.toByte()] = 0xff310042.toInt()

        animalCrossingPalettePositionToColorMap[0x50.toByte()] = 0xffffbaff.toInt()
        animalCrossingPalettePositionToColorMap[0x51.toByte()] = 0xffff9aff.toInt()
        animalCrossingPalettePositionToColorMap[0x52.toByte()] = 0xffde20bd.toInt()
        animalCrossingPalettePositionToColorMap[0x53.toByte()] = 0xffff55ef.toInt()
        animalCrossingPalettePositionToColorMap[0x54.toByte()] = 0xffff00ce.toInt()
        animalCrossingPalettePositionToColorMap[0x55.toByte()] = 0xff8c5573.toInt()
        animalCrossingPalettePositionToColorMap[0x56.toByte()] = 0xffbd009c.toInt()
        animalCrossingPalettePositionToColorMap[0x57.toByte()] = 0xff8c0063.toInt()
        animalCrossingPalettePositionToColorMap[0x58.toByte()] = 0xff520042.toInt()

        animalCrossingPalettePositionToColorMap[0x60.toByte()] = 0xffdeba9c.toInt()
        animalCrossingPalettePositionToColorMap[0x61.toByte()] = 0xffceaa73.toInt()
        animalCrossingPalettePositionToColorMap[0x62.toByte()] = 0xff734531.toInt()
        animalCrossingPalettePositionToColorMap[0x63.toByte()] = 0xffad7542.toInt()
        animalCrossingPalettePositionToColorMap[0x64.toByte()] = 0xff9c3000.toInt()
        animalCrossingPalettePositionToColorMap[0x65.toByte()] = 0xff733021.toInt()
        animalCrossingPalettePositionToColorMap[0x66.toByte()] = 0xff522000.toInt()
        animalCrossingPalettePositionToColorMap[0x67.toByte()] = 0xff311000.toInt()
        animalCrossingPalettePositionToColorMap[0x68.toByte()] = 0xff211000.toInt()

        animalCrossingPalettePositionToColorMap[0x70.toByte()] = 0xffffffce.toInt()
        animalCrossingPalettePositionToColorMap[0x71.toByte()] = 0xffffff73.toInt()
        animalCrossingPalettePositionToColorMap[0x72.toByte()] = 0xffdedf21.toInt()
        animalCrossingPalettePositionToColorMap[0x73.toByte()] = 0xffffff00.toInt()
        animalCrossingPalettePositionToColorMap[0x74.toByte()] = 0xffffdf00.toInt()
        animalCrossingPalettePositionToColorMap[0x75.toByte()] = 0xffceaa00.toInt()
        animalCrossingPalettePositionToColorMap[0x76.toByte()] = 0xff9c9a00.toInt()
        animalCrossingPalettePositionToColorMap[0x77.toByte()] = 0xff8c7500.toInt()
        animalCrossingPalettePositionToColorMap[0x78.toByte()] = 0xff525500.toInt()

        animalCrossingPalettePositionToColorMap[0x80.toByte()] = 0xffdebaff.toInt()
        animalCrossingPalettePositionToColorMap[0x81.toByte()] = 0xffbd9aef.toInt()
        animalCrossingPalettePositionToColorMap[0x82.toByte()] = 0xff6330ce.toInt()
        animalCrossingPalettePositionToColorMap[0x83.toByte()] = 0xff9c55ff.toInt()
        animalCrossingPalettePositionToColorMap[0x84.toByte()] = 0xff6300ff.toInt()
        animalCrossingPalettePositionToColorMap[0x85.toByte()] = 0xff52458c.toInt()
        animalCrossingPalettePositionToColorMap[0x86.toByte()] = 0xff42009c.toInt()
        animalCrossingPalettePositionToColorMap[0x87.toByte()] = 0xff210063.toInt()
        animalCrossingPalettePositionToColorMap[0x88.toByte()] = 0xff211031.toInt()

        animalCrossingPalettePositionToColorMap[0x90.toByte()] = 0xffbdbaff.toInt()
        animalCrossingPalettePositionToColorMap[0x91.toByte()] = 0xff8c9aff.toInt()
        animalCrossingPalettePositionToColorMap[0x92.toByte()] = 0xff3130ad.toInt()
        animalCrossingPalettePositionToColorMap[0x93.toByte()] = 0xff3155ef.toInt()
        animalCrossingPalettePositionToColorMap[0x94.toByte()] = 0xff0000ff.toInt()
        animalCrossingPalettePositionToColorMap[0x95.toByte()] = 0xff31308c.toInt()
        animalCrossingPalettePositionToColorMap[0x96.toByte()] = 0xff0000ad.toInt()
        animalCrossingPalettePositionToColorMap[0x97.toByte()] = 0xff101063.toInt()
        animalCrossingPalettePositionToColorMap[0x98.toByte()] = 0xff000021.toInt()

        animalCrossingPalettePositionToColorMap[0xa0.toByte()] = 0xff9cefbd.toInt()
        animalCrossingPalettePositionToColorMap[0xa1.toByte()] = 0xff63cf73.toInt()
        animalCrossingPalettePositionToColorMap[0xa2.toByte()] = 0xff216510.toInt()
        animalCrossingPalettePositionToColorMap[0xa3.toByte()] = 0xff42aa31.toInt()
        animalCrossingPalettePositionToColorMap[0xa4.toByte()] = 0xff008a31.toInt()
        animalCrossingPalettePositionToColorMap[0xa5.toByte()] = 0xff527552.toInt()
        animalCrossingPalettePositionToColorMap[0xa6.toByte()] = 0xff215500.toInt()
        animalCrossingPalettePositionToColorMap[0xa7.toByte()] = 0xff103021.toInt()
        animalCrossingPalettePositionToColorMap[0xa8.toByte()] = 0xff002010.toInt()

        animalCrossingPalettePositionToColorMap[0xb0.toByte()] = 0xffdeffbd.toInt()
        animalCrossingPalettePositionToColorMap[0xb1.toByte()] = 0xffceff8c.toInt()
        animalCrossingPalettePositionToColorMap[0xb2.toByte()] = 0xff8caa52.toInt()
        animalCrossingPalettePositionToColorMap[0xb3.toByte()] = 0xffaddf8c.toInt()
        animalCrossingPalettePositionToColorMap[0xb4.toByte()] = 0xff8cff00.toInt()
        animalCrossingPalettePositionToColorMap[0xb5.toByte()] = 0xffadba9c.toInt()
        animalCrossingPalettePositionToColorMap[0xb6.toByte()] = 0xff63ba00.toInt()
        animalCrossingPalettePositionToColorMap[0xb7.toByte()] = 0xff529a00.toInt()
        animalCrossingPalettePositionToColorMap[0xb8.toByte()] = 0xff316500.toInt()

        animalCrossingPalettePositionToColorMap[0xc0.toByte()] = 0xffbddfff.toInt()
        animalCrossingPalettePositionToColorMap[0xc1.toByte()] = 0xff73cfff.toInt()
        animalCrossingPalettePositionToColorMap[0xc2.toByte()] = 0xff31559c.toInt()
        animalCrossingPalettePositionToColorMap[0xc3.toByte()] = 0xff639aff.toInt()
        animalCrossingPalettePositionToColorMap[0xc4.toByte()] = 0xff1075ff.toInt()
        animalCrossingPalettePositionToColorMap[0xc5.toByte()] = 0xff4275ad.toInt()
        animalCrossingPalettePositionToColorMap[0xc6.toByte()] = 0xff214573.toInt()
        animalCrossingPalettePositionToColorMap[0xc7.toByte()] = 0xff002073.toInt()
        animalCrossingPalettePositionToColorMap[0xc8.toByte()] = 0xff001042.toInt()

        animalCrossingPalettePositionToColorMap[0xd0.toByte()] = 0xffadffff.toInt()
        animalCrossingPalettePositionToColorMap[0xd1.toByte()] = 0xff52ffff.toInt()
        animalCrossingPalettePositionToColorMap[0xd2.toByte()] = 0xff008abd.toInt()
        animalCrossingPalettePositionToColorMap[0xd3.toByte()] = 0xff52bace.toInt()
        animalCrossingPalettePositionToColorMap[0xd4.toByte()] = 0xff00cfff.toInt()
        animalCrossingPalettePositionToColorMap[0xd5.toByte()] = 0xff429aad.toInt()
        animalCrossingPalettePositionToColorMap[0xd6.toByte()] = 0xff00658c.toInt()
        animalCrossingPalettePositionToColorMap[0xd7.toByte()] = 0xff004552.toInt()
        animalCrossingPalettePositionToColorMap[0xd8.toByte()] = 0xff002031.toInt()

        animalCrossingPalettePositionToColorMap[0xe0.toByte()] = 0xffceffef.toInt()
        animalCrossingPalettePositionToColorMap[0xe1.toByte()] = 0xffadefde.toInt()
        animalCrossingPalettePositionToColorMap[0xe2.toByte()] = 0xff31cfad.toInt()
        animalCrossingPalettePositionToColorMap[0xe3.toByte()] = 0xff52efbd.toInt()
        animalCrossingPalettePositionToColorMap[0xe4.toByte()] = 0xff00ffce.toInt()
        animalCrossingPalettePositionToColorMap[0xe5.toByte()] = 0xff73aaad.toInt()
        animalCrossingPalettePositionToColorMap[0xe6.toByte()] = 0xff00aa9c.toInt()
        animalCrossingPalettePositionToColorMap[0xe7.toByte()] = 0xff008a73.toInt()
        animalCrossingPalettePositionToColorMap[0xe8.toByte()] = 0xff004531.toInt()

        animalCrossingPalettePositionToColorMap[0xf0.toByte()] = 0xffadffad.toInt()
        animalCrossingPalettePositionToColorMap[0xf1.toByte()] = 0xff73ff73.toInt()
        animalCrossingPalettePositionToColorMap[0xf2.toByte()] = 0xff63df42.toInt()
        animalCrossingPalettePositionToColorMap[0xf3.toByte()] = 0xff00ff00.toInt()
        animalCrossingPalettePositionToColorMap[0xf4.toByte()] = 0xff21df21.toInt()
        animalCrossingPalettePositionToColorMap[0xf5.toByte()] = 0xff52ba52.toInt()
        animalCrossingPalettePositionToColorMap[0xf6.toByte()] = 0xff00ba00.toInt()
        animalCrossingPalettePositionToColorMap[0xf7.toByte()] = 0xff008a00.toInt()
        animalCrossingPalettePositionToColorMap[0xf8.toByte()] = 0xff214521.toInt()

        animalCrossingPalettePositionToColorMap[0x0f.toByte()] = 0xffffffff.toInt()
        animalCrossingPalettePositionToColorMap[0x1f.toByte()] = 0xffefefef.toInt()
        animalCrossingPalettePositionToColorMap[0x2f.toByte()] = 0xffdedfde.toInt()
        animalCrossingPalettePositionToColorMap[0x3f.toByte()] = 0xffcecfce.toInt()
        animalCrossingPalettePositionToColorMap[0x4f.toByte()] = 0xffbdbabd.toInt()
        animalCrossingPalettePositionToColorMap[0x5f.toByte()] = 0xffadaaad.toInt()
        animalCrossingPalettePositionToColorMap[0x6f.toByte()] = 0xff9c9a9c.toInt()
        animalCrossingPalettePositionToColorMap[0x7f.toByte()] = 0xff8c8a8c.toInt()
        animalCrossingPalettePositionToColorMap[0x8f.toByte()] = 0xff737573.toInt()
        animalCrossingPalettePositionToColorMap[0x9f.toByte()] = 0xff636563.toInt()
        animalCrossingPalettePositionToColorMap[0xaf.toByte()] = 0xff525552.toInt()
        animalCrossingPalettePositionToColorMap[0xbf.toByte()] = 0xff424542.toInt()
        animalCrossingPalettePositionToColorMap[0xcf.toByte()] = 0xff313031.toInt()
        animalCrossingPalettePositionToColorMap[0xdf.toByte()] = 0xff212021.toInt()
        animalCrossingPalettePositionToColorMap[0xef.toByte()] = 0xff000000.toInt()

        animalCrossingPaletteColorToPositionMap.entries.associateBy({ it.value }) { it.key }
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
        this.colorPalettePositions = colorPalettePositions.toList()
        this.imagePositionByteData = imageData
        this.imagePixels = getPixelColorsFromBytes(imageData).map { animalCrossingPalettePositionToColorMap[this.colorPalettePositions!![it]]!! }.toIntArray()
    }

    private fun getPixelColorsFromBytes(bytes: ByteArray): ArrayList<Int> {
        val arrayListOfPixels = ArrayList<Int>()

        for (byte in bytes) {
            val firstHalf = (byte.toInt() shr 4) and 0x0f
            val secondHalf = byte.toInt() and 0x0f

            arrayListOfPixels.add(animalCrossingPalettePositionToColorMap[firstHalf.toByte()]!!)
            arrayListOfPixels.add(animalCrossingPalettePositionToColorMap[secondHalf.toByte()]!!)
        }
        return arrayListOfPixels
    }

    fun testing() {
        //https://medium.com/mobile-app-development-publication/simple-kotlin-null-check-for-multiple-mutable-variables-b095f7ac9bf1
        if (title != null && author!= null) {
            println(title)
            println(author)
        }
    }

    fun toQRCode(): Bitmap {
        /*
        If all information is here make QR code.
        Todo: Check if data missing and handle it
         */

        var qrByteArray = this.title.toByteArray()

        while (qrByteArray.size < 42) {
            qrByteArray += 0
        }

        qrByteArray += byteArrayOf(0, 0)

        qrByteArray += this.author.toByteArray()
        while (qrByteArray.size < 62) {
            qrByteArray += 0
        }

        qrByteArray += byteArrayOf(0, 0, 0, 0)

        qrByteArray += this.town.toByteArray()
        while (qrByteArray.size < 84) {
            qrByteArray += 0
        }

        qrByteArray += byteArrayOf(0, 0, 0, 0)

        if (BuildConfig.DEBUG && this.colorPalettePositions.size != 15) {
            error("Assertion failed")
        }
        //Todo: check if any strange behavior with adding a ByteArray with a List<Byte>
        qrByteArray += this.colorPalettePositions
        while (qrByteArray.size < 103) {
            qrByteArray += 0
        }

        qrByteArray += byteArrayOf(0, 0, 0, 0, 0)

        // If data is already available
        qrByteArray += imagePositionByteData

        /* Pixel color to positionbyte data
        //if (imagePositionByteData != null)
        for (pixelPair in imagePixels.toList().chunked(2)) {

            val firstHalfOfByte = this.colorPalettePositions.indexOf(pixelPair[0]) shl 4
            val secondHalfOfByte = this.colorPalettePositions.indexOf(pixelPair[1])
            val combinedByte = firstHalfOfByte or secondHalfOfByte
            qrByteArray += combinedByte.toByte()
        }*/

        //temp return
        val codeWriter = MultiFormatWriter()
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        try {

            val bitMatrix = codeWriter.encode(qrByteArray.toString(Charsets.ISO_8859_1),
                    //val bitMatrix = codeWriter.encode(getEncoder().encodeToString(qrByteArrary),
                    BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
        //return Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
    }

}
class AnimalCrossingColors {
    val animalCrossingPalettePositionToColorMap = mutableMapOf<Byte, Int>()
    val animalCrossingPaletteColorToPositionMap = mutableMapOf<Int, Byte>()

    init {
        animalCrossingPalettePositionToColorMap[0x00.toByte()] = 0xffffefff.toInt()
        animalCrossingPalettePositionToColorMap[0x01.toByte()] = 0xffff9aad.toInt()
        animalCrossingPalettePositionToColorMap[0x02.toByte()] = 0xffef559c.toInt()
        animalCrossingPalettePositionToColorMap[0x03.toByte()] = 0xffff65ad.toInt()
        animalCrossingPalettePositionToColorMap[0x04.toByte()] = 0xffff0063.toInt()
        animalCrossingPalettePositionToColorMap[0x05.toByte()] = 0xffbd4573.toInt()
        animalCrossingPalettePositionToColorMap[0x06.toByte()] = 0xffce0052.toInt()
        animalCrossingPalettePositionToColorMap[0x07.toByte()] = 0xff9c0031.toInt()
        animalCrossingPalettePositionToColorMap[0x08.toByte()] = 0xff522031.toInt()

        animalCrossingPalettePositionToColorMap[0x10.toByte()] = 0xffffbace.toInt()
        animalCrossingPalettePositionToColorMap[0x11.toByte()] = 0xffff7573.toInt()
        animalCrossingPalettePositionToColorMap[0x12.toByte()] = 0xffde3010.toInt()
        animalCrossingPalettePositionToColorMap[0x13.toByte()] = 0xffff5542.toInt()
        animalCrossingPalettePositionToColorMap[0x14.toByte()] = 0xffff0000.toInt()
        animalCrossingPalettePositionToColorMap[0x15.toByte()] = 0xffce6563.toInt()
        animalCrossingPalettePositionToColorMap[0x16.toByte()] = 0xffbd4542.toInt()
        animalCrossingPalettePositionToColorMap[0x17.toByte()] = 0xffbd0000.toInt()
        animalCrossingPalettePositionToColorMap[0x18.toByte()] = 0xff8c2021.toInt()

        animalCrossingPalettePositionToColorMap[0x20.toByte()] = 0xffdecfbd.toInt()
        animalCrossingPalettePositionToColorMap[0x21.toByte()] = 0xffffcf63.toInt()
        animalCrossingPalettePositionToColorMap[0x22.toByte()] = 0xffde6521.toInt()
        animalCrossingPalettePositionToColorMap[0x23.toByte()] = 0xffffaa21.toInt()
        animalCrossingPalettePositionToColorMap[0x24.toByte()] = 0xffff6500.toInt()
        animalCrossingPalettePositionToColorMap[0x25.toByte()] = 0xffbd8a52.toInt()
        animalCrossingPalettePositionToColorMap[0x26.toByte()] = 0xffde4500.toInt()
        animalCrossingPalettePositionToColorMap[0x27.toByte()] = 0xffbd4500.toInt()
        animalCrossingPalettePositionToColorMap[0x28.toByte()] = 0xff633010.toInt()

        animalCrossingPalettePositionToColorMap[0x30.toByte()] = 0xffffefde.toInt()
        animalCrossingPalettePositionToColorMap[0x31.toByte()] = 0xffffdfce.toInt()
        animalCrossingPalettePositionToColorMap[0x32.toByte()] = 0xffffcfad.toInt()
        animalCrossingPalettePositionToColorMap[0x33.toByte()] = 0xffffba8c.toInt()
        animalCrossingPalettePositionToColorMap[0x34.toByte()] = 0xffffaa8c.toInt()
        animalCrossingPalettePositionToColorMap[0x35.toByte()] = 0xffde8a63.toInt()
        animalCrossingPalettePositionToColorMap[0x36.toByte()] = 0xffbd6542.toInt()
        animalCrossingPalettePositionToColorMap[0x37.toByte()] = 0xff9c5531.toInt()
        animalCrossingPalettePositionToColorMap[0x38.toByte()] = 0xff8c4521.toInt()

        animalCrossingPalettePositionToColorMap[0x40.toByte()] = 0xffffcfff.toInt()
        animalCrossingPalettePositionToColorMap[0x41.toByte()] = 0xffef8aff.toInt()
        animalCrossingPalettePositionToColorMap[0x42.toByte()] = 0xffce65de.toInt()
        animalCrossingPalettePositionToColorMap[0x43.toByte()] = 0xffbd8ace.toInt()
        animalCrossingPalettePositionToColorMap[0x44.toByte()] = 0xffce00ff.toInt()
        animalCrossingPalettePositionToColorMap[0x45.toByte()] = 0xff9c659c.toInt()
        animalCrossingPalettePositionToColorMap[0x46.toByte()] = 0xff8c00ad.toInt()
        animalCrossingPalettePositionToColorMap[0x47.toByte()] = 0xff520073.toInt()
        animalCrossingPalettePositionToColorMap[0x48.toByte()] = 0xff310042.toInt()

        animalCrossingPalettePositionToColorMap[0x50.toByte()] = 0xffffbaff.toInt()
        animalCrossingPalettePositionToColorMap[0x51.toByte()] = 0xffff9aff.toInt()
        animalCrossingPalettePositionToColorMap[0x52.toByte()] = 0xffde20bd.toInt()
        animalCrossingPalettePositionToColorMap[0x53.toByte()] = 0xffff55ef.toInt()
        animalCrossingPalettePositionToColorMap[0x54.toByte()] = 0xffff00ce.toInt()
        animalCrossingPalettePositionToColorMap[0x55.toByte()] = 0xff8c5573.toInt()
        animalCrossingPalettePositionToColorMap[0x56.toByte()] = 0xffbd009c.toInt()
        animalCrossingPalettePositionToColorMap[0x57.toByte()] = 0xff8c0063.toInt()
        animalCrossingPalettePositionToColorMap[0x58.toByte()] = 0xff520042.toInt()

        animalCrossingPalettePositionToColorMap[0x60.toByte()] = 0xffdeba9c.toInt()
        animalCrossingPalettePositionToColorMap[0x61.toByte()] = 0xffceaa73.toInt()
        animalCrossingPalettePositionToColorMap[0x62.toByte()] = 0xff734531.toInt()
        animalCrossingPalettePositionToColorMap[0x63.toByte()] = 0xffad7542.toInt()
        animalCrossingPalettePositionToColorMap[0x64.toByte()] = 0xff9c3000.toInt()
        animalCrossingPalettePositionToColorMap[0x65.toByte()] = 0xff733021.toInt()
        animalCrossingPalettePositionToColorMap[0x66.toByte()] = 0xff522000.toInt()
        animalCrossingPalettePositionToColorMap[0x67.toByte()] = 0xff311000.toInt()
        animalCrossingPalettePositionToColorMap[0x68.toByte()] = 0xff211000.toInt()

        animalCrossingPalettePositionToColorMap[0x70.toByte()] = 0xffffffce.toInt()
        animalCrossingPalettePositionToColorMap[0x71.toByte()] = 0xffffff73.toInt()
        animalCrossingPalettePositionToColorMap[0x72.toByte()] = 0xffdedf21.toInt()
        animalCrossingPalettePositionToColorMap[0x73.toByte()] = 0xffffff00.toInt()
        animalCrossingPalettePositionToColorMap[0x74.toByte()] = 0xffffdf00.toInt()
        animalCrossingPalettePositionToColorMap[0x75.toByte()] = 0xffceaa00.toInt()
        animalCrossingPalettePositionToColorMap[0x76.toByte()] = 0xff9c9a00.toInt()
        animalCrossingPalettePositionToColorMap[0x77.toByte()] = 0xff8c7500.toInt()
        animalCrossingPalettePositionToColorMap[0x78.toByte()] = 0xff525500.toInt()

        animalCrossingPalettePositionToColorMap[0x80.toByte()] = 0xffdebaff.toInt()
        animalCrossingPalettePositionToColorMap[0x81.toByte()] = 0xffbd9aef.toInt()
        animalCrossingPalettePositionToColorMap[0x82.toByte()] = 0xff6330ce.toInt()
        animalCrossingPalettePositionToColorMap[0x83.toByte()] = 0xff9c55ff.toInt()
        animalCrossingPalettePositionToColorMap[0x84.toByte()] = 0xff6300ff.toInt()
        animalCrossingPalettePositionToColorMap[0x85.toByte()] = 0xff52458c.toInt()
        animalCrossingPalettePositionToColorMap[0x86.toByte()] = 0xff42009c.toInt()
        animalCrossingPalettePositionToColorMap[0x87.toByte()] = 0xff210063.toInt()
        animalCrossingPalettePositionToColorMap[0x88.toByte()] = 0xff211031.toInt()

        animalCrossingPalettePositionToColorMap[0x90.toByte()] = 0xffbdbaff.toInt()
        animalCrossingPalettePositionToColorMap[0x91.toByte()] = 0xff8c9aff.toInt()
        animalCrossingPalettePositionToColorMap[0x92.toByte()] = 0xff3130ad.toInt()
        animalCrossingPalettePositionToColorMap[0x93.toByte()] = 0xff3155ef.toInt()
        animalCrossingPalettePositionToColorMap[0x94.toByte()] = 0xff0000ff.toInt()
        animalCrossingPalettePositionToColorMap[0x95.toByte()] = 0xff31308c.toInt()
        animalCrossingPalettePositionToColorMap[0x96.toByte()] = 0xff0000ad.toInt()
        animalCrossingPalettePositionToColorMap[0x97.toByte()] = 0xff101063.toInt()
        animalCrossingPalettePositionToColorMap[0x98.toByte()] = 0xff000021.toInt()

        animalCrossingPalettePositionToColorMap[0xa0.toByte()] = 0xff9cefbd.toInt()
        animalCrossingPalettePositionToColorMap[0xa1.toByte()] = 0xff63cf73.toInt()
        animalCrossingPalettePositionToColorMap[0xa2.toByte()] = 0xff216510.toInt()
        animalCrossingPalettePositionToColorMap[0xa3.toByte()] = 0xff42aa31.toInt()
        animalCrossingPalettePositionToColorMap[0xa4.toByte()] = 0xff008a31.toInt()
        animalCrossingPalettePositionToColorMap[0xa5.toByte()] = 0xff527552.toInt()
        animalCrossingPalettePositionToColorMap[0xa6.toByte()] = 0xff215500.toInt()
        animalCrossingPalettePositionToColorMap[0xa7.toByte()] = 0xff103021.toInt()
        animalCrossingPalettePositionToColorMap[0xa8.toByte()] = 0xff002010.toInt()

        animalCrossingPalettePositionToColorMap[0xb0.toByte()] = 0xffdeffbd.toInt()
        animalCrossingPalettePositionToColorMap[0xb1.toByte()] = 0xffceff8c.toInt()
        animalCrossingPalettePositionToColorMap[0xb2.toByte()] = 0xff8caa52.toInt()
        animalCrossingPalettePositionToColorMap[0xb3.toByte()] = 0xffaddf8c.toInt()
        animalCrossingPalettePositionToColorMap[0xb4.toByte()] = 0xff8cff00.toInt()
        animalCrossingPalettePositionToColorMap[0xb5.toByte()] = 0xffadba9c.toInt()
        animalCrossingPalettePositionToColorMap[0xb6.toByte()] = 0xff63ba00.toInt()
        animalCrossingPalettePositionToColorMap[0xb7.toByte()] = 0xff529a00.toInt()
        animalCrossingPalettePositionToColorMap[0xb8.toByte()] = 0xff316500.toInt()

        animalCrossingPalettePositionToColorMap[0xc0.toByte()] = 0xffbddfff.toInt()
        animalCrossingPalettePositionToColorMap[0xc1.toByte()] = 0xff73cfff.toInt()
        animalCrossingPalettePositionToColorMap[0xc2.toByte()] = 0xff31559c.toInt()
        animalCrossingPalettePositionToColorMap[0xc3.toByte()] = 0xff639aff.toInt()
        animalCrossingPalettePositionToColorMap[0xc4.toByte()] = 0xff1075ff.toInt()
        animalCrossingPalettePositionToColorMap[0xc5.toByte()] = 0xff4275ad.toInt()
        animalCrossingPalettePositionToColorMap[0xc6.toByte()] = 0xff214573.toInt()
        animalCrossingPalettePositionToColorMap[0xc7.toByte()] = 0xff002073.toInt()
        animalCrossingPalettePositionToColorMap[0xc8.toByte()] = 0xff001042.toInt()

        animalCrossingPalettePositionToColorMap[0xd0.toByte()] = 0xffadffff.toInt()
        animalCrossingPalettePositionToColorMap[0xd1.toByte()] = 0xff52ffff.toInt()
        animalCrossingPalettePositionToColorMap[0xd2.toByte()] = 0xff008abd.toInt()
        animalCrossingPalettePositionToColorMap[0xd3.toByte()] = 0xff52bace.toInt()
        animalCrossingPalettePositionToColorMap[0xd4.toByte()] = 0xff00cfff.toInt()
        animalCrossingPalettePositionToColorMap[0xd5.toByte()] = 0xff429aad.toInt()
        animalCrossingPalettePositionToColorMap[0xd6.toByte()] = 0xff00658c.toInt()
        animalCrossingPalettePositionToColorMap[0xd7.toByte()] = 0xff004552.toInt()
        animalCrossingPalettePositionToColorMap[0xd8.toByte()] = 0xff002031.toInt()

        animalCrossingPalettePositionToColorMap[0xe0.toByte()] = 0xffceffef.toInt()
        animalCrossingPalettePositionToColorMap[0xe1.toByte()] = 0xffadefde.toInt()
        animalCrossingPalettePositionToColorMap[0xe2.toByte()] = 0xff31cfad.toInt()
        animalCrossingPalettePositionToColorMap[0xe3.toByte()] = 0xff52efbd.toInt()
        animalCrossingPalettePositionToColorMap[0xe4.toByte()] = 0xff00ffce.toInt()
        animalCrossingPalettePositionToColorMap[0xe5.toByte()] = 0xff73aaad.toInt()
        animalCrossingPalettePositionToColorMap[0xe6.toByte()] = 0xff00aa9c.toInt()
        animalCrossingPalettePositionToColorMap[0xe7.toByte()] = 0xff008a73.toInt()
        animalCrossingPalettePositionToColorMap[0xe8.toByte()] = 0xff004531.toInt()

        animalCrossingPalettePositionToColorMap[0xf0.toByte()] = 0xffadffad.toInt()
        animalCrossingPalettePositionToColorMap[0xf1.toByte()] = 0xff73ff73.toInt()
        animalCrossingPalettePositionToColorMap[0xf2.toByte()] = 0xff63df42.toInt()
        animalCrossingPalettePositionToColorMap[0xf3.toByte()] = 0xff00ff00.toInt()
        animalCrossingPalettePositionToColorMap[0xf4.toByte()] = 0xff21df21.toInt()
        animalCrossingPalettePositionToColorMap[0xf5.toByte()] = 0xff52ba52.toInt()
        animalCrossingPalettePositionToColorMap[0xf6.toByte()] = 0xff00ba00.toInt()
        animalCrossingPalettePositionToColorMap[0xf7.toByte()] = 0xff008a00.toInt()
        animalCrossingPalettePositionToColorMap[0xf8.toByte()] = 0xff214521.toInt()

        animalCrossingPalettePositionToColorMap[0x0f.toByte()] = 0xffffffff.toInt()
        animalCrossingPalettePositionToColorMap[0x1f.toByte()] = 0xffefefef.toInt()
        animalCrossingPalettePositionToColorMap[0x2f.toByte()] = 0xffdedfde.toInt()
        animalCrossingPalettePositionToColorMap[0x3f.toByte()] = 0xffcecfce.toInt()
        animalCrossingPalettePositionToColorMap[0x4f.toByte()] = 0xffbdbabd.toInt()
        animalCrossingPalettePositionToColorMap[0x5f.toByte()] = 0xffadaaad.toInt()
        animalCrossingPalettePositionToColorMap[0x6f.toByte()] = 0xff9c9a9c.toInt()
        animalCrossingPalettePositionToColorMap[0x7f.toByte()] = 0xff8c8a8c.toInt()
        animalCrossingPalettePositionToColorMap[0x8f.toByte()] = 0xff737573.toInt()
        animalCrossingPalettePositionToColorMap[0x9f.toByte()] = 0xff636563.toInt()
        animalCrossingPalettePositionToColorMap[0xaf.toByte()] = 0xff525552.toInt()
        animalCrossingPalettePositionToColorMap[0xbf.toByte()] = 0xff424542.toInt()
        animalCrossingPalettePositionToColorMap[0xcf.toByte()] = 0xff313031.toInt()
        animalCrossingPalettePositionToColorMap[0xdf.toByte()] = 0xff212021.toInt()
        animalCrossingPalettePositionToColorMap[0xef.toByte()] = 0xff000000.toInt()

        animalCrossingPaletteColorToPositionMap.entries.associateBy({ it.value }) { it.key }
    }

}