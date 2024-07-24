package com.rasel.androidbaseapp.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.orhanobut.logger.Logger
import com.rasel.androidbaseapp.cache.database.AppDatabase
import com.rasel.androidbaseapp.cache.entities.Plant
import com.rasel.androidbaseapp.util.PLANT_DATA_FILENAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SeedDatabaseWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val database: AppDatabase
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(PLANT_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<Plant>>() {}.type
                    val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)

//                    val database = AppDatabase.invoke(applicationContext)
                    database.getPlantDao().insertAll(plantList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Logger.e("Error seeding database ${ex.stackTrace}")
            Result.failure()
        }
    }
}
