package android.com.viper.ui.userDetail

import android.com.viper.R
import android.com.viper.app.NetworkActivity
import android.com.viper.di.component.BaseActivityComponent
import android.com.viper.model.response.UserDetailModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_user_detail.*
import javax.inject.Inject

class GitHubUserDetailActivity : NetworkActivity(), GitHubUserDetailRouter, GitHubUserDetailViewCallBacks {

  private val userName: String by lazy { intent.getStringExtra(PRPO_USER_NAME) }
  @Inject lateinit var presenter: GitHubUserDetailPresenter
  private lateinit var userDetail: UserDetailModel

  companion object {
    private val PRPO_USER_NAME: String = ""

    fun start(context: Context, userName: String) {
      val intent = Intent(context, GitHubUserDetailActivity::class.java)
      intent.putExtra(PRPO_USER_NAME, userName)
      context.startActivity(intent)
    }
  }

  override fun setupComponent(component: BaseActivityComponent) = component.inject(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_detail)
    presenter.takeGitHubUserName(userName)

    listOf<View>(userAvatarImageView).forEach { v ->
      v.setOnClickListener(onClickedEvent)
    }
  }

  override fun onStart() {
    super.onStart()
    presenter.takeView(this)
    presenter.takeRouter(this)
  }

  override fun onStop() {
    presenter.dropView(this)
    presenter.dropRouter(this)
    super.onStop()
  }

  override fun showUserDetails(userDetails: String) {
    showIoError(userDetails)
  }

  override fun showUserDetailsInViews(userDetailModel: UserDetailModel) {
    userDetail = userDetailModel
    Glide.with(this)
      .load(userDetailModel.avatar_url)
      .diskCacheStrategy(DiskCacheStrategy.DATA)
      .into(userAvatarImageView)

    userCompany.text = Html.fromHtml(getString(R.string.company_name, userDetailModel.getUserCompany()))
    userBlog.text = Html.fromHtml(getString(R.string.user_blog_name, userDetailModel.getUserBlog()))
    userBiodata.text = Html.fromHtml(getString(R.string.user_bio, userDetailModel.getUserBio()))
    userLocation.text = Html.fromHtml(getString(R.string.user_location_name, userDetailModel.location))
    publicRepos.text = Html.fromHtml(getString(R.string.user_public_repos, userDetailModel.public_repos))
    followers.text = Html.fromHtml(getString(R.string.user_followers_number, userDetailModel.followers))
    following.text = Html.fromHtml(getString(R.string.user_following_number, userDetailModel.following))
  }

  private val onClickedEvent = View.OnClickListener { v ->
    when (v) {
      userAvatarImageView -> {
        presenter.userDetailRequested(userDetail)
      }
    }
  }
}