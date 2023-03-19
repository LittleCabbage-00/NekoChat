package com.myneko.neko_chat_interface

class User {
    @JvmField
	var user_id: String? = null
    @JvmField
	var user_pwd: String? = null
    @JvmField
	var user_name: String? = null
    @JvmField
	var user_icon: String? = null
    @JvmField
	var state: String? = null

    //无参构造函数
    constructor()

    //有参构造函数
    constructor(user_id: String?, user_pwd: String?, user_name: String?, user_icon: String?) {
        this.user_id = user_id
        this.user_pwd = user_pwd
        this.user_name = user_name
        this.user_icon = user_icon
    }

    //有参构造函数
    constructor(user_id: String?, user_pwd: String?, user_name: String?, user_icon: String?, state: String?) {
        this.user_id = user_id
        this.user_pwd = user_pwd
        this.user_name = user_name
        this.user_icon = user_icon
        this.state = state
    }
}