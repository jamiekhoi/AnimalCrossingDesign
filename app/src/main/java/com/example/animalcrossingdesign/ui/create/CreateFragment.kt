package com.example.animalcrossingdesign.ui.create

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.example.animalcrossingdesign.databinding.FragmentCreateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth


class CreateFragment : Fragment() {
    private lateinit var customadapter: CustomAdapter
    private lateinit var createViewModel: CreateViewModel
    private lateinit var fragmentCreateBinding: FragmentCreateBinding

    lateinit var imageView: ImageView
    private lateinit var crop_switch: Switch
    private lateinit var adapter_arraylist: ArrayList<Bitmap>
    private lateinit var textViewRowCol: TextView
    private lateinit var recycleview: RecyclerView
    //Todo: Is it bad to put root here instead of inside constructor?
    //private lateinit var root: View

    private val PICK_THEN_CROP_IMAGE = 100
    private val CROP_IMAGE = 101
    private val THE_WHOLE_ENCHILADA_PICK_IMAGE = 411
    private val THE_WHOLE_ENCHILADA_CROP_IMAGE = 412

    private var imageUri: Uri? = null

    // Todo: remove other instances of hardcoded 32
    private val animalCrossingDesignWidth = 32
    private val animalCrossingDesignHeight = 32

    //private lateinit var convertedImageColorPalettePositions: List<Byte> // as 1 Byte location based on xml file
    //private lateinit var convertedImagePixels: IntArray

    private lateinit var var1: ByteArray
    private lateinit var var2: ByteArray

    private val db = Firebase.firestore

    @ExperimentalStdlibApi
    private var paletteSelectionMethod: (Bitmap) -> Bitmap = ::convertBitmapMedianCut

    @ExperimentalStdlibApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        createViewModel =
                ViewModelProvider(this).get(CreateViewModel::class.java)

        fragmentCreateBinding = FragmentCreateBinding.inflate(inflater, container, false)
        //root = inflater.inflate(R.layout.fragment_create, container, false)

        val textViewCreate: TextView = fragmentCreateBinding.textCreate
        createViewModel.text.observe(viewLifecycleOwner, Observer {
            textViewCreate.text = it
        })

        // Connect the row/column chooser
        textViewRowCol = fragmentCreateBinding.textViewCols
        // Create onClickListener for button
        val minusColsButton: ImageButton = fragmentCreateBinding.minusColsButton
        minusColsButton.setOnClickListener {
            val amtCols = textViewRowCol.text.toString().toInt()
            if (amtCols > 1){
                textViewRowCol.text = (amtCols - 1).toString()
            }
            update_rows_columns()
        }
        // Create onClickListener for button
        val plusColsButton: ImageButton = fragmentCreateBinding.plusColsButton
        plusColsButton.setOnClickListener {
            val amtCols = textViewRowCol.text.toString().toInt()
            if (amtCols < 10){
                textViewRowCol.text = (amtCols + 1).toString()
            }
            update_rows_columns()
        }

        // Create onClickListener for button to choose image
        val pickImageAndCropButton: Button = fragmentCreateBinding.pickImageAndCropButton
        pickImageAndCropButton.setOnClickListener {
            // Pick image
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_THEN_CROP_IMAGE)
        }

        // Create onClickListener for button to splitimage
        val splitImageButton: Button = fragmentCreateBinding.splitImageButton
        splitImageButton.setOnClickListener {
            Toast.makeText(activity, "Splitting image!", Toast.LENGTH_SHORT).show()
            // Split image up
            splitImage()
        }


        // Create onClickListener for button to convert to animal crossing colors
        val changeColorButton: Button = fragmentCreateBinding.changeColorButton
        changeColorButton.setOnClickListener {
            // Change colors to animal crossing colors
            //val convertedbmp = convertBitmapToFitACPalette(imageView.drawable.toBitmap(), "rgb")
            //val convertedbmp = convertBitmapMedianCut(imageView.drawable.toBitmap())
            adapter_arraylist.clear()
            scaleBitmap_convertBitmap_createQR_saveFireStore(imageView.drawable.toBitmap())
        }


        // Connect the imageview
        imageView = fragmentCreateBinding.createMainImageView

        // Create adapter for
        // val split_images: MutableList<Bitmap> = arrayListOf()
        // mutableListOf("one", "two", "three", "four")
        adapter_arraylist = ArrayList<Bitmap>()


        recycleview = fragmentCreateBinding.recyclerView
        // Creates a vertical Layout Manager
        //recycleview.layoutManager = LinearLayoutManager(root.context)
        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        val gridlayoutmanger = GridLayoutManager(fragmentCreateBinding.root.context, textViewRowCol.text.toString().toInt())
        recycleview.layoutManager = gridlayoutmanger//GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())

        // Access the RecyclerView Adapter and load the data into it
        customadapter = CustomAdapter(adapter_arraylist)
        recycleview.adapter = customadapter


        imageView.setOnClickListener {
            println("buttonpressstart")
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
        val theWholeEnchilada: Button = fragmentCreateBinding.theWholeEnchilada
        theWholeEnchilada.setOnClickListener {
            /*
            Do everything
             */
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, THE_WHOLE_ENCHILADA_PICK_IMAGE)

        }

        // Initializing a String Array
        val spinnerArray = arrayListOf("Old method", "Median Cut")
        val spinnerArrayMethods = arrayListOf(::convertBitmapToFitACPalette, ::convertBitmapMedianCut)
        // Initializing an ArrayAdapter
        val spinnerArrayAdapter = ArrayAdapter(fragmentCreateBinding.root.context, android.R.layout.simple_spinner_item, spinnerArray)
        // Set the drop down view resource
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Connect spinner
        val paletteSelectionMethodSpinner = fragmentCreateBinding.paletteSelectionMethodSpinner
        // Finally, data bind the spinner object with dapter
        paletteSelectionMethodSpinner.adapter = spinnerArrayAdapter
        // Set an on item selected listener for spinner object
        paletteSelectionMethodSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                textViewCreate.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                paletteSelectionMethod = spinnerArrayMethods[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        return fragmentCreateBinding.root
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
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            db.collection("users").document(user.email)
                .collection("designs").add(qrObject)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                    db.collection("designs").document(documentReference.id)
                        .set(qrObject)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            // TODO: add field to this doc with user id for design
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }



        } else {
                    // No user is signed in
                    print("")
                }
    }

    private fun update_rows_columns(){
        val amtCols = textViewRowCol.text.toString().toInt()
        val gridlayoutmanger = GridLayoutManager(fragmentCreateBinding.root.context, amtCols)
        recycleview.layoutManager = gridlayoutmanger//GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())
        //splitImage()
    }

    private fun add_design_and_qr_to_gridview(design_bitmap: Bitmap, qr_bitmap: Bitmap){
        val gridlayoutmanger = GridLayoutManager(fragmentCreateBinding.root.context, 2)
        recycleview.layoutManager = gridlayoutmanger
        // Empty images already loaded
        //adapter_arraylist.clear()

        adapter_arraylist.add(design_bitmap)
        adapter_arraylist.add(qr_bitmap)

        customadapter.notifyDataSetChanged()
    }

    private fun addpicstest() {
        //val animalNames = arrayOf("Lion","Tiger","Monkey","Dog","Cat","Elephant")
        val animalImages = listOf(R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster)

        //adapter_arraylist.addAll(animalImages)
        //customadapter.notifyDataSetChanged()
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

    @ExperimentalStdlibApi
    private fun splitImage() {

        // Empty images already loaded
        adapter_arraylist.clear()
        val split_amount: Int = textViewRowCol.text.toString().toInt()
        // Getting the scaled bitmap of the source image
        val bitmap = imageView.drawable.toBitmap()
        println(bitmap.height)


        val x_chuck_size: Int = bitmap.width/split_amount
        val y_chuck_size: Int = bitmap.height/split_amount

        val split_images: MutableList<Bitmap> = arrayListOf()

        for(y in 0 until split_amount){
            for(x in 0 until split_amount){
                //split_images.add(Bitmap.createBitmap(bitmap, x * x_chuck_size, y * y_chuck_size, x_chuck_size, y_chuck_size))
                scaleBitmap_convertBitmap_createQR_saveFireStore(Bitmap.createBitmap(bitmap, x * x_chuck_size, y * y_chuck_size, x_chuck_size, y_chuck_size))
            }
        }

        //imageView.setImageBitmap(split_images.get(0))


        //adapter_arraylist.addAll(split_images)
        //customadapter.notifyDataSetChanged()

    }

    @ExperimentalStdlibApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_THEN_CROP_IMAGE) {
                println(data)
                imageUri = data?.data

                // TODO: CROP using intent if the phone has it, if not crop to largest square
                // Crop image
                imageView.setImageURI(imageUri) // Set this in case crop intent fails
                performCrop(imageUri!!, CROP_IMAGE)
                //Toast.makeText(activity, "Cropping!", Toast.LENGTH_SHORT).show()
                //Toast.makeText(activity, "Not cropping!", Toast.LENGTH_SHORT).show()

            }
            else if(requestCode == THE_WHOLE_ENCHILADA_PICK_IMAGE){

                imageUri = data?.data

                // TODO: CROP using intent if the phone has it, if not crop to largest square
                imageView.setImageURI(imageUri) // Set this in case crop intent fails
                imageUri?.let { performCrop(it, THE_WHOLE_ENCHILADA_CROP_IMAGE) }

            }
            else if(requestCode == THE_WHOLE_ENCHILADA_CROP_IMAGE){
                val bundle: Bundle? = data?.extras
                val bitmap: Bitmap? = data?.getParcelableExtra<Bitmap>("data")

                adapter_arraylist.clear()
                scaleBitmap_convertBitmap_createQR_saveFireStore(bitmap!!)

            }
            else if(requestCode == CROP_IMAGE) {
                val bundle: Bundle? = data?.extras
                val bitmap: Bitmap? = data?.getParcelableExtra<Bitmap>("data")

                imageView.setImageBitmap(bitmap)

                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                    }
                }




            }
        }
        else{
            println(resultCode)
            if(requestCode == CROP_IMAGE) {
                val bitmap = imageView.drawable.toBitmap()
                imageViewBitmapAutoCrop(bitmap)
            }
            else if(requestCode == THE_WHOLE_ENCHILADA_CROP_IMAGE){
                val bitmap = imageView.drawable.toBitmap()
                val croppedBitmap = imageViewBitmapAutoCrop(bitmap)

                adapter_arraylist.clear()
                scaleBitmap_convertBitmap_createQR_saveFireStore(croppedBitmap)

            }
        }
    }

    private fun imageViewBitmapAutoCrop(bitmap: Bitmap): Bitmap {
        val minLength = if (bitmap.width < bitmap.height) bitmap.width else bitmap.height
        val croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, minLength, minLength)
        imageView.setImageBitmap(croppedBitmap)
        return croppedBitmap
    }

    @ExperimentalStdlibApi
    private fun scaleBitmap_convertBitmap_createQR_saveFireStore(croppedBitmap: Bitmap) {
        val scaledBitmap = Bitmap.createScaledBitmap(
            croppedBitmap,
            animalCrossingDesignWidth,
            animalCrossingDesignHeight,
            true
        )

        //val convertedbmp = convertBitmapToFitACPalette(scaledBitmap, "rgb")
        val convertedbmp = paletteSelectionMethod(scaledBitmap)

        val QRObject = AnimalCrossingQRObject(convertedbmp)
        val QRCodeBitmap = QRObject.toQRBitmap()

        add_design_and_qr_to_gridview(convertedbmp, QRCodeBitmap)

        saveToFirebaseFireStore(QRObject)
    }
}