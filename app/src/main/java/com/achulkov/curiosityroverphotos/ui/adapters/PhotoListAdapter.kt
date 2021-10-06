package com.achulkov.curiosityroverphotos.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import com.achulkov.curiosityroverphotos.databinding.ListPhotosItemBinding
import com.achulkov.curiosityroverphotos.util.ImageLoader
import javax.inject.Inject

class PhotoListAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : ListAdapter<RoverPhoto, PhotoListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<RoverPhoto?>() {
    override fun areItemsTheSame(oldItem: RoverPhoto, newItem: RoverPhoto): Boolean {
        return (oldItem.id == newItem.id)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RoverPhoto, newItem: RoverPhoto): Boolean {
        return oldItem == newItem
    }
    override fun getChangePayload(oldItem: RoverPhoto, newItem: RoverPhoto): Any {
        return newItem
    }
}) {

    interface AdapterItemClickListener{
        fun onAdapterItemClick(photo : RoverPhoto, position: Int)
        fun onAdapterLongItemClick(photo: RoverPhoto, position: Int)

    }

    private lateinit var inflater: LayoutInflater

    private lateinit var listener: AdapterItemClickListener

    private val mSelectedItems = SparseBooleanArray()

    fun setListener(mListener : AdapterItemClickListener) {
        listener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPhotosItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo: RoverPhoto = getItem(position)
        val isSelected = mSelectedItems.get(position)






    }



    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val context: Context = recyclerView.context
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        inflater = LayoutInflater.from(context)


    }





    inner class ViewHolder(var binding: ListPhotosItemBinding) : RecyclerView.ViewHolder(binding.root){

        init
        {
            itemView.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION)
                    listener.onAdapterItemClick(getItem(adapterPosition), adapterPosition)

            }
            itemView.setOnLongClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION)
                    listener.onAdapterLongItemClick(getItem(adapterPosition), adapterPosition)
                false
            }
        }
    }


    fun clearSelections() {
        mSelectedItems.clear()
        notifyDataSetChanged()
    }

    fun toggleItem(position: Int) {
        mSelectedItems.put(position, !mSelectedItems.get(position))
        notifyItemChanged(position)
    }

    fun clearItem(position: Int) {
        mSelectedItems.put(position, false)
        notifyItemChanged(position)
    }

    fun getSelectedSize(listSize : Int) : Int {
        var result = 0
        for (i in 0..listSize) {
            if (mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    fun getItemsSelection() : SparseBooleanArray {
        return mSelectedItems
    }
}
