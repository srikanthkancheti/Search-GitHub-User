package android.com.viper.model

import android.com.viper.app.db.SearchResultsDao
import android.com.viper.app.db.ViperSampleDB
import android.com.viper.di.annotation.IoThread
import android.com.viper.di.annotation.UiThread
import android.com.viper.model.network.Repository
import android.com.viper.model.response.GitHubSearchResponseModel
import android.com.viper.model.response.GitHubSearchResultItem
import androidx.paging.PagedList
import androidx.paging.toFlowable
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubSearchManager @Inject constructor(
  private val repository: Repository,
  private val viperSampleDB: ViperSampleDB,
  @IoThread private val ioScheduler: Scheduler,
  @UiThread private val uiScheduler: Scheduler,
  @IoThread private val rxIoScheduler: io.reactivex.Scheduler,
  @UiThread private val rxUiScheduler: io.reactivex.Scheduler
) {

  private val searchResultsDao: SearchResultsDao = viperSampleDB.points()
  private var searchResultsDataPage = 0
  private var language = ""

  private val searchErrorChannel = PublishSubject.create<Throwable>()
  private val gitHubSearchSubscriptions = CompositeSubscription()

  fun subscribeForSearchErrors(subscriber: (Throwable) -> Unit): Subscription {
    return searchErrorChannel.subscribe(subscriber)
  }

  fun getGitHubSearchDataObservable(): Flowable<PagedList<GitHubSearchResultItem>> {
    return searchResultsDao.getSearchResultsData().toFlowable(
      config = PagedList.Config.Builder()
        .setPageSize(DEFAULT_PAGE_SIZE)
        .setPrefetchDistance(PREFETCH_DIASTANCE) // give user enough room for smooth scrolling
        .setInitialLoadSizeHint(DEFAULT_PAGE_SIZE)
        .setEnablePlaceholders(true) // placeholders needed to help us save scroll state
        .build(),
      boundaryCallback = ResultsBoundaryCallback(),
      fetchScheduler = rxIoScheduler,
      notifyScheduler = rxUiScheduler,
      backpressureStrategy = BackpressureStrategy.LATEST
    )
  }

  fun fetchFirstPageFromRemote(searchString: String, networkFetchCompletedCallback: ((Int) -> Unit)? = null) {
    language = searchString;
    searchResultsDataPage = 1
    gitHubSearchSubscriptions.add(
      repository.getGitHubSearchData(language, DEFAULT_PAGE_SIZE, searchResultsDataPage)
        .flatMap { content ->
          viperSampleDB.runInTransaction {
            searchResultsDao.deleteAll()
            searchResultsDao.insert(content.items)
          }
          return@flatMap Observable.just(content)
        }
        .subscribeOn(ioScheduler)
        .observeOn(uiScheduler)
        .subscribe(
          { content ->
            gitHubSearchSubscriptions.clear()
            networkFetchCompletedCallback?.invoke(content.items.size)
          },
          { throwable ->
            gitHubSearchSubscriptions.clear()
            searchErrorChannel.onNext(throwable)
          }
        )
    )
  }

  fun fetchNextPageFromRemote(networkFetchCompletedCallback: ((Int) -> Unit)? = null) {
    searchResultsDataPage += 1
    gitHubSearchSubscriptions.add(
      repository.getGitHubSearchData(language, DEFAULT_PAGE_SIZE, searchResultsDataPage)
        .flatMap { content ->
          if (content.items.isNotEmpty()) {
            searchResultsDao.insert(content.items)
          }
          return@flatMap Observable.just(content)
        }
        .subscribeOn(ioScheduler)
        .observeOn(uiScheduler)
        .subscribe(
          { content ->
            gitHubSearchSubscriptions.clear()
            networkFetchCompletedCallback?.invoke(content.items.size)
          },
          { throwable ->
            gitHubSearchSubscriptions.clear()
            searchErrorChannel.onNext(throwable)
          }
        )
    )
  }

  inner class ResultsBoundaryCallback : PagedList.BoundaryCallback<GitHubSearchResultItem>() {
    override fun onItemAtEndLoaded(itemAtEnd: GitHubSearchResultItem) {
      if (!gitHubSearchSubscriptions.hasSubscriptions()) {
        fetchNextPageFromRemote()
      }
    }

    // override fun onZeroItemsLoaded() {
    //   super.onZeroItemsLoaded()
    //   fetchFirstPageFromRemote()
    // }
  }

  companion object {
    private const val DEFAULT_PAGE_SIZE = 20
    private const val PREFETCH_DIASTANCE = 10
  }

}