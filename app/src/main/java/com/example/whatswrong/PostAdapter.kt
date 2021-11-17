package com.example.whatswrong

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter (val mContext : Context, val PostList : ArrayList<Posts>, val className : String) : RecyclerView.Adapter<PostAdapter.CustomViewHolder>() {

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postTitle = itemView.findViewById<TextView>(R.id.post_title)
        val postContent = itemView.findViewById<TextView>(R.id.post_content)
        val postUser = itemView.findViewById<TextView>(R.id.post_user)
        val postTime = itemView.findViewById<TextView>(R.id.post_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post,parent,false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition
                val post : Posts = PostList.get(curPos)
                val intent = Intent(mContext,PostActivity::class.java)

                intent.putExtra("과목명", className)
                intent.putExtra("title", post.title)
                intent.putExtra("content", post.content)
                intent.putExtra("user", post.user)
                intent.putExtra("time", post.time)

                mContext.startActivity(intent)
            }
        }
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