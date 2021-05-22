package com.example.animalcrossingdesign.ui.designinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animalcrossingdesign.R

class DesignInfoFragment : Fragment() {

    companion object {
        fun newInstance() = DesignInfoFragment()
    }

    private lateinit var viewModel: DesignInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_design_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DesignInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}