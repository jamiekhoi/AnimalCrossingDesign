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
import com.example.animalcrossingdesign.AnimalCrossingQRObject
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.pow


class CreateFragment : Fragment() {
    private lateinit var customadapter: CustomAdapter
    private lateinit var createViewModel: CreateViewModel

    lateinit var imageView: ImageView
    private lateinit var crop_switch: Switch
    private lateinit var split_images_hashmap: ArrayList<HashMap<String, Any>>
    private lateinit var textViewRowCol: TextView
    private lateinit var recycleview: RecyclerView
    //Todo: Is it bad to put root here instead of inside constructor?
    private lateinit var root: View

    private val PICK_IMAGE = 100
    private val CROP_IMAGE = 101
    private val THE_WHOLE_ENCHILADA_PICK_IMAGE = 411
    private val THE_WHOLE_ENCHILADA_CROP_IMAGE = 412

    private var imageUri: Uri? = null

    // Todo: remove other instances of hardcoded 32
    private val animalCrossingDesignWidth = 32
    private val animalCrossingDesignHeight = 32

    private lateinit var convertedImageColorPalettePositions: List<Byte> // as 1 Byte location based on xml file
    private lateinit var convertedImagePixels: IntArray

    private lateinit var var1: ByteArray
    private lateinit var var2: ByteArray

    private val db = Firebase.firestore

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
            imageUri?.let { it1 -> performCrop(it1, CROP_IMAGE) }
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
            //imageView.setImageBitmap(convertedbmp)
        }

        // Create onClickListener for button to create QR code
        val createQRButton: Button = root.findViewById(R.id.createQRButton)
        createQRButton.setOnClickListener {
            /*
            Create qr code for image in imageview
             */
            val QRObject = AnimalCrossingQRObject(imageView.drawable.toBitmap())
            val QRCodeBitmap = QRObject.toQRBitmap()
            imageView.setImageBitmap(QRCodeBitmap)

            saveToFirebaseFireStore(QRObject)

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

            // Quick Crop
            val oldImage = imageView.drawable.toBitmap()
            val newSize = if (oldImage.width < oldImage.height) oldImage.width else oldImage.height
            val newbitmap = Bitmap.createBitmap(oldImage,
                0, 0, newSize, newSize)

            // rescale image
            val tempimg = imageView.drawable.toBitmap()
            val tempbmp = tempimg.scale(animalCrossingDesignWidth, animalCrossingDesignHeight)
            imageView.setImageBitmap(tempbmp)

            // change color palette
            //val convertedbmp = convertBitmapToFitACPalette(tempbmp)
            //imageView.setImageBitmap(convertedbmp)
        }

        // Create onClickListener
        val theWholeEnchilada: Button = root.findViewById(R.id.theWholeEnchilada)
        theWholeEnchilada.setOnClickListener {
            /*
            Do everything
             */
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, THE_WHOLE_ENCHILADA_PICK_IMAGE)

        }

        return root
    }

    private fun QRCodeCloseup() {
        val image = InputImage.fromBitmap(imageView.drawable.toBitmap(), 0)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)
        val result = scanner.process(image) // TODO: debugging not going inside onSuccessListener. Why?
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                // ...
                println("nr of barcodes: ")
                println(barcodes.size)

                var1 = barcodes[0].rawBytes!!

                for (barcode in barcodes) {
                    barcode
                    //val newBitmap = Bitmap.createBitmap(animalCrossingDesignWidth, animalCrossingDesignHeight, Bitmap.Config.ARGB_8888)
                    //newBitmap.setPixels(QRObject.imagePixels, 0, animalCrossingDesignWidth, 0,0, animalCrossingDesignWidth, animalCrossingDesignHeight)
                    val box = barcode.getBoundingBox()
                    val newbitmap = Bitmap.createBitmap(imageView.drawable.toBitmap(),
                        box.left-0, box.top-0, box.width(), box.height())
                    imageView.setImageBitmap(newbitmap)

                    barcode
                }
                // END TESTING
            }
    }

    private fun saveToFirebaseFireStore(qrObject: AnimalCrossingQRObject) {
        // Add a new document with a generated ID
        // TODO: Fix authentication so only logged in users can read/write
        //db.document("users/" + FirebaseAuth.getInstance().currentUser.uid).collection("users")
        db.collection("designObjects")
            .add(qrObject)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
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

    private fun getClosestColor(color: Color, method: (Color, Color) -> Double): Pair<Color, HashMap<Color, Double>> {
        val acColors = AnimalCrossingQRObject.animalCrossingPalettePositionToColorMap.values

        var minDistance: Double = Double.MAX_VALUE
        var minDistanceColor: Color = color

        val distanceList = HashMap<Color, Double>()

        for (colorCode in acColors) {
            val acColor = colorCode.toInt().toColor()
            //val distance = getContrastRatio(color, acColor)
            //val distance = getEuclideanSRGBDistance(color, acColor)
            val distance = method(color, acColor)

            distanceList[acColor] = distance
            // What should happen if same distance?
            if (distance < minDistance) {
                minDistance = distance
                minDistanceColor = acColor
            }
        }
        return Pair(minDistanceColor, distanceList)
    }

    private fun convertBitmapToFitACPalette(bitmap: Bitmap, method: String = "rgb"): Bitmap{

        val arrayListOfImageColors = IntArray(bitmap.width*bitmap.height)
        bitmap.getPixels(arrayListOfImageColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Todo
        // Add up total distances PER color in AC color palette and choose the 15 highest?/lowest?
        // <Color of pixel in image, <Color from AC palette, distance between them>>
        val colorDistanceMapOfMap = HashMap<Color, HashMap<Color, Double>>()

        // make array of already calculated distances for ACcolors and check against to reduce calculations
        val colorDistanceMap =  listOf<Color>()

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width){
                val pixel = bitmap.getPixel(x, y)
                val pixelColor = pixel.toColor()
                //arrayListOfImageColors.add(pixelColor)

                if (colorDistanceMap.contains(pixelColor)) {
                    continue
                }
                when (method) {
                    "rgb" -> {
                        val closestColorInfo = getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                        val distanceListForThisPixel = closestColorInfo.second
                        colorDistanceMapOfMap[pixelColor] = distanceListForThisPixel
                    }
                    /*"contrast" -> {
                        val closestColorInfo = getClosestColor(pixelColor, ::getContrastRatio)
                        closestColor = closestColorInfo.first
                        val distanceListForThisPixel = closestColorInfo.second
                    }
                    else -> {
                        Toast.makeText(activity, "Color distance method not found. Using Euclidean sRGB Distance", Toast.LENGTH_LONG).show()
                        val closestColorInfo = getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                        closestColor = closestColorInfo.first
                        val distanceListForThisPixel = closestColorInfo.second
                    }*/
                }
            }
        }

        // Is there a more effective way?
        val sumOfDifferencesForACColorPalette = HashMap<Color, Double>() // (Color in ACPalette, SumOfDifferences)
        for (distanceMap in colorDistanceMapOfMap.values) {
            for ((colorKey, distanceValue) in distanceMap) {
                sumOfDifferencesForACColorPalette[colorKey] = sumOfDifferencesForACColorPalette.getOrDefault(colorKey, 0.0) + distanceValue
            }
        }
        val orderedSumOfDifferencesACPalette = sumOfDifferencesForACColorPalette.entries.sortedWith(compareBy { it.value })
        val topFifteenColors = orderedSumOfDifferencesACPalette.slice(0..14)
        // TODO: turn topFifteenColors into list instead of list of map here
        convertedImageColorPalettePositions = topFifteenColors.map { AnimalCrossingQRObject.animalCrossingPaletteColorToPositionMap[it.key.toArgb()]!! }.toList()

        // Using only 15 colors from AC palette
        val finalizedColorConversionMap =  HashMap<Color, Color>()
        for ((pixelColor, distanceMap) in colorDistanceMapOfMap) {
            val closestColorWithin15ColorPalette = distanceMap.filterKeys { it -> it in topFifteenColors.map { it.key } }.entries.sortedWith(compareBy { it.value })[0].key
            finalizedColorConversionMap[pixelColor] = closestColorWithin15ColorPalette
        }

        val recoloredImagePixels: IntArray = arrayListOfImageColors.map { finalizedColorConversionMap[it.toColor()]!!.toArgb() }.toList().toIntArray()

        convertedImagePixels = recoloredImagePixels

        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        newBitmap.setPixels(recoloredImagePixels, 0, bitmap.width, 0,0, bitmap.width, bitmap.height)

        return newBitmap
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

    private fun performCrop(picUri: Uri, requestCode: Int) {
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
            startActivityForResult(cropIntent, requestCode)//PIC_CROP)
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
                    imageUri?.let { performCrop(it, CROP_IMAGE) }
                    Toast.makeText(activity, "Cropping!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "Not cropping!", Toast.LENGTH_SHORT).show()
                }

            }
            else if(requestCode == THE_WHOLE_ENCHILADA_PICK_IMAGE){
                imageUri = data?.data
                imageUri?.let { performCrop(it, THE_WHOLE_ENCHILADA_CROP_IMAGE) }

            }
            else if(requestCode == THE_WHOLE_ENCHILADA_CROP_IMAGE){
                val bundle: Bundle? = data?.extras
                val bitmap: Bitmap? = data?.getParcelableExtra<Bitmap>("data")

                val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!,
                    animalCrossingDesignWidth,
                    animalCrossingDesignHeight,
                    true)

                val convertedbmp = convertBitmapToFitACPalette(scaledBitmap, "rgb")

                val QRObject = AnimalCrossingQRObject(convertedbmp)
                val QRCodeBitmap = QRObject.toQRBitmap()

                imageView.setImageBitmap(QRCodeBitmap)

                saveToFirebaseFireStore(QRObject)

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