package com.example.buituananh.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.buituananh.data.local.converter.SongSourceConverter
import com.example.buituananh.data.local.converter.UriConverter
import com.example.buituananh.data.local.dao.PlaylistDao
import com.example.buituananh.data.local.dao.SongDao
import com.example.buituananh.data.local.dao.UserDao
import com.example.buituananh.data.local.model.PlaylistEntity
import com.example.buituananh.data.local.model.PlaylistMusicCrossRef
import com.example.buituananh.data.local.model.SongEntity
import com.example.buituananh.data.local.model.UserEntity

@Database(
    entities = [UserEntity::class, PlaylistEntity::class, SongEntity::class, PlaylistMusicCrossRef::class],
    version = 3
)
@TypeConverters(UriConverter::class, SongSourceConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun songDao(): SongDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                ALTER TABLE users
                ADD COLUMN avatar_uri TEXT
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE users
                    ADD COLUMN phone_number TEXT
                """.trimIndent()
                )
            }
        }
    }


}
