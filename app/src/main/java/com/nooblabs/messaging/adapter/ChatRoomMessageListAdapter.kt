package com.nooblabs.messaging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nooblabs.messaging.R
import com.nooblabs.messaging.model.Message
import kotlinx.android.synthetic.main.chatroom_message_item.view.*
import java.util.*

class ChatRoomMessageListAdapter: RecyclerView.Adapter<ChatRoomMessageListAdapter.VH>() {

    class VH(itemView: View): RecyclerView.ViewHolder(itemView)

    private val data = ArrayList<Message>()

    fun setData(newData: ArrayList<Message>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.chatroom_message_item, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.run {
            val it = data[position]
            itemView.message_data.text = it.data
            itemView.sender_name.text = it.fromDisplayName
            itemView.time.text = Date(it.timeStamp).toString()
        }
    }
}