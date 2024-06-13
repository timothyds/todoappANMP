package com.nativepractice.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nativepractice.todoapp.R
import com.nativepractice.todoapp.TodoWorker
import com.nativepractice.todoapp.databinding.FragmentCreateTodoBinding
import com.nativepractice.todoapp.model.Todo
import com.nativepractice.todoapp.util.NotificationHelper
import com.nativepractice.todoapp.viewmodel.DetailTodoViewModel
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment(), ButtonAddTodoClickListener, RadioClick {
    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var dataBinding:FragmentCreateTodoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_todo,container,false)
        return dataBinding.root
//        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
//        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel=ViewModelProvider(this).get(DetailTodoViewModel::class.java)
//        todoListAdapter = TodoListAdapter(arrayListOf()){
//            todos -> viewModel.updateTodoStatus(todos.uuid)
//        }
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),NotificationHelper.REQUEST_NOTIF)
        }


        dataBinding.todo = Todo("","",3,0)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        dataBinding.listener = this
        dataBinding.radioListener = this
        dataBinding.btnAdd.setOnClickListener {
            val notif = NotificationHelper(view.context)
            notif.createNotification("Todo Created",
                "A new todo has been created! Stay focus!")

            val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                .setInitialDelay(30, TimeUnit.SECONDS)
                .setInputData(
                    workDataOf(
                        "title" to "Todo created",
                        "message" to "Stay focus"
                    ))
                .build()
            WorkManager.getInstance(requireContext()).enqueue(workRequest)
        }


            /* binding.btnAdd.setOnClickListener {
                 TodoWorker.activity = requireActivity()
                 var radio =
                     view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
                 var todos = Todos(binding.txtTitle.text.toString(),
                     binding.txtNotes.text.toString(), radio.tag.toString().toInt(),0)
                 val list = listOf(todos)
                 viewModel.addTodo(list)
                 //slide 37
     //            val notif = NotificationHelper(view.context, requireActivity())
     //            notif.createNotification("Tods Created",
     //                "A new tods has been created! Stay focus!")
                 //slide 47
                 TodoWorker.activity = requireActivity()
                 val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                     .setInitialDelay(5, TimeUnit.SECONDS)
                     .setInputData(
                         workDataOf(
                             "title" to "Todos created",
                             "message" to "Stay focus"
                         ))
                     .build()
                 WorkManager.getInstance(requireContext()).enqueue(workRequest)

                 Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
                 Navigation.findNavController(it).popBackStack()
             }*/

    }
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode ==NotificationHelper.REQUEST_NOTIF) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationHelper(requireContext())
                    .createNotification("Todo Created",
                        "A new todo has been created! Stay focus!")
            }
        }
    }

    override fun onRadioClick(v: View, priority: Int, obj: Todo) {
        obj.priority = priority
    }

    override fun onButtonAddTodo(v: View) {
        viewModel.addTodo(listOf(dataBinding.todo!!))
        Toast.makeText(v.context, "Data added", Toast.LENGTH_LONG).show()
        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                ))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
        Navigation.findNavController(v).popBackStack()

    }

}