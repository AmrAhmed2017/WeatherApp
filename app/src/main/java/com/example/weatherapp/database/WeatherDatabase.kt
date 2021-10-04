package com.example.weatherapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.App
import com.example.weatherapp.model.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListTypeConverter::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun cityDao(): CityDao

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
                instance
            }
        }
    }
}