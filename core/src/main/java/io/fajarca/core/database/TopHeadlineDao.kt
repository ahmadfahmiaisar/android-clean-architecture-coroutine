package io.fajarca.core.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TopHeadlineDao {
    @Query("SELECT * FROM top_headlines ORDER BY publishedAt DESC")
    abstract fun findAll(): List<TopHeadlineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(characters: List<TopHeadlineEntity>)

    @Query("DELETE FROM top_headlines")
    abstract suspend fun deleteAll()
    /**
     * Execute multiple queries in single transaction
     */
    @Transaction
    open suspend fun deleteAndInsertInTransaction(topHeadlines : List<TopHeadlineEntity>) {
        deleteAll()
        insertAll(topHeadlines)
    }
}