package android.com.viper.ui.home

import android.com.viper.model.response.GitHubSearchResultItem
import com.dzaitsev.rxviper.Router

interface SearchGitHubRouter : Router {
  fun openDetailView(gitHubSearchResultItem: GitHubSearchResultItem)
}