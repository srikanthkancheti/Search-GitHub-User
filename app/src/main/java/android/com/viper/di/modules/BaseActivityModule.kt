package android.com.viper.di.modules

import android.app.Activity
import android.com.viper.di.scope.ActivityScope
import com.tbruyelle.rxpermissions.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class BaseActivityModule(private val activity: Activity) {

  @ActivityScope @Provides fun provideRxPermissions(): RxPermissions {
    return RxPermissions(activity)
  }
}