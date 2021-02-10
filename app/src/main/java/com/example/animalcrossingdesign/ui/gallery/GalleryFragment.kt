package com.example.animalcrossingdesign.ui.gallery

import android.os.Bundle
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

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

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

        val galleryRecyclerView: RecyclerView = root.findViewById(R.id.galleryRecyclerView)
        //galleryRecyclerView.isNestedScrollingEnabled = false

        // Creates a vertical GridLayoutManager
        val gridlayoutmanger = GridLayoutManager(root.context, 3)
        galleryRecyclerView.layoutManager = gridlayoutmanger//GridLayoutManager(root.context, textViewRowCol.text.toString().toInt())

        // Access the RecyclerView Adapter and load the data into it
        val animalImages = arrayListOf(R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster,
                R.drawable.nh_dizzy_poster)
        val galleryAdapter = GalleryAdapter(animalImages)
        galleryRecyclerView.adapter = galleryAdapter

        return root
    }
}