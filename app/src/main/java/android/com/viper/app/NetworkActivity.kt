package android.com.viper.app

import android.app.Dialog
import android.com.viper.R
import android.com.viper.model.network.interceptor.ErrorCode
import android.com.viper.ui.errorhandling.NetworkView
import android.com.viper.util.DialogUtil.buildProgressDialog
import android.com.viper.util.UiUtils
import android.com.viper.util.cancelIfShowing
import android.com.viper.util.setVisible
import android.os.Bundle
import android.view.View

abstract class NetworkActivity : BaseActivity(), NetworkView {

  private var progressDialog: Dialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    progressDialog = buildProgressDialog(this)
  }

  override fun showIoError(errorMessage: String) {
    UiUtils.showWhiteSnackbar(findViewById<View>(android.R.id.content), errorMessage)
  }

  override fun getNetworkErrorMessage(): String {
    return resources.getString(R.string.no_internet_message_main_screen)
  }

  override fun getServerErrorMessage(): String {
    return resources.getString(R.string.error_not_2xx_or_3xx)
  }

  override fun getErrorByErrorCode(errorCode: ErrorCode): String {
    return errorCode.getLocalizedError(this)
  }

  override fun showMissingInternetMessage() {
    UiUtils.showWhiteSnackbar(
      findViewById<View>(android.R.id.content),
      getString(R.string.no_internet_message_main_screen)
    )
  }

  override fun showDefaultErrorMessage() {
    UiUtils.showWhiteSnackbar(findViewById<View>(android.R.id.content), getServerErrorMessage())
  }

  override fun showDefaultDisabledMessage() {
    showDefaultErrorMessage() // for now we are showing this one TODO: change it later
  }

  override fun setProgressVisibility(isVisible: Boolean) {
    progressDialog?.setVisible(isVisible)
    // findViewById<View>(android.R.id.content).setVisible(!isVisible) //To hide the page content while loading
  }

  override fun onDestroy() {
    super.onDestroy()
    progressDialog?.cancelIfShowing()
  }
}