package com.ankitgupta.dev.mychatapp.model

import java.io.Serializable

class UserModel :Serializable {

    var name: String? = null
        var email: String? = null
        var description: String? = null
        var imageUrl: String? = null
    var phNumber :String? = null
        var uid:String? = null


        constructor() {
            // Default constructor required by Firebase
        }

        constructor(name: String?, email: String?, description: String?, imageUrl: String?,number: String,uid : String?) {
            this.name = name
            this.email = email
            this.description = description
            this.imageUrl = imageUrl
            this.phNumber = number
            this.uid = uid

        }
    override fun toString(): String {
        return "name = $name, email=$email, description=$description,imageUrl = $imageUrl, phNumber = $phNumber,uId = $uid"
    }
    }
