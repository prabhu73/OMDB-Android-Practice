package com.myomdbapplication.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myomdbapplication.models.MovieItem
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@Database(
    entities = [MovieItem::class],
    version = 1,
    exportSchema = false
)
abstract class OmdbDatabase: RoomDatabase() {
    abstract fun omdbDao(): OmdbDao
}

val omdbModule = module {

    fun provideOmdbDatabase(application: Application): OmdbDatabase =
        synchronized(this) {
            Room.databaseBuilder(application, OmdbDatabase::class.java, "omdb.db").build()
        }

    fun provideOmdbDao(database: OmdbDatabase): OmdbDao =
        database.omdbDao()

    single { provideOmdbDatabase(androidApplication()) }
    single { provideOmdbDao(get()) }
}