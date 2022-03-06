package android.com.viper.ui.home

import android.com.viper.model.response.GitHubSearchResultItem
import android.com.viper.ui.errorhandling.NetworkView
import androidx.paging.PagedList

interface SearchGitHubView : NetworkView {
  fun showGitHubSearchResults(searchResults: PagedList<GitHubSearchResultItem>)
  fun showViewsForNoResults()
  fun showSearchError()
}