package com.example.parentapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parentapp.App
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.WeatherEntity

@Database(entities = [CityEntity::class, WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(): WeatherDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.applicationContext(),
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}