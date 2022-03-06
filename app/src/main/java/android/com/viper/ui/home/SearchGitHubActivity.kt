package android.com.viper.ui.home

import android.com.viper.R
import android.com.viper.app.NetworkActivity
import android.com.viper.app.adapter.SearchResultsRecyclerAdapter
import android.com.viper.di.component.BaseActivityComponent
import android.com.viper.model.response.GitHubSearchResultItem
import android.com.viper.ui.userDetail.GitHubUserDetailActivity
import android.com.viper.ui.helper.OnBottomReachedListener
import android.com.viper.util.UiUtils
import android.com.viper.util.setOnClickListenerThrottled
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_github_search.*
import kotlinx.android.synthetic.main.appbar_search_white_layout.*
import javax.inject.Inject

class SearchGitHubActivity : NetworkActivity(), SearchGitHubView, SearchGitHubRouter, OnBottomReachedListener {

  @Inject lateinit var presenter: SearchGitHubPresenter
  private var adapter: SearchResultsRecyclerAdapter? = null

  override fun setupComponent(component: BaseActivityComponent) = component.inject(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_github_search)
    initView()
    initSearchView()
  }

  private fun initSearchView() {
    searchEditText.hint = getString(R.string.hint_search)
    searchButton.setOnClickListenerThrottled {
      UiUtils.hideSoftKeyboard(searchEditText)
      if (searchEditText.text.toString().isNotEmpty())
        presenter.getFirstPageGitHubSearchResults(searchEditText.text.toString())
      else
        showIoError("Please enter a word to search!")
    }

    searchEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        if (searchEditText.text.toString().isNotEmpty())
          presenter.getFirstPageGitHubSearchResults(searchEditText.text.toString())
        else
          showIoError("Please enter a word to search!")
      }
      false
    }
  }

  private fun initView() {
    searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
    adapter = SearchResultsRecyclerAdapter(this, this) { gitHubSearchResultItem -> presenter.gitHubResultDetailRequested(gitHubSearchResultItem) }
    searchResultsRecyclerView.adapter = adapter
  }

  override fun onStart() {
    super.onStart()
    presenter.takeView(this)
    presenter.takeRouter(this)
    presenter.subscribeToGitHubSearchUpdates()
  }

  override fun onStop() {
    presenter.dropView(this)
    presenter.dropRouter(this)
    super.onStop()
  }

  /**
   * Start of View callbacks region
   */
  // View call back method to bing data to recycler view
  override fun showGitHubSearchResults(searchResults: PagedList<GitHubSearchResultItem>) {
    adapter?.submitList(searchResults)
  }

  override fun showViewsForNoResults() {
    showIoError("You reached to the end of the list, nothing more to load!")
  }

  override fun showSearchError() {
    showDefaultErrorMessage()
  }

  // Router method implementation to navigate to detail screen
  override fun openDetailView(gitHubSearchResultItem: GitHubSearchResultItem) {
    GitHubUserDetailActivity.start(this, gitHubSearchResultItem.login)
  }

  /**
   * End of view callbacks region
   */

  // Call API scroll position reached to last item
  override fun onBottomReached() {
    presenter.requestGitHubSearchResults()
  }
}
