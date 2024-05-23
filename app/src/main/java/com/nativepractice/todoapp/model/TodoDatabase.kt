package com.nativepractice.todoapp.model

import DB_NAME
import MIGRATION_1_2
import MIGRATION_2_3
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = arrayOf(Todo::class), version = 3)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object{
        @Volatile private var instance: TodoDatabase ?= null
        private val LOCK = Any()

       private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                DB_NAME).addMigrations(MIGRATION_2_3).build()
        operator fun invoke(context:Context) {
            if(instance!=null) {
                synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }

    }
}