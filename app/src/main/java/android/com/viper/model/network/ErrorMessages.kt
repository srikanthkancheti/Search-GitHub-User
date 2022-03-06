package android.com.viper.model.network

import android.com.viper.R
import android.content.Context

interface ErrorMessages {
  fun getSomethingWrongHappened(): String
}

class ErrorMessagesUtil(private val context: Context) : ErrorMessages {
  override fun getSomethingWrongHappened(): String = context.getString(R.string.something_wrong)
}
