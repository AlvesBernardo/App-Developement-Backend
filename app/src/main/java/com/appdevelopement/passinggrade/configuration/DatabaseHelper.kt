package com.appdevelopement.passinggrade.configuration

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appdevelopement.passinggrade.database.AppDatabase

class DatabaseHelper(private val context: Context) {
  val Migration_1_2 =
      object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
          TODO("Not yet implemented")
        }
      }

  val db: AppDatabase by lazy {
    Room.databaseBuilder(context, AppDatabase::class.java, "app-dev")
        .fallbackToDestructiveMigration()
        .build()
  }
}
