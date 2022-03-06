package android.com.viper.model.network

import android.com.viper.model.response.UserDetailModel
import android.com.viper.model.response.GitHubSearchResponseModel
import android.content.Context
import retrofit2.Retrofit
import rx.Observable

open class GitHubSearchRepository(
  retrofit: Retrofit,
  apiClass: Class<GitHubSearchApi>,
  val context: Context
): Repository {

  private val ORDER = "desc"
  private val restAPI: GitHubSearchApi = retrofit.create(apiClass)

  override fun getGitHubSearchData(language: String, pageSize: Int, page: Int): Observable<GitHubSearchResponseModel> {
    return restAPI.getSearchResults(language, pageSize, page, ORDER)
  }

  override fun getUserDetailData(userName: String?): Observable<UserDetailModel> {
    return restAPI.getUserDetails(userName)
  }
}