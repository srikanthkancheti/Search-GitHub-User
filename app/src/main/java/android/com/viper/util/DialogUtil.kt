package android.com.viper.util

import android.app.Dialog
import android.com.viper.R
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager

object DialogUtil {

  @JvmStatic
  fun buildProgressDialog(context: Context): Dialog {
    val dialog = Dialog(context)
    val inflatedView = LayoutInflater.from(context).inflate(R.layout.layout_progress_large, null)
    dialog.setContentView(inflatedView)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    // dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    return dialog
  }
}

fun Dialog.setVisible(isShow: Boolean) {
  if (isShow) show() else cancel()
}

fun Dialog.cancelIfShowing() {
  if (isShowing) {
    cancel()
  }
}