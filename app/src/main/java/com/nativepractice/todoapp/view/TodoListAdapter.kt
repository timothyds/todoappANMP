package com.nativepractice.todoapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nativepractice.todoapp.databinding.TodoItemLayoutBinding
import com.nativepractice.todoapp.model.Todo

class TodoListAdapter(val todoList: ArrayList<Todo>,
    val adapterOnClick : (Todo)->Unit):RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(var binding:TodoItemLayoutBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var binding = TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
        val todo = todoList[position]
        holder.binding.checkTask.text = todo.title
        holder.binding.checkTask.isChecked = todo.isDone == 1

        holder.binding.checkTask.setOnCheckedChangeListener {
                compoundButton, isChecked ->
            if(compoundButton.isPressed){
                val updatedTodo = todo.copy(isDone = if (isChecked)1 else 0)
                adapterOnClick(updatedTodo)
                }
        }
        holder.binding.imgEdit.setOnClickListener {
            val action = TodoListFragmentDirections.actionEditTodoFragment(todoList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
    fun updateTodoList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }


}