package com.nooblabs.messaging.model

import java.util.*

data class Message(
        var data: String = "",
        var fromEmail: String = "",
        var fromDisplayName: String = "",
        var timeStamp: Long = Date().time
)