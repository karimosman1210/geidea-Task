package com.geidea.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geidea.task.data.model.User
import com.geidea.task.databinding.ItemUserBinding

class UsersAdapter(
    private val context: Context,
    private val items: MutableList<User>,
    private val itemClickListener: OnUserClickListener,
) : RecyclerView.Adapter<UsersAdapter.ItemHolder>() {

    /* clear adapter list */
    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        items.clear()
        notifyDataSetChanged()
    }

    /* update item list  */
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(newItems: List<User>) {
        if (items.isEmpty()) {
            items.addAll(newItems)
            notifyDataSetChanged()
        } else {
            val lastPosition = items.size - 1

            items.addAll(newItems)

            notifyItemRangeInserted(lastPosition + 1, items.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding: ItemUserBinding =
            ItemUserBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        val item = items[position]

        viewHolder.itemBinding.tvId.text = item.id.toString()
        val name = "${item.firstName} ${item.lastName}"
        viewHolder.itemBinding.tvName.text = name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(val itemBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {

        init {
            itemBinding.root.setOnClickListener {
                itemClickListener.onUserClick(items[bindingAdapterPosition])
            }
        }
    }

    /* adapter item click  */
    interface OnUserClickListener {
        fun onUserClick(item: User)
    }
}