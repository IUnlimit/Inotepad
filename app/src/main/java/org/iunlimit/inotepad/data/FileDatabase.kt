package org.iunlimit.inotepad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.iunlimit.inotepad.data.models.FileData

@Database(
    entities = [FileData::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    TypeConverter::class
)
abstract class FileDatabase: RoomDatabase() {

    abstract fun fileDao(): FileDao

    // public static final class
    companion object {

        @Volatile // violate
        private var INSTANCE: FileDatabase? = null

        fun getDatabase(context: Context): FileDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileDatabase::class.java,
                    "file_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

}