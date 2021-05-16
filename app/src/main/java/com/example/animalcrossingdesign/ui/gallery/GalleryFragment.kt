package com.example.animalcrossingdesign.ui.gallery

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GalleryFragment : Fragment() {
    companion object {
        private val TAG = GalleryFragment::class.qualifiedName
        private val animalCrossingDesignWidth = 32
        private val animalCrossingDesignHeight = 32
    }
    private lateinit var galleryViewModel: GalleryViewModel

    private val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        //galleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        //galleryRecyclerView.isNestedScrollingEnabled = false
        val galleryFireStoreDesignRecyclerView: RecyclerView = root.findViewById(R.id.galleryFireStoreDesignRecyclerView)

        // Creates a vertical GridLayoutManager
        val gridlayoutmanger = GridLayoutManager(root.context, 3)
        val gridfirestorelayoutmanger = GridLayoutManager(root.context, 3)
        galleryFireStoreDesignRecyclerView.layoutManager = gridfirestorelayoutmanger

        // Access the RecyclerView Adapter and load the data into it
        val designPreviews = arrayListOf<Bitmap>()
        val fireStoreDesignAdapter = FireStoreDesignAdapter(designPreviews)
        galleryFireStoreDesignRecyclerView.adapter = fireStoreDesignAdapter

        val user = Firebase.auth.currentUser
        if (user != null) {
            db.collection("users").document(user.email)
                .collection("designs").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        document.data["imagePixels"].let {
                            val imagePixels = (it as List<Long>).map { el -> el.toInt() }.toIntArray()
                            designPreviews.add(Bitmap.createBitmap(imagePixels,
                                0,
                                animalCrossingDesignWidth,
                                animalCrossingDesignWidth,
                                animalCrossingDesignHeight,
                                Bitmap.Config.ARGB_8888))
                            fireStoreDesignAdapter.notifyDataSetChanged()

                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }

        return root
    }
}