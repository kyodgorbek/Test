package com.example.test.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.test.R
import com.example.test.model.User
import com.example.test.utils.EmailValidator
import com.example.test.utils.setVisibility
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.spinner_layout.*
import kotlinx.android.synthetic.main.user_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListFragment : Fragment(R.layout.user_list_fragment) {

    private val viewModel by viewModel<UserListViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_new_user_button.setOnClickListener { showAddNewUserDialog() }

        val deleteUserAction: (User) -> Unit = { user ->
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_user)
                .setMessage(R.string.sure_delete_user)
                .setPositiveButton(android.R.string.ok, { _, _ -> viewModel.deleteUser(user) })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        val adapter = UserAdapter(deleteUserAction)
        users.adapter = adapter
        users.isNestedScrollingEnabled = false

        viewModel.loading.observe(viewLifecycleOwner, { showLoading ->
            progressBar.setVisibility(showLoading)
            content.setVisibility(showLoading.not())
        })

        viewModel.error.observe(viewLifecycleOwner, {
            AlertDialog.Builder(requireContext())
                .setMessage(it.message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        })

        viewModel.users.observe(viewLifecycleOwner, {
            adapter.updateUsers(it)
        })
    }

    private fun showAddNewUserDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setTitle(R.string.add_new_user)
        dialog.setContentView(R.layout.layout_fill_user)
        val name = dialog.findViewById(R.id.entry_name) as TextInputEditText
        val email = dialog.findViewById(R.id.entry_mail) as TextInputEditText
        val createButton = dialog.findViewById(R.id.create_user_button) as Button
        createButton.setOnClickListener {
            if (name.text.isNullOrBlank() || email.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Check your fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (EmailValidator.isEmailValid(email.text.toString()).not()) {
                Toast.makeText(requireContext(), "Check your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createNewUser(name.text.toString(), email.text.toString())

            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        fun newInstance() = UserListFragment()
    }
}