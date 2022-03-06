package android.com.viper.ui.errorhandling

import android.com.viper.model.network.interceptor.ErrorCode
import com.dzaitsev.rxviper.ViewCallbacks

interface NetworkView : ViewCallbacks {

  fun getNetworkErrorMessage(): String

  fun getServerErrorMessage(): String

  fun showIoError(errorMessage: String)

  fun getErrorByErrorCode(errorCode: ErrorCode): String

  fun showMissingInternetMessage()

  fun showDefaultErrorMessage()

  fun showDefaultDisabledMessage()

  fun setProgressVisibility(isVisible: Boolean)
}
