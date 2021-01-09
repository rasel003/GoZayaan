package com.rasel.androidbaseapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rasel.androidbaseapp.data.db.dao.PlantDao
import com.rasel.androidbaseapp.data.db.dao.UserDao
import com.rasel.androidbaseapp.data.db.entities.Department
import com.rasel.androidbaseapp.data.db.entities.Designation
import com.rasel.androidbaseapp.data.db.entities.Plant
import com.rasel.androidbaseapp.data.db.entities.User
import com.rasel.androidbaseapp.workers.SeedDatabaseWorker

@Database(
    entities = [
        User::class,
        Department::class,
        Designation::class,
        Plant::class
    ],
    version = 1
)

//Converter class is used to store and retrieve data in the database when is not storable  in their original format
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun plantDao(): PlantDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "AppDatabase.db"
            ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
    }
}