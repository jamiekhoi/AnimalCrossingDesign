package com.example.animalcrossingdesign.ui.gallery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.DesignDataClassSimple
import com.example.animalcrossingdesign.R


class FireStoreDesignAdapter(//private val dataSet: ArrayList<DesignDataClassSimple>,
private val onItemClicked_listener: (DesignDataClassSimple) -> Unit) :
    RecyclerView.Adapter<FireStoreDesignAdapter.ViewHolder>() {
    companion object {
        private val animalCrossingDesignWidth = 32
        private val animalCrossingDesignHeight = 32
    }
    var data: MutableList<DesignDataClassSimple> = ArrayList(0)
        set(value) {
            field = value
            //field.add(value)
            notifyDataSetChanged()
        }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val textView: TextView
        val imageView: ImageView// = view.findViewById(R.id.galleryFireStoreDesignRecyclerView)

        init {
            // Define click listener for the ViewHolder's View.
            //textView = view.findViewById(R.id.textViewGrid)
            imageView = view.findViewById(R.id.gallery_design_grid_view)
            //imageView.setOnClickListener {view ->
              //  view.findNavController().navigate(R.id.action_nav_gallery_to_nav_design_detail)
            //}
        }

        fun bind(item: DesignDataClassSimple) {
            //setOnClickListener{}
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.gallery_design_grid_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = data[position]
        viewHolder.bind(item)
        viewHolder.imageView.setOnClickListener { onItemClicked_listener(item)}
        viewHolder.imageView.setImageBitmap(Bitmap.createBitmap(item.imagePixels.toIntArray(),
                0,
                animalCrossingDesignWidth,
                animalCrossingDesignWidth,
                animalCrossingDesignHeight,
                Bitmap.Config.ARGB_8888)) // move this into viewHolder.bind(item)?
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size//dataSet.size

}
