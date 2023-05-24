package com.ankitgupta.dev.mychatapp.model

class Message {
    var senderId: String? = null
    var content: String? = null
    var timestamp: Long = 0


    constructor() {
        // Default constructor required for Firebase Realtime Database
    }

    constructor(senderId: String?, content: String?, timestamp: Long) {
        this.senderId = senderId
        this.content = content
        this.timestamp = timestamp
    }
    override fun toString(): String {
        return "senderId=$senderId, content=$content, timestamp=$timestamp"
    }
}
