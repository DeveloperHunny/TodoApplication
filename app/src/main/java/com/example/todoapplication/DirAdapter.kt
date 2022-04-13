package com.example.todoapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DirAdapter(private val context: Context, val dirList: ArrayList<String>, val listener: ItemStartDragListener):
    RecyclerView.Adapter<DirAdapter.ViewHolder>(), ItemTouchHelperListener {

    private lateinit var itemClickListener: itemClickListener

    fun setOnItemClickListener(itemClickListener: itemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirAdapter.ViewHolder {
        val inflateView = LayoutInflater.from(context).inflate(R.layout.diritem,parent,false)
        return DirAdapter.ViewHolder(inflateView,dirList,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this,dirList,position)
    }

    override fun getItemCount(): Int {
        return dirList.size
    }


    class ViewHolder(itemView:View, private var dirList: ArrayList<String>, listener: ItemStartDragListener ) : RecyclerView.ViewHolder(itemView){
        val dirText: TextView = itemView.findViewById(R.id.dirText)
        val hamburgerBtn: ImageView = itemView.findViewById(R.id.hamburgerBtn)

        init{
            hamburgerBtn.setOnTouchListener{v,event ->
                if(event.action == MotionEvent.ACTION_DOWN){
                    Log.d("TEST","TOUCH EVENT")
                    listener.onStartDrag(this)
                }
                false
            }
        }


        fun bind(DirAdapter: DirAdapter, dirList: ArrayList<String>, position: Int){
            dirText.setText(dirList[position])

        }


    }


    override fun onItemMoved(fromIdx: Int, toIdx: Int) {
        val fromItem = dirList.removeAt(fromIdx) //이동할 객체 저장 및 삭제
        dirList.add(toIdx, fromItem) //이동할 위치에 객체 저장
        this.notifyItemMoved(fromIdx,toIdx) //객체 이동 알림
    }

    override fun onItemSwiped(position: Int) {
        dirList.removeAt(position) //객체 삭제
        this.notifyItemRemoved(position) //객체 삭제 알림
    }


}