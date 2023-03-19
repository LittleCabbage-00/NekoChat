package com.myneko.neko_chat_interface

class Friend {
    @JvmField
	var friend_id: String? = null
    @JvmField
	var friend_name: String? = null
    @JvmField
	var friend_icon: String? = null
    @JvmField
	var ipAddress: String? = null
    @JvmField
	var user_id1: String? = null
    @JvmField
	var user_id2: String? = null

    constructor()
    constructor(friendId: String?, friendName: String?, frendIco: String?, ipAddress: String?) {
        friend_id = friendId
        friend_name = friendName
        friend_icon = frendIco
        this.ipAddress = ipAddress
    }
}