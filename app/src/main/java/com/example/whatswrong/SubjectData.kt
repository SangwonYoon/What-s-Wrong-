package com.example.whatswrong

data class SubjectData(
    var Subject:String?=null,
    var Code : Int?=null
) {
constructor() : this("",0)
}