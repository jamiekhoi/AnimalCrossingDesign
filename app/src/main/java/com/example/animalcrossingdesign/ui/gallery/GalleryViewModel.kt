package com.example.animalcrossingdesign.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animalcrossingdesign.DesignDataClassSimple

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    private val mutableSelectedItem = MutableLiveData<Any>()
    val selectedItem: LiveData<Any> get() = mutableSelectedItem

    fun selectItem(design: Map<String, Any>) {
        mutableSelectedItem.value = design
    }

    //------------------
    private var _mutablelivedatalist = MutableLiveData<MutableList<DesignDataClassSimple>>().apply {
        value =  arrayListOf() // correct type?
    }
    val mutablelivedatalist: MutableLiveData<MutableList<DesignDataClassSimple>> = _mutablelivedatalist
    fun setList(list: MutableList<DesignDataClassSimple>) {
        _mutablelivedatalist.value = list
        mutablelivedatalist.value = _mutablelivedatalist.value
        //mutablelivedatalist.value = list
    }

    fun getList(): MutableLiveData<MutableList<DesignDataClassSimple>> {
        return _mutablelivedatalist
    }

    //var mutableDesignList = MutableLiveData<List<Map<String, Any>>>()
    //fun addToList(design: Map<String, Any>) {
        //mutableDesignList.
    //}
}