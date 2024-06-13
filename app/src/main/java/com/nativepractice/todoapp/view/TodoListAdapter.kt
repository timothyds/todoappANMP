package com.nativepractice.todoapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nativepractice.todoapp.databinding.TodoItemLayoutBinding
import com.nativepractice.todoapp.model.Todo

class TodoListAdapter(val todoList: ArrayList<Todo>,
    val adapterOnClick : (Todo)->Unit):RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(),
    TodoCheckedChangeListener, TodoEditClick {

    class TodoViewHolder(var binding:TodoItemLayoutBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var binding = TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
        holder.binding.todo = todoList[position] // todoitu variabel yang baru dibuat di todo_item_layout.xml
        holder.binding.listener = this
        holder.binding.editListener = this

    //        val tdo = todoList[position] // todojadi tdo buat comment doang
//        holder.binding.checkTask.text = tdo.title
//        holder.binding.checkTask.isChecked = tdo.isDone == 1
//
//        holder.binding.checkTask.setOnCheckedChangeListener {
//                compoundButton, isChecked ->
//            if(compoundButton.isPressed){
//                val updatedTodo = tdo.copy(isDone = if (isChecked)1 else 0)
//                adapterOnClick(updatedTodo)
//                }
//        }
//        holder.binding.imgEdit.setOnClickListener {
//            val action = TodoListFragmentDirections.actionEditTodoFragment(todoList[position].uuid)
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
    fun updateTodoList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onCheckedChanged(cb: CompoundButton, isChecked: Boolean, obj: Todo) {
        if(cb.isPressed){
            adapterOnClick(obj)
        }
    }

    override fun onTodoEditClick(v: View) {
        val uuid = v.tag.toString().toInt()
        val action = TodoListFragmentDirections.actionEditTodoFragment(uuid)

        Navigation.findNavController(v).navigate(action)

    }


}