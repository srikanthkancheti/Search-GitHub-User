package android.com.viper.app

import android.com.viper.SearchUserApplication
import android.com.viper.di.component.BaseActivityComponent
import android.com.viper.di.modules.BaseActivityModule
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class BaseActivity : AppCompatActivity() {

  private lateinit var activityComponent: BaseActivityComponent
  private lateinit var containerView: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Enable vector drawable compound views on prior lollipop
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    // Dagger setup
    activityComponent = SearchUserApplication.componentFromContext(this).plus(BaseActivityModule(this))
    setupComponent(activityComponent)
    containerView = findViewById<View>(android.R.id.content)
  }

  protected abstract fun setupComponent(component: BaseActivityComponent)

}