package android.com.viper.ui.errorhandling

import androidx.annotation.CallSuper
import com.dzaitsev.rxviper.Router
import com.dzaitsev.rxviper.ViperPresenter
import rx.Subscriber
import rx.functions.Action0
import rx.functions.Action1

/**
 * Confluence guideline @link https://bigpay.atlassian.net/wiki/spaces/BP/pages/652247172/Android+Coding+Guidelines
 */
abstract class NetworkPresenter<V : NetworkView, R : Router> :
  ViperPresenter<V, R>() {

  /**
   * Indicates visibility of progress bar and content while network request
   * Default state is progress dialog dismissed and content visible
   */
  private var isProgressActive = false

  @CallSuper
  override fun onTakeView(view: V) {
    super.onTakeView(view)
    view.setProgressVisibility(isProgressActive)
  }

  /**
   * Set the progress bar and content visibility depends on state
   * @param isActive if true will show progress and hide content and the other way around
   * @param isContinueProgressWhileNavigating if true will not call changing progress visibility state
   */
  @JvmOverloads
  protected fun setProgressActive(isActive: Boolean, isContinueProgressWhileNavigating: Boolean = false) {
    isProgressActive = isActive
    if (!isContinueProgressWhileNavigating) {
      view.setProgressVisibility(isActive)
    }
  }

  /**
   * Creating network subscriber with onNext, onError and onComplete actions
   */
  @JvmOverloads
  protected fun <T> makeNetworkSubscriber(
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit = {}
  ): NetworkSubscriber<T> =
    buildNetworkSubscriber(
      Action1 { onNext(it) },
      Action1 { onError(it) },
      Action0 { onComplete() }
    )

  private fun <T> buildNetworkSubscriber(
    onNext: Action1<T>,
    onError: Action1<Throwable>,
    onComplete: Action0
  ): NetworkSubscriber<T> {

    return NetworkSubscriber(object : Subscriber<T>() {
      override fun onCompleted() {
        onComplete.call()
      }

      override fun onError(e: Throwable) {
        onError.call(e)
      }

      override fun onNext(result: T) {
        onNext.call(result)
      }
    }, view)
  }
}
