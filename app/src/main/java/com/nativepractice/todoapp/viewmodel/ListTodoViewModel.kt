package com.nativepractice.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import buildDb
import com.nativepractice.todoapp.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ListTodoViewModel(application: Application):AndroidViewModel(application), CoroutineScope {
    val todoLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    fun refresh() {
        loadingLD.value = true
        todoLoadErrorLD.value = false
        viewModelScope.launch {
            val db = buildDb(

                getApplication()
            )
            val todos = withContext(Dispatchers.IO) {
            db.todoDao().getIncompleteTodos()
        }
            // Updating LiveData on the main thread
            todoLD.value = todos
            loadingLD.value = false
        }
    }
//    fun refresh() {
//        loadingLD.value = true
//        todoLoadErrorLD.value = false
//        viewModelScope.launch {
//            val db = TodoDatabase.buildDatabase(
//                getApplication()
//            )
//            todoLD.postValue(db.todoDao().selectAllTodo())
//            loadingLD.postValue(false)
//        }
//    }

    fun clearTask(todo: Todo) {
        launch {
            val db = buildDb(
                getApplication()
            )
            db.todoDao().updateTodoStatus(todo.uuid)

            todoLD.postValue(db.todoDao().getIncompleteTodos())
        }
    }


}

