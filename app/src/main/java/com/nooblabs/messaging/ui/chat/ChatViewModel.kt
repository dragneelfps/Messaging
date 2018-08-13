package com.nooblabs.messaging.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nooblabs.messaging.model.Message
import com.nooblabs.messaging.model.Room
import java.util.*

class ChatViewModel : ViewModel() {

    private var chatMessageListObservable: MutableLiveData<ArrayList<Message>>? = null
    private var isAdminObservable: MutableLiveData<Boolean>? = null


    fun getMessageList(roomID: String): LiveData<ArrayList<Message>> {
        if(chatMessageListObservable == null) {
            chatMessageListObservable = MutableLiveData()
            loadMessages(roomID)
        }
        return chatMessageListObservable!!
    }

    fun loadMessages(id: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(id).collection("messages")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.let { value ->
                        val messageList = ArrayList<Message>()
                        value.forEach {
                            val message = it.toObject(Message::class.java)
                            messageList.add(message)
                        }
                        chatMessageListObservable?.value = messageList
                    }
                }
    }

    fun sendMessage(roomID: String, message: String) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser!!
        db.collection("rooms").document(roomID).collection("messages")
                .add(Message(message, user.email!!, user.displayName!!, Date().time))
                .addOnSuccessListener {
                    Log.d("xyz","Message sent")
                }
    }

    fun getAdminStatus(roomID: String): LiveData<Boolean> {
        if(isAdminObservable == null) {
            isAdminObservable = MutableLiveData()
            val db = FirebaseFirestore.getInstance()
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email
            db.collection("rooms").document(roomID).get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val room = it.result.toObject(Room::class.java)
                            isAdminObservable?.value = room?.creatorEmail == userEmail
                        } else
                            isAdminObservable?.value = false
                    }

        }
        return isAdminObservable!!
    }

    fun deleteRoom(roomID: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomID)
                .delete()
    }
}
