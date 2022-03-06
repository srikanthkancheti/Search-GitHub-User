package android.com.viper.model.response

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Entity refers to model objects used by Interactor. It is the simplest element of our VIPER structure.
 */
data class GitHubSearchResponseModel(
  @SerializedName("total_count") var total_count: String,
  @SerializedName("incomplete_results") var incomplete_results: Boolean,
  @SerializedName("items") var items: List<GitHubSearchResultItem>
) : Serializable