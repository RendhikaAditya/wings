package co.dh.wings.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.dh.wings.database.dao.TransactionDao
import co.dh.wings.network.response.Transactions

@Database(entities = [Transactions::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "penjualan_database").allowMainThreadQueries()
                .build()
        }
    }
}