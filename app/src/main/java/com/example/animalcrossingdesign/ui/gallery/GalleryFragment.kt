package com.example.animalcrossingdesign.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.DesignDataClassSimple
import com.example.animalcrossingdesign.R
import com.example.animalcrossingdesign.databinding.FragmentGalleryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class GalleryFragment : Fragment() {
    companion object {
        private val TAG = GalleryFragment::class.qualifiedName
        private val animalCrossingDesignWidth = 32
        private val animalCrossingDesignHeight = 32
    }
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var fragmentGalleryBinding: FragmentGalleryBinding

    private val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //val testviewModel: GalleryViewModel by activityViewModels()

        //galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val galleryViewModel: GalleryViewModel by activityViewModels()

        //val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        fragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the VieWModel
        fragmentGalleryBinding.galleryViewModel = galleryViewModel

        // TODO: find out how this works and if it's important
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        fragmentGalleryBinding.lifecycleOwner = viewLifecycleOwner

        // Observer for the Game finished event
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            val t = it
        })

        galleryViewModel.pleasechange.observe(viewLifecycleOwner, Observer {
            val t = it
        })

        galleryViewModel.pleasechange.postValue("chagned")

        //val textView: TextView = fragmentGalleryBinding.text_gallery
        //galleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        //galleryRecyclerView.isNestedScrollingEnabled = false

        val galleryFireStoreDesignRecyclerView: RecyclerView = fragmentGalleryBinding.galleryFireStoreDesignRecyclerView

        // Creates a vertical GridLayoutManager
        val gridlayoutmanger = GridLayoutManager(fragmentGalleryBinding.root.context, 3)
        val gridfirestorelayoutmanger = GridLayoutManager(fragmentGalleryBinding.root.context, 3)
        galleryFireStoreDesignRecyclerView.layoutManager = gridfirestorelayoutmanger

        // Access the RecyclerView Adapter and load the data into it
        val designPreviews = arrayListOf<DesignDataClassSimple>()
        val clicklistener =  { v: View, item: DesignDataClassSimple ->
            galleryViewModel.design.postValue(item)
            v.findNavController().navigate(R.id.action_nav_gallery_to_nav_design_detail)
        }
        val fireStoreDesignAdapter = FireStoreDesignAdapter(designPreviews, clicklistener)
        galleryFireStoreDesignRecyclerView.adapter = fireStoreDesignAdapter

        val user = Firebase.auth.currentUser
        if (user != null) {

            db.collection("users").document(user.email)
                .collection("designs").get()
            //db.collection("users/james.k.giang@gmail.com/designs").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val design = document.toObject<DesignDataClassSimple>()
                        designPreviews.add(design)
                        fireStoreDesignAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }

        return fragmentGalleryBinding.root
    }
}