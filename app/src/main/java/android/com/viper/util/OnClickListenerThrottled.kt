package android.com.viper.util

import android.view.View
import android.view.View.OnClickListener
import java.util.WeakHashMap

private const val defaultThrottleDelayMillis = 500

/**
 * Convenient extension to use [OnClickListenerThrottled]
 */
fun View.setOnClickListenerThrottled(throttleDelayMillis: Int = defaultThrottleDelayMillis, listener: (View) -> Unit) {
  setOnClickListener(OnClickListenerThrottled(throttleDelayMillis, listener))
}

/**
 * Class is [View.OnClickListener] implementation with support of clicks throttling.
 *
 * It allows filter out situations, when use can rapidly click on some button and cause unwanted side effects,
 * e.g. double send network request or open same navigation two times
 */
class OnClickListenerThrottled @JvmOverloads constructor(
  private val delayMillis: Int = defaultThrottleDelayMillis,
  private val listener: (View) -> Unit
) : OnClickListener {

  private val lastClickTimeMap: MutableMap<View, Long> = WeakHashMap()

  override fun onClick(v: View) {
    val lastClickTime = lastClickTimeMap[v] ?: 0L
    val currentTime = System.currentTimeMillis()

    if (currentTime - lastClickTime > delayMillis) {
      listener(v)
      lastClickTimeMap[v] = currentTime
    }
  }
}