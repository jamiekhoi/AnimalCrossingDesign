package com.example.animalcrossingdesign.ui.create

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.AnimalCrossingQRObject
import com.example.animalcrossingdesign.DesignDataClassSimple
import com.example.animalcrossingdesign.R
import com.example.animalcrossingdesign.databinding.ImageGridViewBinding
import com.example.animalcrossingdesign.pixelListToBitmap

class CustomAdapter(private val dataSet: ArrayList<Pair<Bitmap, AnimalCrossingQRObject>>,
                    private val listener: (v: View, item: DesignDataClassSimple) -> Unit) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val textView: TextView
        //val imageView: ImageView
        val imageGridViewBinding = ImageGridViewBinding.bind(view)

        init {
            // Define click listener for the ViewHolder's View.
            //textView = view.findViewById(R.id.textViewGrid)
            //imageView = view.findViewById(R.id.imageViewGrid)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.image_grid_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.textView.text = dataSet[position]["name"].toString()

        val data = dataSet[position]

        viewHolder.imageGridViewBinding.imageViewGrid.setOnClickListener {
            // Todo make this listener external and reuse this class in home feed
            //viewModel.design.postValue(item)
            //it.findNavController().navigate(R.id.action_nav_gallery_to_nav_design_detail)
            listener(it, data.second.toDesignDataClassSimple())
        }

        viewHolder.imageGridViewBinding.imageViewGrid.setImageBitmap(data.first)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}