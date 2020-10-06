package com.example.netservice

import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class UserAdapter(
    val pictures: List<User>,
    val onClick: (User) -> Unit)
    : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.list_item,
                    parent,
                    false
                )
        )
        holder.root.setOnClickListener {
            onClick(pictures[holder.adapterPosition])
        }
        return holder
    }


    inner class UserViewHolder(var root: View) : RecyclerView.ViewHolder(root) {
        fun bind(user: User) {
            with(root) {
                text.text = user.description
//                image.background = BitmapDrawable(resources, BitmapFactory.decodeByteArray(b, 0, b.size))
                image.background = BitmapDrawable(resources,user.lowQ)
            }
        }
    }

    override fun getItemCount() = pictures.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(pictures[position])
}
