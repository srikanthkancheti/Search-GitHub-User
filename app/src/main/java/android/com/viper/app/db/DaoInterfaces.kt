package android.com.viper.app.db

import android.com.viper.model.response.GITHUB_SEARCH_RESULTS
import android.com.viper.model.response.GitHubSearchResultItem
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SearchResultsDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gitHubSearchResultItems: List<GitHubSearchResultItem>)

  @Update
  fun update(gitHubSearchResultItem: GitHubSearchResultItem)

  @Query("SELECT * FROM $GITHUB_SEARCH_RESULTS")
  fun getSearchResultsData(): DataSource.Factory<Int, GitHubSearchResultItem>

  @Query("DELETE FROM $GITHUB_SEARCH_RESULTS")
  fun deleteAll()

  @Query("SELECT COUNT(*) FROM $GITHUB_SEARCH_RESULTS")
  fun getCount(): Int
}
