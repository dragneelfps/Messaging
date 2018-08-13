package com.nooblabs.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nooblabs.messaging.R
import com.nooblabs.messaging.model.Room
import kotlinx.android.synthetic.main.chatroom_list_item.view.*

class ChatRoomListAdapter: RecyclerView.Adapter<ChatRoomListAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val data: ArrayList<Room> = ArrayList()
    private var itemListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemListener = listener
    }

    fun updateData(newData: ArrayList<Room>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.chatroom_list_item, parent, false))
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.run {
            itemView.chatoom_id.text = data[position].roomName
            itemView.setOnClickListener {
                itemListener?.onItemClick(data[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(chatroom: Room)
    }

}