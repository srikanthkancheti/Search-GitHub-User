package android.com.viper.model.network

import android.com.viper.model.response.UserDetailModel
import android.com.viper.model.response.GitHubSearchResponseModel
import rx.Observable

interface Repository {
  fun getGitHubSearchData(language: String, pageSize: Int, page: Int): Observable<GitHubSearchResponseModel>
  fun getUserDetailData(userName: String?): Observable<UserDetailModel>
}