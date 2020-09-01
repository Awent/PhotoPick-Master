package com.simaple

class UserBean {
    var code = 0
    var msg: String? = null
    var list: List<User>? = null

    class User {
        var avatar: String? = null
        var smallAvatar: String? = null
        var name: String? = null
        var age = 0
    }
}