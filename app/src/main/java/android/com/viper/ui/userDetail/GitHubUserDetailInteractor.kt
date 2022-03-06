package android.com.viper.ui.userDetail

import android.com.viper.di.annotation.IoThread
import android.com.viper.di.annotation.UiThread
import android.com.viper.model.network.Repository
import android.com.viper.model.response.UserDetailModel
import com.dzaitsev.rxviper.Interactor
import rx.Observable
import rx.Scheduler
import javax.inject.Inject

/**
 * Interactor will retrieve the data from the source, convert it into ready-to-work one, and return it to Presenter.
 * To share the work entrusted to them and increase testability,
 * another layer called Repo (Repository) was added which is responsible for obtaining the data.
 */
class GitHubUserDetailInteractor @Inject constructor(
  @IoThread subscribeOn: Scheduler,
  @UiThread observeOn: Scheduler,
  private val repository: Repository
) :
  Interactor<String, UserDetailModel>(subscribeOn, observeOn) {

  override fun createObservable(userName: String?): Observable<UserDetailModel> {
    return repository.getUserDetailData(userName)
  }
}