package com.example.whatswrong

// 사용자 계정 정보 모델 클래

data class UserAccount(
    var idToken : String? = null,
    var strEmail : String? = null,
    var strPwd : String? = null,
    var strName : String? = null,
    var strPhone : String? = null,
    var strAddr : String? = null,
    var strUniv : String? = null,
    var strNickname : String? = null,
    var subject : String? = null
) {
    constructor() : this("","","","","","","","", "")
}