package com.example.animalcrossingdesign.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.DesignDataClassSimple
import com.example.animalcrossingdesign.R
import com.example.animalcrossingdesign.databinding.FragmentHomeBinding
import com.example.animalcrossingdesign.ui.gallery.FireStoreDesignAdapter
import com.example.animalcrossingdesign.ui.gallery.GalleryViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    companion object {
        private val TAG = HomeFragment::class.qualifiedName
        private val animalCrossingDesignWidth = 32
        private val animalCrossingDesignHeight = 32
    }
    //private lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //val viewModel: HomeViewModel by activityViewModels()
        val viewModel: GalleryViewModel by activityViewModels()

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the VieWModel
        ///fragmentHomeBinding.homeViewModel = homeViewModel

        // TODO: find out how this works and if it's important
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        ///fragmentHomeBinding.lifecycleOwner = viewLifecycleOwner

        //val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = fragmentHomeBinding.textHome//root.findViewById(R.id.text_home)
        /*viewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

         */

        val homeFeedFireStoreDesignRecyclerView: RecyclerView = fragmentHomeBinding.homeFeedFireStoreDesignRecyclerView
        val gridfirestorelayoutmanger = GridLayoutManager(fragmentHomeBinding.root.context, 3)
        homeFeedFireStoreDesignRecyclerView.layoutManager = gridfirestorelayoutmanger

        // Access the RecyclerView Adapter and load the data into it
        val designPreviews = arrayListOf<DesignDataClassSimple>()
        val clicklistener =  { v: View, item: DesignDataClassSimple ->
            viewModel.design.postValue(item)
            v.findNavController().navigate(R.id.action_nav_home_to_nav_design_detail)
        }
        val fireStoreDesignAdapter = FireStoreDesignAdapter(designPreviews, clicklistener)
        homeFeedFireStoreDesignRecyclerView.adapter = fireStoreDesignAdapter

        db.collection("users/james.k.giang@gmail.com/designs").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(HomeFragment.TAG, "${document.id} => ${document.data}")
                    val design = document.toObject<DesignDataClassSimple>()
                    designPreviews.add(design)
                    fireStoreDesignAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(HomeFragment.TAG, "Error getting documents: ", exception)
            }

        db.collection("designs").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(HomeFragment.TAG, "${document.id} => ${document.data}")
                    val design = document.toObject<DesignDataClassSimple>()
                    designPreviews.add(design)
                    fireStoreDesignAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(HomeFragment.TAG, "Error getting documents: ", exception)
            }

        return fragmentHomeBinding.root
    }
}