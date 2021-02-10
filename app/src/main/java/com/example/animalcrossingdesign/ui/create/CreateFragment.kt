package com.example.animalcrossingdesign.ui.create

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.R
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.lang.Exception
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import kotlin.experimental.and


class CreateFragment : Fragment() {


    private lateinit var customadapter: CustomAdapter
    private lateinit var createViewModel: CreateViewModel

    lateinit var imageView: ImageView
    lateinit var button: Button
    private lateinit var crop_switch: Switch
    private lateinit var split_images_hashmap: ArrayList<HashMap<String, Any>>
    private lateinit var textViewRowCol: TextView
    private lateinit var recycleview: RecyclerView
    //Todo: Is it bad to put root here instead of inside constructor?
    private lateinit var root: View

    private val PICK_IMAGE = 100
    private val CROP_IMAGE = 101

    private var imageUri: Uri? = null

    val animalCrossingPaletteColors = mutableMapOf<Byte, Int>()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        animalCrossingPaletteColors[0x00.toByte()] = 0xffffefff.toInt()
        animalCrossingPaletteColors[0x01.toByte()] = 0xffff9aad.toInt()
        animalCrossingPaletteColors[0x02.toByte()] = 0xffef559c.toInt()
        animalCrossingPaletteColors[0x03.toByte()] = 0xffff65ad.toInt()
        animalCrossingPaletteColors[0x04.toByte()] = 0xffff0063.toInt()
        animalCrossingPaletteColors[0x05.toByte()] = 0xffbd4573.toInt()
        animalCrossingPaletteColors[0x06.toByte()] = 0xffce0052.toInt()
        animalCrossingPaletteColors[0x07.toByte()] = 0xff9c0031.toInt()
        animalCrossingPaletteColors[0x08.toByte()] = 0xff522031.toInt()

        animalCrossingPaletteColors[0x10.toByte()] = 0xffffbace.toInt()
        animalCrossingPaletteColors[0x11.toByte()] = 0xffff7573.toInt()
        animalCrossingPaletteColors[0x12.toByte()] = 0xffde3010.toInt()
        animalCrossingPaletteColors[0x13.toByte()] = 0xffff5542.toInt()
        animalCrossingPaletteColors[0x14.toByte()] = 0xffff0000.toInt()
        animalCrossingPaletteColors[0x15.toByte()] = 0xffce6563.toInt()
        animalCrossingPaletteColors[0x16.toByte()] = 0xffbd4542.toInt()
        animalCrossingPaletteColors[0x17.toByte()] = 0xffbd0000.toInt()
        animalCrossingPaletteColors[0x18.toByte()] = 0xff8c2021.toInt()

        animalCrossingPaletteColors[0x20.toByte()] = 0xffdecfbd.toInt()
        animalCrossingPaletteColors[0x21.toByte()] = 0xffffcf63.toInt()
        animalCrossingPaletteColors[0x22.toByte()] = 0xffde6521.toInt()
        animalCrossingPaletteColors[0x23.toByte()] = 0xffffaa21.toInt()
        animalCrossingPaletteColors[0x24.toByte()] = 0xffff6500.toInt()
        animalCrossingPaletteColors[0x25.toByte()] = 0xffbd8a52.toInt()
        animalCrossingPaletteColors[0x26.toByte()] = 0xffde4500.toInt()
        animalCrossingPaletteColors[0x27.toByte()] = 0xffbd4500.toInt()
        animalCrossingPaletteColors[0x28.toByte()] = 0xff633010.toInt()

        animalCrossingPaletteColors[0x30.toByte()] = 0xffffefde.toInt()
        animalCrossingPaletteColors[0x31.toByte()] = 0xffffdfce.toInt()
        animalCrossingPaletteColors[0x32.toByte()] = 0xffffcfad.toInt()
        animalCrossingPaletteColors[0x33.toByte()] = 0xffffba8c.toInt()
        animalCrossingPaletteColors[0x34.toByte()] = 0xffffaa8c.toInt()
        animalCrossingPaletteColors[0x35.toByte()] = 0xffde8a63.toInt()
        animalCrossingPaletteColors[0x36.toByte()] = 0xffbd6542.toInt()
        animalCrossingPaletteColors[0x37.toByte()] = 0xff9c5531.toInt()
        animalCrossingPaletteColors[0x38.toByte()] = 0xff8c4521.toInt()

        animalCrossingPaletteColors[0x40.toByte()] = 0xffffcfff.toInt()
        animalCrossingPaletteColors[0x41.toByte()] = 0xffef8aff.toInt()
        animalCrossingPaletteColors[0x42.toByte()] = 0xffce65de.toInt()
        animalCrossingPaletteColors[0x43.toByte()] = 0xffbd8ace.toInt()
        animalCrossingPaletteColors[0x44.toByte()] = 0xffce00ff.toInt()
        animalCrossingPaletteColors[0x45.toByte()] = 0xff9c659c.toInt()
        animalCrossingPaletteColors[0x46.toByte()] = 0xff8c00ad.toInt()
        animalCrossingPaletteColors[0x47.toByte()] = 0xff520073.toInt()
        animalCrossingPaletteColors[0x48.toByte()] = 0xff310042.toInt()

        animalCrossingPaletteColors[0x50.toByte()] = 0xffffbaff.toInt()
        animalCrossingPaletteColors[0x51.toByte()] = 0xffff9aff.toInt()
        animalCrossingPaletteColors[0x52.toByte()] = 0xffde20bd.toInt()
        animalCrossingPaletteColors[0x53.toByte()] = 0xffff55ef.toInt()
        animalCrossingPaletteColors[0x54.toByte()] = 0xffff00ce.toInt()
        animalCrossingPaletteColors[0x55.toByte()] = 0xff8c5573.toInt()
        animalCrossingPaletteColors[0x56.toByte()] = 0xffbd009c.toInt()
        animalCrossingPaletteColors[0x57.toByte()] = 0xff8c0063.toInt()
        animalCrossingPaletteColors[0x58.toByte()] = 0xff520042.toInt()

        animalCrossingPaletteColors[0x60.toByte()] = 0xffdeba9c.toInt()
        animalCrossingPaletteColors[0x61.toByte()] = 0xffceaa73.toInt()
        animalCrossingPaletteColors[0x62.toByte()] = 0xff734531.toInt()
        animalCrossingPaletteColors[0x63.toByte()] = 0xffad7542.toInt()
        animalCrossingPaletteColors[0x64.toByte()] = 0xff9c3000.toInt()
        animalCrossingPaletteColors[0x65.toByte()] = 0xff733021.toInt()
        animalCrossingPaletteColors[0x66.toByte()] = 0xff522000.toInt()
        animalCrossingPaletteColors[0x67.toByte()] = 0xff311000.toInt()
        animalCrossingPaletteColors[0x68.toByte()] = 0xff211000.toInt()

        animalCrossingPaletteColors[0x70.toByte()] = 0xffffffce.toInt()
        animalCrossingPaletteColors[0x71.toByte()] = 0xffffff73.toInt()
        animalCrossingPaletteColors[0x72.toByte()] = 0xffdedf21.toInt()
        animalCrossingPaletteColors[0x73.toByte()] = 0xffffff00.toInt()
        animalCrossingPaletteColors[0x74.toByte()] = 0xffffdf00.toInt()
        animalCrossingPaletteColors[0x75.toByte()] = 0xffceaa00.toInt()
        animalCrossingPaletteColors[0x76.toByte()] = 0xff9c9a00.toInt()
        animalCrossingPaletteColors[0x77.toByte()] = 0xff8c7500.toInt()
        animalCrossingPaletteColors[0x78.toByte()] = 0xff525500.toInt()

        animalCrossingPaletteColors[0x80.toByte()] = 0xffdebaff.toInt()
        animalCrossingPaletteColors[0x81.toByte()] = 0xffbd9aef.toInt()
        animalCrossingPaletteColors[0x82.toByte()] = 0xff6330ce.toInt()
        animalCrossingPaletteColors[0x83.toByte()] = 0xff9c55ff.toInt()
        animalCrossingPaletteColors[0x84.toByte()] = 0xff6300ff.toInt()
        animalCrossingPaletteColors[0x85.toByte()] = 0xff52458c.toInt()
        animalCrossingPaletteColors[0x86.toByte()] = 0xff42009c.toInt()
        animalCrossingPaletteColors[0x87.toByte()] = 0xff210063.toInt()
        animalCrossingPaletteColors[0x88.toByte()] = 0xff211031.toInt()

        animalCrossingPaletteColors[0x90.toByte()] = 0xffbdbaff.toInt()
        animalCrossingPaletteColors[0x91.toByte()] = 0xff8c9aff.toInt()
        animalCrossingPaletteColors[0x92.toByte()] = 0xff3130ad.toInt()
        animalCrossingPaletteColors[0x93.toByte()] = 0xff3155ef.toInt()
        animalCrossingPaletteColors[0x94.toByte()] = 0xff0000ff.toInt()
        animalCrossingPaletteColors[0x95.toByte()] = 0xff31308c.toInt()
        animalCrossingPaletteColors[0x96.toByte()] = 0xff0000ad.toInt()
        animalCrossingPaletteColors[0x97.toByte()] = 0xff101063.toInt()
        animalCrossingPaletteColors[0x98.toByte()] = 0xff000021.toInt()

        animalCrossingPaletteColors[0xa0.toByte()] = 0xff9cefbd.toInt()
        animalCrossingPaletteColors[0xa1.toByte()] = 0xff63cf73.toInt()
        animalCrossingPaletteColors[0xa2.toByte()] = 0xff216510.toInt()
        animalCrossingPaletteColors[0xa3.toByte()] = 0xff42aa31.toInt()
        animalCrossingPaletteColors[0xa4.toByte()] = 0xff008a31.toInt()
        animalCrossingPaletteColors[0xa5.toByte()] = 0xff527552.toInt()
        animalCrossingPaletteColors[0xa6.toByte()] = 0xff215500.toInt()
        animalCrossingPaletteColors[0xa7.toByte()] = 0xff103021.toInt()
        animalCrossingPaletteColors[0xa8.toByte()] = 0xff002010.toInt()

        animalCrossingPaletteColors[0xb0.toByte()] = 0xffdeffbd.toInt()
        animalCrossingPaletteColors[0xb1.toByte()] = 0xffceff8c.toInt()
        animalCrossingPaletteColors[0xb2.toByte()] = 0xff8caa52.toInt()
        animalCrossingPaletteColors[0xb3.toByte()] = 0xffaddf8c.toInt()
        animalCrossingPaletteColors[0xb4.toByte()] = 0xff8cff00.toInt()
        animalCrossingPaletteColors[0xb5.toByte()] = 0xffadba9c.toInt()
        animalCrossingPaletteColors[0xb6.toByte()] = 0xff63ba00.toInt()
        animalCrossingPaletteColors[0xb7.toByte()] = 0xff529a00.toInt()
        animalCrossingPaletteColors[0xb8.toByte()] = 0xff316500.toInt()

        animalCrossingPaletteColors[0xc0.toByte()] = 0xffbddfff.toInt()
        animalCrossingPaletteColors[0xc1.toByte()] = 0xff73cfff.toInt()
        animalCrossingPaletteColors[0xc2.toByte()] = 0xff31559c.toInt()
        animalCrossingPaletteColors[0xc3.toByte()] = 0xff639aff.toInt()
        animalCrossingPaletteColors[0xc4.toByte()] = 0xff1075ff.toInt()
        animalCrossingPaletteColors[0xc5.toByte()] = 0xff4275ad.toInt()
        animalCrossingPaletteColors[0xc6.toByte()] = 0xff214573.toInt()
        animalCrossingPaletteColors[0xc7.toByte()] = 0xff002073.toInt()
        animalCrossingPaletteColors[0xc8.toByte()] = 0xff001042.toInt()

        animalCrossingPaletteColors[0xd0.toByte()] = 0xffadffff.toInt()
        animalCrossingPaletteColors[0xd1.toByte()] = 0xff52ffff.toInt()
        animalCrossingPaletteColors[0xd2.toByte()] = 0xff008abd.toInt()
        animalCrossingPaletteColors[0xd3.toByte()] = 0xff52bace.toInt()
        animalCrossingPaletteColors[0xd4.toByte()] = 0xff00cfff.toInt()
        animalCrossingPaletteColors[0xd5.toByte()] = 0xff429aad.toInt()
        animalCrossingPaletteColors[0xd6.toByte()] = 0xff00658c.toInt()
        animalCrossingPaletteColors[0xd7.toByte()] = 0xff004552.toInt()
        animalCrossingPaletteColors[0xd8.toByte()] = 0xff002031.toInt()

        animalCrossingPaletteColors[0xe0.toByte()] = 0xffceffef.toInt()
        animalCrossingPaletteColors[0xe1.toByte()] = 0xffadefde.toInt()
        animalCrossingPaletteColors[0xe2.toByte()] = 0xff31cfad.toInt()
        animalCrossingPaletteColors[0xe3.toByte()] = 0xff52efbd.toInt()
        animalCrossingPaletteColors[0xe4.toByte()] = 0xff00ffce.toInt()
        animalCrossingPaletteColors[0xe5.toByte()] = 0xff73aaad.toInt()
        animalCrossingPaletteColors[0xe6.toByte()] = 0xff00aa9c.toInt()
        animalCrossingPaletteColors[0xe7.toByte()] = 0xff008a73.toInt()
        animalCrossingPaletteColors[0xe8.toByte()] = 0xff004531.toInt()

        animalCrossingPaletteColors[0xf0.toByte()] = 0xffadffad.toInt()
        animalCrossingPaletteColors[0xf1.toByte()] = 0xff73ff73.toInt()
        animalCrossingPaletteColors[0xf2.toByte()] = 0xff63df42.toInt()
        animalCrossingPaletteColors[0xf3.toByte()] = 0xff00ff00.toInt()
        animalCrossingPaletteColors[0xf4.toByte()] = 0xff21df21.toInt()
        animalCrossingPaletteColors[0xf5.toByte()] = 0xff52ba52.toInt()
        animalCrossingPaletteColors[0xf6.toByte()] = 0xff00ba00.toInt()
        animalCrossingPaletteColors[0xf7.toByte()] = 0xff008a00.toInt()
        animalCrossingPaletteColors[0xf8.toByte()] = 0xff214521.toInt()

        animalCrossingPaletteColors[0x0f.toByte()] = 0xffffffff.toInt()
        animalCrossingPaletteColors[0x1f.toByte()] = 0xffefefef.toInt()
        animalCrossingPaletteColors[0x2f.toByte()] = 0xffdedfde.toInt()
        animalCrossingPaletteColors[0x3f.toByte()] = 0xffcecfce.toInt()
        animalCrossingPaletteColors[0x4f.toByte()] = 0xffbdbabd.toInt()
        animalCrossingPaletteColors[0x5f.toByte()] = 0xffadaaad.toInt()
        animalCrossingPaletteColors[0x6f.toByte()] = 0xff9c9a9c.toInt()
        animalCrossingPaletteColors[0x7f.toByte()] = 0xff8c8a8c.toInt()
        animalCrossingPaletteColors[0x8f.toByte()] = 0xff737573.toInt()
        animalCrossingPaletteColors[0x9f.toByte()] = 0xff636563.toInt()
        animalCrossingPaletteColors[0xaf.toByte()] = 0xff525552.toInt()
        animalCrossingPaletteColors[0xbf.toByte()] = 0xff424542.toInt()
        animalCrossingPaletteColors[0xcf.toByte()] = 0xff313031.toInt()
        animalCrossingPaletteColors[0xdf.toByte()] = 0xff212021.toInt()
        animalCrossingPaletteColors[0xef.toByte()] = 0xff000000.toInt()


        createViewModel =
                ViewModelProvider(this).get(CreateViewModel::class.java)

        root = inflater.inflate(R.layout.fragment_create, container, false)
        val textViewCreate: TextView = root.findViewById(R.id.text_create)
        createViewModel.text.observe(viewLifecycleOwner, Observer {
            textViewCreate.text = it
        })

        // Connect the row/column chooser
        textViewRowCol = root.findViewById(R.id.textViewCols)

        // Create onClickListener for button
        val minusColsButton: ImageButton = root.findViewById(R.id.minusColsButton)
        minusColsButton.setOnClickListener {
            val amtCols = textViewRowCol.text.toString().toInt()
            if (amtCols > 1){
                textViewRowCol.text = (amtCols - 1).toString()
            }
            update_rows_columns()
        }
        // Create onClickListener for button
        val plusColsButton: ImageButton = root.findViewById(R.id.plusColsButton)
        plusColsButton.setOnClickListener {
            val amtCols = textViewRowCol.text.toString().toInt()
            if (amtCols < 10){
                textViewRowCol.text = (amtCols + 1).toString()
            }
            update_rows_columns()
        }

        // Create onClickListener for button
        val pickImageButton: Button = root.findViewById(R.id.pickImageButton)
        pickImageButton.setOnClickListener {

            // Pick image
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        // Create onClickListener for button
        val splitImageButton: Button = root.findViewById(R.id.splitImageButton)
        splitImageButton.setOnClickListener {

            // Pick image
            //val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            //startActivityForResult(gallery, PICK_IMAGE)

            Toast.makeText(activity, "Splitting image!", Toast.LENGTH_SHORT).show()

            // Split image up
            splitImage()
        }

        // Connect the imageview
        imageView = root.findViewById(R.id.imageView)

        // Connect the crop_switch
        crop_switch = root.findViewById(R.id.crop_switch)

        // Create adapter for
        // val split_images: MutableList<Bitmap> = arrayListOf()
        // mutableListOf("one", "two", "three", "four")
        split_images_hashmap=ArrayList<HashMap<String, Any>>()

        var animalNames = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
        animalNames = arrayOf("1", "1", "1", "1", "1", "1")

        val animalImages = listOf(R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster)

        for(i in animalNames.indices){
            val map=HashMap<String, Any>()

            // Data entry in HashMap
            map["name"] = animalNames[i]
            map["image"]=animalImages[i]

            // adding the HashMap to the ArrayList
            //split_images_hashmap.add(map)
        }

        recycleview = root.findViewById(R.id.recyclerView)
        // Creates a vertical Layout Manager
        //recycleview.layoutManager = LinearLayoutManager(root.context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        val gridlayoutmanger = GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())
        recycleview.layoutManager = gridlayoutmanger//GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())

        // Access the RecyclerView Adapter and load the data into it
        customadapter = CustomAdapter(split_images_hashmap)
        recycleview.adapter = customadapter


        imageView.setOnClickListener {
            println("buttonpressstart")
            addpicstest()
            println("buttonpressend")

            // read qr code
            // Testing google ml kit qr code
            val image = InputImage.fromBitmap(imageView.drawable.toBitmap(), 0)
            val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_QR_CODE,
                            Barcode.FORMAT_AZTEC)
                    .build()
            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        // Task completed successfully
                        // ...
                        println("nr of barcodes: ")
                        println(barcodes.size)
                        try {
                            for (barcode in barcodes) {
                                val bounds = barcode.boundingBox
                                val corners = barcode.cornerPoints

                                val rawValue = barcode.rawValue

                                textViewCreate.text = barcode.rawBytes.size.toString()
                                val qrData: ByteArray = barcode.rawBytes
                                val title: ByteArray = qrData.sliceArray(0..41)
                                val author: ByteArray = qrData.sliceArray(44..61)
                                val town: ByteArray = qrData.sliceArray(66..83)
                                val colorPalette: ByteArray = qrData.sliceArray(88..102)
                                val data: ByteArray = qrData.sliceArray(108 until qrData.size)
                                textViewCreate.text = data.size.toString()
                                val titletest: String = title.joinToString(separator = "", transform = { it.toChar().toString() })
                                val authortest: String = author.joinToString(separator = "", transform = { it.toChar().toString() })
                                val towntest: String = town.joinToString(separator = "", transform = { it.toChar().toString() })
                                val colortest: String = colorPalette.joinToString(separator = "", transform = { it.toChar().toString() })

                                var colorarray = ""
                                for (b in colorPalette) {
                                    val st = String.format("%02X", b)
                                    colorarray += " $st"
                                    println(st)
                                }

                                var dataarray = ""
                                for (b in data) {
                                    val st = String.format("%02X", b)
                                    dataarray += " $st"
                                    println(st)
                                }

                                val pixels = getPixelColorsFromBytes(data)
                                val qrDecodedBmp = createBitmapFromQR(pixels, colorPalette)


                                val valueType = barcode.valueType



                                // See API reference for complete list of supported types
                                when (valueType) {
                                    Barcode.TYPE_WIFI -> {
                                        val ssid = barcode.wifi!!.ssid
                                        val password = barcode.wifi!!.password
                                        val type = barcode.wifi!!.encryptionType
                                    }
                                    Barcode.TYPE_URL -> {
                                        val title = barcode.url!!.title
                                        val url = barcode.url!!.url
                                    }
                                }
                            }

                        }catch (e: Exception){
                            Toast.makeText(activity, "Could not read animal crossing qr code", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                    }

            // rescale image
            val tempimg = imageView.drawable.toBitmap()
            val tempbmp = tempimg.scale(32, 32)
            imageView.setImageBitmap(tempbmp)

            // change color palette
            tempbmp

        }



        // Testing QR code stuff ZXing
        val height: Int = imageView.drawable.toBitmap().height//bitMatrix.getHeight()
        val width: Int = imageView.drawable.toBitmap().width//bitMatrix.getWidth()
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (imageView.drawable.toBitmap().get(x, y) > 100) Color.BLACK else Color.WHITE)//.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        imageView.setImageBitmap(bmp)

        // Tesitng ZXing qr again
        val bitmap = generateQRCode("Sample Text")
        imageView.setImageBitmap(bitmap)

        return root
    }

    fun createBitmapFromQR(pixels: ArrayList<Int>, colorPalette: ByteArray): Bitmap {
        val height: Int = 32
        val width: Int = 32
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        var counter = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                var color = animalCrossingPaletteColors[colorPalette[pixels[counter]]]
                if (color != null) {
                    //color = "ff" + color.slice(1 until color.length)
                    //color = color.toUpperCase()
                    //val colorInt = parseLong(color, 16)
                    Color.WHITE
                    bmp.setPixel(x, y, color)//.get(x, y)) Color.BLACK else Color.WHITE)
                }

                counter++
            }
        }
        //imageView.setImageBitmap(bmp)
        return bmp
    }
    fun getPixelColorsFromBytes(bytes: ByteArray): ArrayList<Int> {
        val arrayListOfPixels = ArrayList<Int>()

        for (byte in bytes) {
            //val byte = "01101001".toInt(2).toByte()
            val first_half = (byte.toInt() shr 4) and "00001111".toInt(2)
            val second_half = byte.toInt() and "00001111".toInt(2)
            val second_half2 = (byte.toInt() shr 4)

            val byteasstring = byte.toString(2).padStart(8, '0')
            val firsthalfbytestring = first_half.toString(2).padStart(8, '0')
            val secondhalfbytestring = second_half.toString(2).padStart(8, '0')
            println()
            arrayListOfPixels.add(first_half)
            arrayListOfPixels.add(second_half)

        }
        println()
        return arrayListOfPixels
    }


    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

    private fun generateQRCode(image: Bitmap): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode("image", BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

    private fun update_rows_columns(){
        val amtCols = textViewRowCol.text.toString().toInt()
        val gridlayoutmanger = GridLayoutManager(root.context, amtCols)
        recycleview.layoutManager = gridlayoutmanger//GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())
        splitImage()
    }

    private fun addpicstest() {
        //val animalNames = arrayOf("Lion","Tiger","Monkey","Dog","Cat","Elephant")
        val animalImages = listOf(R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster)

        for(i in animalImages.indices){
            val map=HashMap<String, Any>()

            // Data entry in HashMap
            map["name"] = ""
            map["image"]=animalImages[i]

            // adding the HashMap to the ArrayList
            split_images_hashmap.add(map)
        }
        customadapter.notifyDataSetChanged()
    }

    private fun performCrop(picUri: Uri) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties here
            cropIntent.putExtra("crop", true)
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 350)
            cropIntent.putExtra("outputY", 350)
            // ME TESTING NB!!!!!!!!!!!!!!!
            cropIntent.putExtra("scale", true)
            cropIntent.putExtra("scaleUpIfNeeded", true)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_IMAGE)//PIC_CROP)
        } // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            // display an error message
            val errorMessage = "Whoops - your device doesn't support the crop action!"
            ///val toast: Toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
            ///toast.show()
        }
    }

    private fun splitImage() {

        // Empty images already loaded
        split_images_hashmap.clear()
        val split_amount: Int = textViewRowCol.text.toString().toInt()
        // Getting the scaled bitmap of the source image
        val bitmap = imageView.drawable.toBitmap()
        println(bitmap.height)


        val x_chuck_size: Int = bitmap.width/split_amount
        val y_chuck_size: Int = bitmap.height/split_amount

        val split_images: MutableList<Bitmap> = arrayListOf()

        for(y in 0 until split_amount){
            for(x in 0 until split_amount){
                split_images.add(Bitmap.createBitmap(bitmap, x * x_chuck_size, y * y_chuck_size, x_chuck_size, y_chuck_size))
            }
        }

        //imageView.setImageBitmap(split_images.get(0))
        for(i in split_images.indices){
            val map=HashMap<String, Any>()

            // Data entry in HashMap
            map["name"] = "testName"
            map["image"]=split_images[i]

            // adding the HashMap to the ArrayList
            split_images_hashmap.add(map)
        }
        customadapter.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE) {
                println(data)
                imageUri = data?.data

                imageView.setImageURI(imageUri)
                println("BEORFEFJDSOFIDJSOAIFDJSOIJFOIS")
                println(data)
                println(data?.extras)
                println(imageUri)
                print(data?.data.toString())
                println("AFTEREKARFJDSJSAFKLDSAKFDJ")

                // Crop image
                if(crop_switch.isChecked){
                    imageUri?.let { performCrop(it) }
                    Toast.makeText(activity, "Cropping!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "Not cropping!", Toast.LENGTH_SHORT).show()
                }

            }
            else if(requestCode == CROP_IMAGE) {
                println("activity result crop image")
                println(data)
                println(data?.data)
                println(data?.extras)
                println(data?.extras.toString())
                println(data?.extras)

                val bundle: Bundle? = data?.extras
                println("Categories!!!!!!!!!!1")
                println(data)

                val bitmap: Bitmap? = data?.getParcelableExtra<Bitmap>("data")

                //////////////////////////
                imageView.setImageBitmap(bitmap)

                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                    }
                }




            }
        }
    }
}