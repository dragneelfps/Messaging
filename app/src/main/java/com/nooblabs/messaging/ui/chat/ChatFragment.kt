package com.nooblabs.messaging.ui.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth

import com.nooblabs.messaging.R
import com.nooblabs.messaging.adapter.ChatRoomMessageListAdapter
import kotlinx.android.synthetic.main.chat_fragment.view.*

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel
    private val adapter = ChatRoomMessageListAdapter()
    private lateinit var roomID: String
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        // TODO: Use the ViewModel

        roomID = arguments!!.getString("roomID")!!
        viewModel.getMessageList(roomID).observe(this, Observer {
            adapter.setData(it)
        })
        viewModel.getAdminStatus(roomID).observe(this, Observer {
            isAdmin = it!!
            requireActivity().invalidateOptionsMenu()
        })

        view?.let { view ->
            view.chat_messages_list.layoutManager = LinearLayoutManager(requireContext())
            view.chat_messages_list.adapter = adapter
            view.send_btn.setOnClickListener {
                val message = view.new_message.text.toString()
                viewModel.sendMessage(roomID, message)
                clearState()
            }
        }
    }

    fun clearState() {
        view?.let { view ->
            view.new_message.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if(isAdmin)
            inflater?.inflate(R.menu.chatroom_admin_menu, menu)
        else
            inflater?.inflate(R.menu.chatroom_normal_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.dummy -> {
                Toast.makeText(requireContext(), "Ooops.", Toast.LENGTH_SHORT).show()
            }
            R.id.delete_room_item -> {
                fragmentManager?.popBackStack()
                viewModel.deleteRoom(roomID)

            }
        }
        return super.onOptionsItemSelected(item)
    }



}
