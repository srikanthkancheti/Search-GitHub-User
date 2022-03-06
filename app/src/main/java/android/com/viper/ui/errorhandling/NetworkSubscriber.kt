package android.com.viper.ui.errorhandling

import android.com.viper.model.network.exceptions.ErrorCodedProtocolException
import android.com.viper.model.network.exceptions.ProtocolException
import rx.Subscriber

class NetworkSubscriber<T>(private val subscriber: Subscriber<T>, private val networkView: NetworkView) : Subscriber<T>() {

  override fun onCompleted() {
    subscriber.onCompleted()
  }

  override fun onNext(t: T) {
    subscriber.onNext(t)
  }

  override fun onError(error: Throwable) {
    val errorMessage: String = when (error) {
      is ErrorCodedProtocolException -> error.errorModel.errorCode?.let {
        networkView.getErrorByErrorCode(it)
      } ?: networkView.getServerErrorMessage()
      is ProtocolException -> networkView.getServerErrorMessage()
      else -> networkView.getNetworkErrorMessage()
    }
    networkView.showIoError(errorMessage)
    subscriber.onError(error)
  }
}
