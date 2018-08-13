package com.nooblabs.messaging.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nooblabs.messaging.model.Room
import java.util.*

class HomeViewModel : ViewModel() {

    var chatRoomsObservable: MutableLiveData<ArrayList<Room>>? = null

    fun getChatRooms(): LiveData<ArrayList<Room>> {
        if(chatRoomsObservable == null){
            chatRoomsObservable = MutableLiveData()
            loadChatRoomFromDB()
        }
        return chatRoomsObservable!!
    }

    fun loadChatRoomFromDB() {
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.let { value ->
                        val roomList = ArrayList<Room>()
                        for(roomDoc in value){
                            val room = roomDoc.toObject(Room::class.java)
                            roomList.add(room)
                        }
                        chatRoomsObservable?.value = roomList
                    }
                }
    }

    fun generateRandomChatRoom() {
        val roomName = "Room ${Date()}"
        generateChatRoom(roomName)
    }

    fun generateChatRoom(roomName: String) {
        val db = FirebaseFirestore.getInstance()
        val room = Room(roomName = roomName, creatorEmail = FirebaseAuth.getInstance().currentUser!!.email!!)
        db.collection("rooms")
                .add(room)
                .addOnSuccessListener{ ref ->
                    ref.update("id",ref.id)
                            .addOnFailureListener {
                                ref.delete()
                            }
                    Log.d("xyz","Created chatroom with id: ${ref.id}")
                }
                .addOnFailureListener {
                    Log.d("xyz","failure while generating chat room: $it")
                }
    }

}
