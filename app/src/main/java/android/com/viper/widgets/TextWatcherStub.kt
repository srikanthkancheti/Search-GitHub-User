package android.com.viper.widgets

import android.text.Editable
import android.text.TextWatcher

abstract class TextWatcherStub : TextWatcher {
  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // empty
  }

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { // empty
  }

  override fun afterTextChanged(s: Editable) { // empty
  }
}