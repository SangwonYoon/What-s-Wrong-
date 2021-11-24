package com.example.whatswrong

data class SchdulerData(

    var index:Int?=null,
    var subject:String?=null,

) {
    constructor() : this(0,"")
}