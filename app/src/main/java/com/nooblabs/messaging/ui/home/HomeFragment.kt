package com.nooblabs.messaging.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

import com.nooblabs.messaging.R
import com.nooblabs.messaging.adapter.ChatRoomListAdapter
import com.nooblabs.messaging.model.Room
import com.nooblabs.messaging.ui.addFragmentToBackStack
import com.nooblabs.messaging.ui.chat.ChatFragment
import com.nooblabs.messaging.ui.login.LoginFragment
import com.nooblabs.messaging.ui.replaceFragment
import com.nooblabs.messaging.ui.replaceFragmentBackStack
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val adapter = ChatRoomListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.getChatRooms().observe(this, Observer {
            adapter.updateData(it)
        })

        view?.let { view ->
            view.chatroom_list.layoutManager = LinearLayoutManager(requireContext())
            view.chatroom_list.adapter = adapter
            view.new_room_btn.setOnClickListener {
//                viewModel.generateRandomChatRoom()
                val newRoomDialog = NewRoomDialogFragment.newInstance()
                newRoomDialog.setOnCreateListener(object : NewRoomDialogFragment.OnCreateListener {
                    override fun onCreate(roomName: String) {
                        viewModel.generateChatRoom(roomName)
                    }
                })
                newRoomDialog.show(childFragmentManager, "create_chat_room_dialog")
            }
        }

        adapter.setOnItemClickListener(object : ChatRoomListAdapter.OnItemClickListener {
            override fun onItemClick(chatroom: Room) {
                val chatFragment = ChatFragment.newInstance()
                chatFragment.arguments = Bundle().apply {
                    putString("roomName", chatroom.roomName)
                    putString("roomID", chatroom.id)
                }
//                fragmentManager?.replaceFragment(R.id.container, chatFragment)
                fragmentManager?.replaceFragmentBackStack(R.id.container, chatFragment, "home_fragment")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sign_out_item -> {
                FirebaseAuth.getInstance().signOut()
                fragmentManager?.replaceFragment(R.id.container, LoginFragment.newInstance())
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
