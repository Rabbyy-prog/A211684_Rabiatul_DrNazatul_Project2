package com.example.a211684_rabiatul_drnazatulaini_lab5.data

import androidx.room.Database
import androidx.room.Room
import android.content.Context
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChargingHistory::class
               ],
    version = 2,
    exportSchema = false
)
 abstract class ElecTraxDatabase : RoomDatabase() {
    abstract fun chargingHistoryDao(): ChargingHistoryDao
    companion object { //companion object = static in java

        @Volatile //make sure all threads see the latest value
        private var Instance: ElecTraxDatabase? = null //object = instance
        fun getDatabase(context: Context): ElecTraxDatabase {

            return Instance ?: synchronized(this) { //synchronized = avoid race condition
                Room.databaseBuilder( //create database
                    context.applicationContext, //database belong to whole app
                    ElecTraxDatabase::class.java, //database class created which need java class reference
                    "elec_trax_database"
                )
                    .fallbackToDestructiveMigration() //if database structure changes, rebuild database
                    .build() //build database
                    .also { Instance = it } //save created database into Instance
            }
        }
    }
}