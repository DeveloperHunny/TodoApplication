package com.example.todoapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat

object todoEntity{
    const val ID = "id"
    const val TITLE = "title"
    const val CONTENT = "content"
    const val COMPLETE = "complete"
    const val DUETIME = "duetime"
}

class DBHandler(val context: Context, val dbName: String, val dbVersion: Int, val tableName: String) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    private val SQL_CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "${todoEntity.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${todoEntity.TITLE} TEXT, " +
                "${todoEntity.CONTENT} TEXT, " +
                "${todoEntity.COMPLETE} INTEGER, " +
                "${todoEntity.DUETIME} TEXT)"

    private val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + tableName


    override fun onCreate(db: SQLiteDatabase) {
        Log.d("TEST","onCreate Exec")
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    fun insertData(todoItem: TodoItem){
        val db = this.writableDatabase

        val values = ContentValues().apply{
            put(todoEntity.ID, todoItem.id)
            put(todoEntity.TITLE, todoItem.title)
            put(todoEntity.CONTENT, todoItem.content)
            put(todoEntity.COMPLETE, todoItem.complete)
            put(todoEntity.DUETIME, SimpleDateFormat("yyyy-dd-HH-mm").format(todoItem.dueTime))
        }

        db.insert(tableName, null, values)
    }

    fun insertData(dirName: String){
        val db = this.writableDatabase

        val values = ContentValues().apply{
            put("dirName", dirName)
        }

        db.insert(tableName, null, values)
    }

    fun getData(todoItem: TodoItem): TodoItem?{
        val db = this.readableDatabase

        val projection = arrayOf(todoEntity.ID, todoEntity.TITLE, todoEntity.CONTENT, todoEntity.COMPLETE, todoEntity.DUETIME)
        val selection = "${todoEntity.ID} = ?"
        val selectionArgs = arrayOf(todoItem.id.toString())

        val cursor = db.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "1"
        )




        if(cursor.count != 0){
            cursor.moveToFirst()
            var id = cursor.getInt(0)
            var title = cursor.getString(1)
            var content = cursor.getString(2)
            var complete = cursor.getInt(3)
            var duetime = SimpleDateFormat("yyyy-dd-HH-mm").parse(cursor.getString(4))

            return TodoItem(id,title,content,complete,duetime)
        }

        return null
    }

    fun getAllData(todoList : ArrayList<TodoItem>, todoAdapter: TodoAdapter): Boolean{
        Log.d("TEST", "getAllData called")
        val db = this.readableDatabase

        val projection = arrayOf(todoEntity.ID, todoEntity.TITLE, todoEntity.CONTENT, todoEntity.COMPLETE, todoEntity.DUETIME)
        val selection = null
        val selectionArgs = null

        val cursor = db.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        todoList.clear() // List 초기화
        var tempList = ArrayList<TodoItem>()
        with(cursor){
            while(moveToNext()){
                var id = cursor.getInt(0)
                var title = cursor.getString(1)
                var content = cursor.getString(2)
                var complete = cursor.getInt(3)
                var duetime = SimpleDateFormat("yyyy-dd-HH-mm").parse(cursor.getString(4))

                if(complete == 1){ tempList.add(TodoItem(id,title,content,complete,duetime)) }
                else{ todoList.add(TodoItem(id,title,content,complete,duetime)) }
            }
        }
        todoList += tempList
        todoAdapter.notifyDataSetChanged()
        return true
    }

    fun insertValuesWithReplace(todoList: ArrayList<TodoItem>){
        Log.d("TEST", "insertValuesWithReplace called")
        val db = this.writableDatabase

        todoList.forEach {
            val values = ContentValues().apply{
                put(todoEntity.ID, it.id)
                put(todoEntity.TITLE, it.title)
                put(todoEntity.CONTENT, it.content)
                put(todoEntity.COMPLETE, it.complete)
                put(todoEntity.DUETIME, SimpleDateFormat("yyyy-dd-HH-mm").format(it.dueTime))
            }

            db.replace(tableName, null, values)
        }

    }

    fun getAllData(dirList : ArrayList<String>): Boolean{
        Log.d("TEST", "getAllData called")
        val db = this.readableDatabase

        val projection = arrayOf("dirName")
        val selection = null
        val selectionArgs = null

        val cursor = db.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        dirList.clear() // List 초기화
        with(cursor){
            while(moveToNext()){
                var dirName = cursor.getString(0)
                dirList.add(dirName)
            }

        }
        return true
    }

    /*fun deleteData(user: User): Int{
        var db = this.writableDatabase

        val selection = "${userEntity.ID} = ?"
        val selectionArgs = arrayOf(user.id)

        var deleteRowNum = db.delete(tableName, selection, selectionArgs)

        return deleteRowNum
    }*/

    /*fun updateData(user: User): Int{
        val db = this.writableDatabase

        val selection = "${userEntity.ID} = ?"
        val selectionArgs = arrayOf(user.id)

        val values = ContentValues().apply {
            put(userEntity.ID,user.id)
            put(userEntity.PASSWORD, user.password)
            put(userEntity.NAME, user.name)
        }

        var updateRowNum = db.update(tableName,values,selection,selectionArgs)

        return updateRowNum
    }*/

}


