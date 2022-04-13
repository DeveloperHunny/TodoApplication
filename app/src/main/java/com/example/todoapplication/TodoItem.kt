package com.example.todoapplication

import java.text.SimpleDateFormat
import java.util.*

class TodoItem {
    var id : Int? = null
    var title : String = ""
    var content: String = ""
    var dueTime: Date = Date()
    var complete: Int = 0

    constructor(title:String, content:String){
        this.title = title;
        this.content = content;
    }

    constructor(id: Int,title: String,content: String,complete:Int, dueTime: Date){
        this.id = id
        this.title = title
        this.content = content
        this.complete = complete
        this.dueTime = dueTime
    }

}