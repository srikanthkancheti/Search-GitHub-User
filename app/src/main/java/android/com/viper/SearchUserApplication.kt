package android.com.viper

import android.com.viper.di.component.AppComponent
import android.com.viper.di.component.DaggerAppComponent
import android.com.viper.di.modules.AppModule
import android.com.viper.di.modules.NetworkModule
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import java.util.Locale

open class SearchUserApplication : MultiDexApplication() {

  lateinit var appComponent: AppComponent

  override fun attachBaseContext(base: Context) {
    val configuration = base.resources.configuration
    Locale.setDefault(Locale.US)
    configuration.setLocale(Locale.US)
    super.attachBaseContext(base.createConfigurationContext(configuration))
  }

  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this)
    buildAppComponent();
  }

  private fun buildAppComponent() {
    appComponent = DaggerAppComponent.builder()
      .networkModule(NetworkModule())
      .appModule(AppModule(this))
      .build()
    appComponent.inject(this)
  }

  companion object {

    operator fun get(context: Context): SearchUserApplication {
      return context.applicationContext as SearchUserApplication
    }

    fun componentFromContext(context: Context): AppComponent {
      return get(context).appComponent
    }
  }
}
