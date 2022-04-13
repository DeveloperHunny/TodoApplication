package com.example.todoapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener{
    fun onItemMoved(fromIdx : Int, toIdx:Int)
    fun onItemSwiped(position: Int)
}

interface ItemStartDragListener{
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}


interface itemClickListener{
    fun onClick(itemView:View, clickView: View, position: Int)
}