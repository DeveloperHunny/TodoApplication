package com.example.todoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), ItemStartDragListener {

    lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var todoAdapter: TodoAdapter
    lateinit var dialogView : itemsetDialogClass
    lateinit var todoList : ArrayList<TodoItem>
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var dbHandler: DBHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //변수 선언 및 초기화
        dbHandler = DBHandler(this,"todoApplication.db",1,"mainTable")

        val addBtn = findViewById<Button>(R.id.addBtn)
        val moveBtn = findViewById<Button>(R.id.moveBtn)
        val configBtn = findViewById<Button>(R.id.configBtn)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        todoList = ArrayList<TodoItem>()
        todoAdapter = TodoAdapter(this, todoList,this)
        linearLayoutManager = LinearLayoutManager(this)

        todoAdapter.setOnItemClickListener(object: itemClickListener{
            override fun onClick(itemView: View, clickView: View, position: Int) {
                if(clickView != null){
                    when(clickView.id){
                        R.id.completeBtn->{
                            completeBtnOnClick(itemView,position)
                        }
                        else->{
                            Log.d("TEST", "else")
                        }
                    }
                }
            }
        })

        //recyclerView 설정
        recyclerView.apply {
            adapter = todoAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }

        //ItemTouchHelper 설정
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(todoAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView) //RecyclerView에 ItemTouchHelper setting


        //todoList sort방법 설정정
        todoList.sortWith(Comparator{ data1, data2 ->
            data1.complete.compareTo(data2.complete)
        })

        //AddBtn 클릭 이벤트 설정
        addBtn.setOnClickListener {
            dialogView = itemsetDialogClass(this, todoList, todoAdapter)
            dialogView.showDialog()
        }

        //MoveBtn 클릭 이벤트 설정
        moveBtn.setOnClickListener{
            val dirDialog = dirDialogClass(this)
            dirDialog.showDialog()
        }

        //configBtn 클릭 이벤트 설정
        configBtn.setOnClickListener{
            TODO("CONFIG BTN FUNCTION SET")
        }

        //DB에서 데이터 읽어와서 보여주기
        dbHandler.getAllData(todoList,todoAdapter)

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    fun completeBtnOnClick(parentView:View, position:Int){
        var todoItem = todoList.get(position)

        if(todoItem.complete == 0){ //not complete -> complete
            todoItem.complete = 1;
            parentView.alpha = 0.5f
            if(position == linearLayoutManager.findFirstVisibleItemPosition()){ //현재 RecyclerView 일종의 버그로 0번째 index 이동 시 scroll 같이 움직임. - 이를 방지하기 위한 방법
                linearLayoutManager.scrollToPosition(position)
            }
            else{todoAdapter.notifyItemChanged(position,todoItem)}

            if(todoList.size != 1 && position != todoList.size -1){
                todoList.removeAt(position)
                todoList.add(todoItem) // todoList 맨 끝에 추가

                todoAdapter.notifyItemMoved(position, todoList.size-1) // Item 이동 알림
                //todoAdapter.notifyItemRangeChanged(position,todoList.size -1)
            }

        }
        else{ //complete -> not complete
            parentView.alpha = 1.0f
            todoItem.complete = 0
            todoAdapter.notifyItemChanged(position,todoItem)
            if(todoList.size != 1 || position != 0){
                var toIdx:Int = position
                for(idx : Int in position-1 downTo 0){
                    if(todoList.get(idx).complete == 0){
                        toIdx = idx+1
                        break;
                    }
                }
                if(toIdx != position){
                    todoList.removeAt(position)
                    todoList.add(toIdx, todoItem) // todoList toIdx 자리에 넣기
                    todoAdapter.notifyItemMoved(position, toIdx) // Item 이동 알림
                }

            }


        }

    }

    override fun onStop() {
        Log.d("TEST", "onStop called")
        //DB table 변경된 내용 추가
        //일단 mainTable만 초기화 - 이후에 여러 테이블 이름 다 받아와서 초기화
        dbHandler.insertValuesWithReplace(todoList)
        dbHandler.close()
        super.onStop()
    }
}