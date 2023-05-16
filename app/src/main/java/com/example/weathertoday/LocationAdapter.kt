package com.example.weathertoday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import com.example.weathertoday.databinding.ItemLocationBinding

class LocationAdapter(private val locations: List<Location>, val itemClick: (Location) -> Unit) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemLocationBinding>(inflater, R.layout.item_location, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.binding.location = location
        holder.binding.root.setOnClickListener { itemClick(location) }
    }

    override fun getItemCount(): Int = locations.size

    class LocationViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)
}

