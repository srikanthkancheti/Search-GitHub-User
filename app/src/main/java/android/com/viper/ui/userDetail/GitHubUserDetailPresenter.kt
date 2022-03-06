package android.com.viper.ui.userDetail

import android.com.viper.model.network.interceptor.NetworkAvailabilityMonitor
import android.com.viper.model.response.UserDetailModel
import android.com.viper.ui.errorhandling.NetworkPresenter
import javax.inject.Inject

/**
 * Presenter may be compared to a “director” who sends commands to Interactor and Router after receiving
 * the data about the user’s actions from View,
 * and also sends the data prepared for display from Interactor to View.
 */
class GitHubUserDetailPresenter @Inject constructor(
  private val gitHubUserDetailInteractor: GitHubUserDetailInteractor,
  private val networkMonitor: NetworkAvailabilityMonitor
) : NetworkPresenter<GitHubUserDetailViewCallBacks, GitHubUserDetailRouter>() {

  private lateinit var gitHubUserName: String

  fun takeGitHubUserName(userName: String) {
    this.gitHubUserName = userName
  }

  override fun onTakeView(view: GitHubUserDetailViewCallBacks) {
    super.onTakeView(view)
    getUserDetailsFromApi(gitHubUserName)
  }

  private fun getUserDetailsFromApi(user: String) {
    view.setProgressVisibility(true)
    gitHubUserDetailInteractor.execute(makeNetworkSubscriber({
      this.onUserDetailsReceived(it)
      view.setProgressVisibility(false)
    }, { throwable ->
      onUserDetailError(throwable.message.toString())
      view.setProgressVisibility(false)
    }), user)
  }

  fun onUserDetailError(errorText: String?) {
    if (networkMonitor.isOnline())
      errorText?.let { view.showIoError(it) }
    else
      view.showMissingInternetMessage()
  }

  fun onUserDetailsReceived(userDetailModel: UserDetailModel) {
    view.showUserDetailsInViews(userDetailModel)
  }

  fun userDetailRequested(userDetailModel: UserDetailModel) {
    router.showUserDetails(userDetailModel.toString())
  }
}