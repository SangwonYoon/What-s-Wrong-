package com.example.whatswrong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class CommentAdapter (val CommentList : ArrayList<Comments>) : RecyclerView.Adapter<CommentAdapter.CustomViewHolder>(){
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val commentUser = itemView.findViewById<TextView>(R.id.comment_user)
        val commentContent = itemView.findViewById<TextView>(R.id.comment_content)
        val commentTime = itemView.findViewById<TextView>(R.id.comment_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.commentUser.text = CommentList.get(position).comment_user
        holder.commentContent.text = CommentList.get(position).comment_content
        holder.commentTime.text = CommentList.get(position).comment_time
    }

    override fun getItemCount(): Int {
        return CommentList.size
    }
}