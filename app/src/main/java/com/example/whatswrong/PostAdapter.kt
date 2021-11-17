package com.example.whatswrong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter (val PostList : ArrayList<Posts>) : RecyclerView.Adapter<PostAdapter.CustomViewHolder>() {
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postTitle = itemView.findViewById<TextView>(R.id.post_title)
        val postContent = itemView.findViewById<TextView>(R.id.post_content)
        val postUser = itemView.findViewById<TextView>(R.id.post_user)
        val postTime = itemView.findViewById<TextView>(R.id.post_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.postTitle.text = PostList.get(position).title
        holder.postContent.text = PostList.get(position).content
        holder.postUser.text = PostList.get(position).user
        holder.postTime.text = PostList.get(position).time
    }

    override fun getItemCount(): Int {
        return PostList.size
    }
}