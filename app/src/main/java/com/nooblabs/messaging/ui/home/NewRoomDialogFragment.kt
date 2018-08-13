package com.nooblabs.messaging.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.nooblabs.messaging.R
import kotlinx.android.synthetic.main.new_room_dialog_fragment.view.*

class NewRoomDialogFragment: DialogFragment() {

    companion object {
        fun newInstance() = NewRoomDialogFragment()
    }

    private var listener: OnCreateListener? = null

    fun setOnCreateListener(listener: OnCreateListener){
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.new_room_dialog_fragment, null)
        view.create_room_btn.setOnClickListener {
            val roomName = view.new_chatroom_name.text.toString()
            listener?.onCreate(roomName)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    interface OnCreateListener {
        fun onCreate(roomName: String)
    }
}