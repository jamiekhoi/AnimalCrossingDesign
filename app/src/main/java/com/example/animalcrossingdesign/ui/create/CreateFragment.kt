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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.animalcrossingdesign.R
import android.widget.SimpleAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateFragment : Fragment() {


    private lateinit var customadapter: CustomAdapter
    private lateinit var createViewModel: CreateViewModel

    lateinit var imageView: ImageView
    lateinit var button: Button
    private lateinit var crop_switch: Switch
    private lateinit var adapter: SimpleAdapter
    private lateinit var split_images_hashmap: ArrayList<HashMap<String,Any>>

    private val PICK_IMAGE = 100
    private val CROP_IMAGE = 101

    private var imageUri: Uri? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        createViewModel =
                ViewModelProvider(this).get(CreateViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_create, container, false)
        val textView: TextView = root.findViewById(R.id.text_create)
        createViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

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
        split_images_hashmap=ArrayList<HashMap<String,Any>>()

        var animalNames = arrayOf("Lion","Tiger","Monkey","Dog","Cat","Elephant")
        animalNames = arrayOf("1","1","1","1","1","1")

        val animalImages = listOf(R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster)

        for(i in animalNames.indices){
            val map=HashMap<String,Any>()

            // Data entry in HashMap
            map["name"] = animalNames[i]
            map["image"]=animalImages[i]

            // adding the HashMap to the ArrayList
            split_images_hashmap.add(map)
        }

        val from = arrayOf("name", "image")
        val to = intArrayOf(R.id.textViewGrid, R.id.imageViewGrid)
        adapter = SimpleAdapter(root.context, split_images_hashmap, R.layout.image_grid_view, from, to)

        val recycleview: RecyclerView = root.findViewById(R.id.recyclerView)

        // Creates a vertical Layout Manager
        //recycleview.layoutManager = LinearLayoutManager(root.context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        recycleview.layoutManager = GridLayoutManager(root.context, 3)

        // Access the RecyclerView Adapter and load the data into it
        customadapter = CustomAdapter(split_images_hashmap)
        recycleview.adapter = customadapter


        imageView.setOnClickListener {
            println("buttonpressstart")
            addpicstest()
            println("buttonpressend")

        }

        return root
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
            val map=HashMap<String,Any>()

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

    private fun splitImage(split_amount: Int = 3) {

        // Getting the scaled bitmap of the source image
        val bitmap = imageView.drawable.toBitmap()
        println(bitmap.height)


        val x_chuck_size: Int = bitmap.width/split_amount
        val y_chuck_size: Int = bitmap.height/split_amount

        val split_images: MutableList<Bitmap> = arrayListOf()

        for(y in 0 until split_amount){
            for(x in 0 until split_amount){
                split_images.add(Bitmap.createBitmap(bitmap, x*x_chuck_size, y*y_chuck_size, x_chuck_size, y_chuck_size))
            }
        }

        imageView.setImageBitmap(split_images.get(0))
        for(i in split_images.indices){
            val map=HashMap<String,Any>()

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