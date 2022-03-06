package android.com.viper.model.response

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

const val GITHUB_SEARCH_RESULTS = "github_search_results"

@Entity(tableName = GITHUB_SEARCH_RESULTS)
data class GitHubSearchResultItem(
  @PrimaryKey
  @ColumnInfo(name = "user_id")
  @SerializedName("id") var id: String,
  @ColumnInfo(name = "login")
  @SerializedName("login") val login: String,
  @ColumnInfo(name = "avatar_url")
  @SerializedName("avatar_url") val avatar_url: String,
  @ColumnInfo(name = "html_url")
  @SerializedName("html_url") val html_url: String,
  @ColumnInfo(name = "repos_url")
  @SerializedName("repos_url") var repos_url: String,
  @SerializedName("type") val type: String
)

