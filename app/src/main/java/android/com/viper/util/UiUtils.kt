package android.com.viper.util

import android.com.viper.R
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

object UiUtils {

  @JvmStatic
  fun showWhiteSnackbar(view: View?, text: CharSequence) {
    createWhiteSnackBar(view, text)?.show()
  }

  private fun createWhiteSnackBar(view: View?, text: CharSequence): Snackbar? {
    view?.let {
      val snack = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
      val textView = snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
      textView.setTextColor(Color.WHITE)
      textView.maxLines = 3
      // textView.typeface = TypefaceManager.getInstance(textView.context).getTypeface(
      //   TypefaceManager.TYPEFACE_CODE_FIRA_SANS_REGULAR)
      snack.view.setBackgroundResource(R.color.grey_5f6673)
      return snack
    } ?: return null
  }

  @JvmStatic
  fun showSoftKeyboard(view: View): Boolean {
    val inputMethodManager = view.context.getSystemService(
      Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view.requestFocus()
    return inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
  }

  @JvmStatic
  fun hideSoftKeyboard(focus: View?): Boolean {
    if (focus != null) {
      val inputMethodManager = focus.context.getSystemService(
        Context.INPUT_METHOD_SERVICE) as InputMethodManager
      return inputMethodManager.hideSoftInputFromWindow(focus.windowToken, 0)
    }
    return false
  }

}