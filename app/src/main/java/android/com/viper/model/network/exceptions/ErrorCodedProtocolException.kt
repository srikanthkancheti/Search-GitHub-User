package android.com.viper.model.network.exceptions

import android.com.viper.model.network.interceptor.ErrorModel

class ErrorCodedProtocolException(
  val errorCode: Int,
  message: String?,
  val errorModel: ErrorModel
) : ProtocolException(message)