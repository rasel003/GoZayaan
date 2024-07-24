package com.rasel.androidbaseapp.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rasel.androidbaseapp.cache.dao.CharacterDao
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.cache.dao.UserDao
import com.rasel.androidbaseapp.cache.entities.CharacterCacheEntity
import com.rasel.androidbaseapp.cache.entities.CharacterLocationCacheEntity
import com.rasel.androidbaseapp.cache.entities.Department
import com.rasel.androidbaseapp.cache.entities.Designation
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.cache.entities.User
import com.rasel.androidbaseapp.cache.utils.CacheConstants
import com.rasel.androidbaseapp.cache.utils.Migrations
import com.rasel.androidbaseapp.workers.SeedDatabaseWorker

@Database(
    entities = [
        User::class,
        Department::class,
        Designation::class,
        Plant::class,
        CharacterCacheEntity::class,
        CharacterLocationCacheEntity::class
    ],
    version = Migrations.DB_VERSION
)

//Converter class is used to store and retrieve data in the database when is not storable  in their original format
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getPlantDao(): PlantDao
    abstract fun cachedCharacterDao(): CharacterDao


    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context, workManager: WorkManager) =
            instance ?: synchronized(LOCK) {
                instance ?: buildDatabase(context, workManager).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context, workManager: WorkManager) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                CacheConstants.DB_NAME
            ).addCallback(
                object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        workManager.enqueue(request)
                    }
                }
            ).build()
    }
}