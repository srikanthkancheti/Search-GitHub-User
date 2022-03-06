package android.com.viper.model.network.interceptor

interface NetworkAvailabilityMonitor {
  fun isOnline(): Boolean
}