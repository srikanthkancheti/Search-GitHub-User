package android.com.viper.ui.home

import android.com.viper.model.GitHubSearchManager
import android.com.viper.model.network.interceptor.NetworkAvailabilityMonitor
import android.com.viper.model.response.GitHubSearchResultItem
import android.com.viper.ui.errorhandling.NetworkPresenter
import io.reactivex.disposables.CompositeDisposable
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SearchGitHubPresenter @Inject constructor(
  private val gitHubSearchManager: GitHubSearchManager,
  private val networkMonitor: NetworkAvailabilityMonitor
) : NetworkPresenter<SearchGitHubView, SearchGitHubRouter>() {

  private val gitHubSearchSubscriptions = CompositeSubscription()
  private val gitHubSearchDisposable = CompositeDisposable()

  override fun onTakeView(view: SearchGitHubView) {
    super.onTakeView(view)

    if (!networkMonitor.isOnline()) {
      view.showMissingInternetMessage()
      // return
    }
  }

  fun subscribeToGitHubSearchUpdates() {
    gitHubSearchSubscriptions.add(gitHubSearchManager.subscribeForSearchErrors {
      view.setProgressVisibility(false)
      view.showSearchError()
    })

    view.setProgressVisibility(true)
    gitHubSearchDisposable.add(
      gitHubSearchManager.getGitHubSearchDataObservable().subscribe { content ->
        view.showGitHubSearchResults(content)
        view.setProgressVisibility(false)
      }
    )
  }

  fun getFirstPageGitHubSearchResults(searchString: String) {
    view.setProgressVisibility(true)
    gitHubSearchManager.fetchFirstPageFromRemote(searchString, fun(it: Int) {
      if (it == 0) {
        view.showViewsForNoResults()
      }
      view.setProgressVisibility(false)
    })
  }

  fun requestGitHubSearchResults() {
    // view.setProgressVisibility(true)
    gitHubSearchManager.fetchNextPageFromRemote { contentCount ->
      if (contentCount == 0) {
        view.showViewsForNoResults()
      }
      // view.setProgressVisibility(false)
    }
  }

  override fun onDropView(view: SearchGitHubView) {
    super.onDropView(view)
    gitHubSearchSubscriptions.clear()
    gitHubSearchDisposable.clear()
  }

  fun gitHubResultDetailRequested(gitHubSearchResultItem: GitHubSearchResultItem) {
    router.openDetailView(gitHubSearchResultItem)
  }
}