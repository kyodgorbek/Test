package com.example.test.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.model.User
import kotlinx.android.synthetic.main.item_user_view.view.*

class UserAdapter(
    private val deleteUserAction: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_view, parent, false)
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], deleteUserAction)
    }

    override fun getItemCount() = users.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: User, deleteUserAction: (User) -> Unit) {
            with(itemView) {
                name.text = item.name
                email.text =
                    String.format(itemView.context.getString(R.string.email_pattern, item.email))
                gender.text =
                    String.format(itemView.context.getString(R.string.gender_pattern, item.gender))
                status.text =
                    String.format(itemView.context.getString(R.string.status_pattern, item.status))
                delete_button.setOnClickListener {
                    deleteUserAction.invoke(item)
                }
            }
        }
    }
}
