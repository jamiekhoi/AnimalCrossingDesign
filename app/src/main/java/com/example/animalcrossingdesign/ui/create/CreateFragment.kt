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
import androidx.core.graphics.toColor
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
import com.example.animalcrossingdesign.AnimalCrossingColors
import kotlin.math.pow


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

    // Todo: remove other instances of hardcoded 32
    private val animalCrossingDesignWidth = 32
    private val animalCrossingDesignHeight = 32

    private val animalCrossingPaletteColors = AnimalCrossingColors().animalCrossingPaletteColors


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

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

        // Create onClickListener for button to choose image
        val pickImageButton: Button = root.findViewById(R.id.pickImageButton)
        pickImageButton.setOnClickListener {
            // Pick image
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        // Create onClickListener for button to splitimage
        val splitImageButton: Button = root.findViewById(R.id.splitImageButton)
        splitImageButton.setOnClickListener {
            Toast.makeText(activity, "Splitting image!", Toast.LENGTH_SHORT).show()
            // Split image up
            splitImage()
        }

        // Create onClickListener for button to crop image
        val cropButton: Button = root.findViewById(R.id.cropButton)
        cropButton.setOnClickListener {
            // Crop image from imageview
            imageUri?.let { it1 -> performCrop(it1) }
        }

        // Create onClickListener for button to downscale image
        val downscaleButton: Button = root.findViewById(R.id.downscaleButton)
        downscaleButton.setOnClickListener {
            // Downscale image from imageview
            imageView.setImageBitmap(Bitmap.createScaledBitmap(imageView.drawable.toBitmap(),
                    animalCrossingDesignWidth,
                    animalCrossingDesignHeight,
                    true))
        }

        // Create onClickListener for button to convert to animal crossing colors
        val changeColorButton: Button = root.findViewById(R.id.changeColorButton)
        changeColorButton.setOnClickListener {
            // Change colors to animal crossing colors
            val convertedbmp = convertBitmapToFitACPalette(imageView.drawable.toBitmap(), "rgb")
            imageView.setImageBitmap(convertedbmp)
        }

        // Create onClickListener for button to convert to animal crossing colors
        val changeColorButton2: Button = root.findViewById(R.id.changeColorButton2)
        changeColorButton2.setOnClickListener {
            // Change colors to animal crossing colors
            val convertedbmp = convertBitmapToFitACPalette(imageView.drawable.toBitmap(), "contrast")
            imageView.setImageBitmap(convertedbmp)
        }

        // Connect the imageview
        imageView = root.findViewById(R.id.imageView)

        // Connect the crop_switch
        crop_switch = root.findViewById(R.id.crop_switch)

        // Create adapter for
        // val split_images: MutableList<Bitmap> = arrayListOf()
        // mutableListOf("one", "two", "three", "four")
        split_images_hashmap = ArrayList<HashMap<String, Any>>()


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
            val tempbmp = tempimg.scale(animalCrossingDesignWidth, animalCrossingDesignHeight)
            imageView.setImageBitmap(tempbmp)

            // change color palette
            val convertedbmp = convertBitmapToFitACPalette(tempbmp)
            imageView.setImageBitmap(convertedbmp)
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

    private fun getContrastRatio(color1: Color, color2: Color): Double {
        val relativeLuminance1 = (
                (0.2126 * color1.red()) +
                        (0.7152 * color1.green()) +
                        (0.0722 * color1.blue())
                )
        val relativeLuminance2 = (
                (0.2126 * color2.red()) + (0.7152 * color2.green()) + (0.0722 * color2.blue()))

        val contrastRatio: Double
        // Todo: check if this is correct
        contrastRatio = if (relativeLuminance1 > relativeLuminance2) {
            (relativeLuminance1 + 0.0005)/(relativeLuminance2 + 0.0005)
        }else {
            (relativeLuminance2 + 0.0005)/(relativeLuminance1 + 0.0005)
        }

        return contrastRatio
    }

    private fun getEuclideanSRGBDistance(color1: Color, color2: Color): Double {
        val redWeight = 0.3
        val greenWeight = 0.59
        val blueWeight = 0.11

        return (
                ((color2.red() - color1.red()) * redWeight).pow(2) +
                ((color2.green() - color1.green()) * greenWeight).pow(2) +
                ((color2.blue() - color1.blue()) * blueWeight).pow(2)).toDouble()
    }

    private fun getClosestColor(color: Color, method: (Color, Color) -> Double): Color {
        val acColors = animalCrossingPaletteColors.values

        var minDistance: Double = Double.MAX_VALUE
        var minDistanceColor: Color = color

        val distanceList = ArrayList<ArrayList<Any>>()

        for (colorCode in acColors) {
            val acColor = colorCode.toInt().toColor()
            //val distance = getContrastRatio(color, acColor)
            //val distance = getEuclideanSRGBDistance(color, acColor)
            val distance = method(color, acColor)

            distanceList.add(arrayListOf(acColor, distance))
            // What should happen if same distance?
            if (distance < minDistance) {
                minDistance = distance
                minDistanceColor = acColor
            }
        }
        return minDistanceColor
    }

    private fun convertBitmapToFitACPalette(bitmap: Bitmap, method: String = "rgb"): Bitmap{
        val recoloredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val arrayListOfImageColors = ArrayList<Color>()
        val arrayListOfConvertColors = ArrayList<Color>()

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width){
                val pixel = bitmap.getPixel(x, y)
                val pixelColor = pixel.toColor()
                val closestColor: Color = if (method == "rgb") {
                    getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                } else if (method == "contrast") {
                    getClosestColor(pixelColor, ::getContrastRatio)
                }else {
                    Toast.makeText(activity, "Color distance method not found. Using Euclidean sRGB Distance", Toast.LENGTH_LONG).show()
                    getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                }

                arrayListOfImageColors.add(pixelColor)
                arrayListOfConvertColors.add(closestColor)
                recoloredBitmap.setPixel(x, y, closestColor.toArgb())
            }
        }
        return recoloredBitmap
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