package android.com.viper.model.network.interceptor

import android.com.viper.model.network.ErrorMessages
import android.com.viper.model.network.exceptions.ErrorCodedProtocolException
import android.com.viper.model.network.exceptions.ProtocolException
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorResponseInterceptor @Inject internal constructor(
  private val gson: Gson,
  private val errorMessages: ErrorMessages
) : Interceptor {
  @Throws(IOException::class) override fun intercept(chain: Chain): Response {
    val response = chain.proceed(chain.request())
    return if (response.isSuccessful
      || response.code == HttpURLConnection.HTTP_NOT_FOUND
      || response.code == HttpURLConnection.HTTP_UNAUTHORIZED
      || response.isRedirect
    ) {
      response
    } else {
      if (response.body == null) {
        throw ProtocolException(errorMessages.getSomethingWrongHappened())
      }
      val string = response.body!!.string()
      val errorModel = gson.fromJson(string, ErrorModel::class.java)
      if (errorModel == null) { // in some rare cases we can have response for failed request, e.g. no internet and no cached response
        if (!response.isSuccessful) {
          throw IOException(string)
        }
        throw ProtocolException(errorMessages.getSomethingWrongHappened())
      } else {
        throw ErrorCodedProtocolException(response.code, errorMessages.getSomethingWrongHappened(), errorModel)
      }
    }
  }
}