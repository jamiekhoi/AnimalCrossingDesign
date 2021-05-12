package com.example.animalcrossingdesign.ui.gallery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossingdesign.R


class FireStoreDesignAdapter(private val dataSet: ArrayList<Bitmap>) :
    RecyclerView.Adapter<FireStoreDesignAdapter.ViewHolder>() {

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
        val data = dataSet[position]
        viewHolder.imageView.setImageBitmap(data)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
class GalleryAdapter(private val dataSet: ArrayList<Int>) :
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val textView: TextView
        val imageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            //textView = view.findViewById(R.id.textViewGrid)
            imageView = view.findViewById(R.id.gallery_grid_imageview)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.gallery_grid_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.textView.text = dataSet[position]["name"].toString()

        val data = dataSet[position]
        if (data is Int){
            viewHolder.imageView.setImageResource(data)
        }
        /*else if (data is Bitmap){
            viewHolder.imageView.setImageBitmap(dataSet[position]["image"] as Bitmap)
        }*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}