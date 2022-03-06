package android.com.viper.util

import android.view.View

fun View.isVisible() = visibility == View.VISIBLE

fun View.setVisible(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.findRootView(): View {
  return rootView ?: this
}