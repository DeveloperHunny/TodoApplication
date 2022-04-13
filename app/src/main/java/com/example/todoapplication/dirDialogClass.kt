package com.example.todoapplication

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class dirDialogClass(val context: Context) : ItemStartDragListener {
    val dialogView = Dialog(context)
    val dbHandler = DBHandler(context,"todoApplication.db",1,"dirTable")
    val dirList = ArrayList<String>()
    val dirAdapter = DirAdapter(context, dirList,this)
    val linearLayoutManager = LinearLayoutManager(context)

    lateinit var recyclerView: RecyclerView
    lateinit var moveBtn: Button
    lateinit var newBtn: Button
    lateinit var deleteBtn: Button
    lateinit var closeBtn: ImageButton

    //ItemTouchHelper 설정
    val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(dirAdapter))

    fun showDialog(){

        //객체 초기화 및 view content 설정
        initializeView()

        //Button Setting
        BtnClickEventSetting()

        dialogView.show()
    }

    fun initializeView(){
        dialogView.setContentView(R.layout.dir_dialog)

        moveBtn = dialogView.findViewById(R.id.moveBtn)
        newBtn = dialogView.findViewById(R.id.newBtn)
        deleteBtn = dialogView.findViewById(R.id.deleteBtn)
        closeBtn = dialogView.findViewById(R.id.closeBtn)
        recyclerView = dialogView.findViewById(R.id.recyclerView)

        //recyclerView 설정
        recyclerView.apply {
            adapter = dirAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        itemTouchHelper.attachToRecyclerView(recyclerView) //RecyclerView에 ItemTouchHelper setting
        //dbHandler.getAllData(dirList)
        dirList.add("main")
        dirList.add("weekly")
        dirList.add("monthly")
        dirAdapter.notifyDataSetChanged()
    }

    fun BtnClickEventSetting(){
        moveBtn.setOnClickListener{

        }

        newBtn.setOnClickListener {
            TODO("add Dir")
        }

        deleteBtn.setOnClickListener {
            TODO("delete Dir")
        }

        closeBtn.setOnClickListener {
            dialogView.dismiss()
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}