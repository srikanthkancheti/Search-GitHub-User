package android.com.viper.di.component

import android.com.viper.di.modules.BaseActivityModule
import android.com.viper.di.scope.ActivityScope
import android.com.viper.ui.userDetail.GitHubUserDetailActivity
import android.com.viper.ui.home.SearchGitHubActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(BaseActivityModule::class)])
interface BaseActivityComponent {

  fun inject(activity: SearchGitHubActivity)

  fun inject(activity: GitHubUserDetailActivity)

}