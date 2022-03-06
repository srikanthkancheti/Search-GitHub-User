package android.com.viper.model.network.interceptor

import android.com.viper.R.string
import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.StringRes

enum class ErrorCode(
  private val value: String,
  @param:StringRes private val resId: Int
) {

  NOT_FOUND("NOT_FOUND", string.error_message_not_found),
  AUTHENTICATION_ERROR("AUTHENTICATION_ERROR", string.something_wrong);

  companion object {
    @JvmStatic fun fromString(value: String?): ErrorCode? {
      for (code in values()) {
        if (code.value.equals(value, ignoreCase = true)) {
          return code
        }
      }
      return null
    }
  }

  fun getLocalizedError(@NonNull context: Context): String {
    return context.getString(resId);
    // return when (this) {
    //   CARD_LIMIT_EXCEED -> {
    //     val dailyLimit: String = NumberFormatUtils.formatAmount(BigDecimal(config.getTransferDaily()), 0)
    //     context.getString(resId, MultiCountryResource.Companion.getDefaultCurrency(), dailyLimit)
    //   }
    //   else -> context.getString(resId)
    // }
  }

}