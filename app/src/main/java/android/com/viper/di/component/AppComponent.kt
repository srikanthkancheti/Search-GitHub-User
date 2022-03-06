package android.com.viper.di.component

import android.com.viper.SearchUserApplication
import android.com.viper.di.modules.AppModule
import android.com.viper.di.modules.BaseActivityModule
import android.com.viper.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AppModule::class,
    NetworkModule::class
  ]
)
interface AppComponent {

  fun plus(activityModule: BaseActivityModule): BaseActivityComponent

  fun inject(app: SearchUserApplication)
}
