package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalNoteDao {
    @Query("SELECT * FROM personal_notes")
    fun getAllNotes(): Flow<List<PersonalNote>>

    @Query("SELECT * FROM personal_notes WHERE themeId = :themeId LIMIT 1")
    fun getNoteForTheme(themeId: String): Flow<PersonalNote?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: PersonalNote)

    @Query("DELETE FROM personal_notes WHERE themeId = :themeId")
    suspend fun deleteNote(themeId: String)
}

@Dao
interface StudyProgressDao {
    @Query("SELECT * FROM study_progress")
    fun getAllProgress(): Flow<List<StudyProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: StudyProgress)

    @Query("UPDATE study_progress SET isCompleted = :isCompleted WHERE themeId = :themeId")
    suspend fun updateCompletion(themeId: String, isCompleted: Boolean)

    @Query("UPDATE study_progress SET plannedDay = :plannedDay WHERE themeId = :themeId")
    suspend fun updatePlannedDay(themeId: String, plannedDay: Int)
}

@Dao
interface ExamAttemptDao {
    @Query("SELECT * FROM exam_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<ExamAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttempt)

    @Query("DELETE FROM exam_attempts")
    suspend fun clearAllAttempts()
}

@Database(entities = [PersonalNote::class, StudyProgress::class, ExamAttempt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personalNoteDao(): PersonalNoteDao
    abstract fun studyProgressDao(): StudyProgressDao
    abstract fun examAttemptDao(): ExamAttemptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tutor_ucs_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
