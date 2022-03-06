package android.com.viper.model.network.interceptor

import android.com.viper.model.network.interceptor.ErrorCode.Companion.fromString
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ErrorModel(@field:SerializedName("message") var errorMessage: String) : Serializable {

  val errorCode: ErrorCode?
    get() = fromString(errorMessage)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true

    if (other == null || javaClass != other.javaClass) return false

    val that = other as ErrorModel

    return if (errorMessage.isNotEmpty()) errorMessage == that.errorMessage else false
  }

  override fun hashCode(): Int {
    return errorMessage.hashCode()
  }
}