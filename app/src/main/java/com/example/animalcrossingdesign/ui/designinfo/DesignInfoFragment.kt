package com.example.animalcrossingdesign.ui.designinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.animalcrossingdesign.R
import com.example.animalcrossingdesign.ui.gallery.GalleryViewModel

class DesignInfoFragment : Fragment() {

    companion object {
        fun newInstance() = DesignInfoFragment()
    }

    //private lateinit var viewModel: DesignInfoViewModel
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_design_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(DesignInfoViewModel::class.java)
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        // TODO: Use the ViewModel

        viewModel.getList().observe(viewLifecycleOwner, Observer {
            var temp = it
        })
        viewModel.text.observe(viewLifecycleOwner, Observer {
            val temp = it
        })

        viewModel.mutablelivedatalist.observe(viewLifecycleOwner, Observer {
            val temp = it
        })
    }

}