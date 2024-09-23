package com.example.travelmate2.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate2.R
import com.example.travelmate2.model.Place
import com.example.travelmate2.model.SinglePlace
import com.squareup.picasso.Picasso

class PlaceAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)

        fun bind(place: Place) {
            Picasso.get().load(place.url).into(imageView)
//            descriptionView.text = place.des
        }
    }
}

class SinglePlaceAdapter(private val singlePlaces: List<SinglePlace>) :
    RecyclerView.Adapter<SinglePlaceAdapter.SinglePlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return SinglePlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SinglePlaceViewHolder, position: Int) {
        val singlePlace = singlePlaces[position]
        holder.bind(singlePlace)
    }

    override fun getItemCount(): Int {
        return singlePlaces.size
    }

    class SinglePlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)

        fun bind(singlePlace: SinglePlace) {
            // Load image using Picasso
            Picasso.get().load(singlePlace.url).into(imageView)
            descriptionView.text = singlePlace.des
        }
    }
}
