package com.example.animalcrossingdesign.ui.designinfo

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.animalcrossingdesign.AnimalCrossingQRObject
import com.example.animalcrossingdesign.databinding.FragmentDesignInfoBinding
import com.example.animalcrossingdesign.ui.gallery.GalleryViewModel

class DesignInfoFragment : Fragment() {

    companion object {
        fun newInstance() = DesignInfoFragment()
        private val animalCrossingDesignWidth = 32
        private val animalCrossingDesignHeight = 32
    }

    //private lateinit var viewModel: DesignInfoViewModel
    //private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentDesignInfoBinding = FragmentDesignInfoBinding.inflate(inflater, container, false)
        fragmentDesignInfoBinding.lifecycleOwner = viewLifecycleOwner
        //viewModel = ViewModelProvider(this).get(DesignInfoViewModel::class.java)
        //viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val viewModel: GalleryViewModel by activityViewModels()
        //val viewModel: GalleryViewModel by activityViewModels()
        // TODO: Use the ViewModel

        viewModel.text.observe(viewLifecycleOwner, Observer {
            val temp = it
        })

        viewModel.pleasechange.observe(viewLifecycleOwner, Observer {
            val t = it
        })

        viewModel.design.observe(viewLifecycleOwner, Observer {
            val acDesignBitmap = Bitmap.createBitmap(it.designPixels.toIntArray(),
                0,
                animalCrossingDesignWidth,
                animalCrossingDesignWidth,
                animalCrossingDesignHeight,
                Bitmap.Config.ARGB_8888)
            fragmentDesignInfoBinding.detailDesignImageView.setImageBitmap(acDesignBitmap)
            val QRObject = AnimalCrossingQRObject(acDesignBitmap, it.title, it.author, it.town)
            val QRCodeBitmap = QRObject.toQRBitmap()
            fragmentDesignInfoBinding.detailDesignQrImageView.setImageBitmap(QRCodeBitmap)

            fragmentDesignInfoBinding.detailDesignAuthorTextView.text = it.author
            fragmentDesignInfoBinding.detailDesignTownTextView.text = it.town
            fragmentDesignInfoBinding.detailDesignTitleTextView.text = it.title

        })

        return fragmentDesignInfoBinding.root
    }

}