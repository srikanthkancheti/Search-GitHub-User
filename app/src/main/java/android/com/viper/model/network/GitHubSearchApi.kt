package android.com.viper.model.network

import android.com.viper.model.response.UserDetailModel
import android.com.viper.model.response.GitHubSearchResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

const val PATH_ARGUMENT = "path_arg"

const val PATH_QUERY_REPOSITORIES = "search/users"
const val PATH_USER = "users/{$PATH_ARGUMENT}"
const val QUERY_USER = "q"
const val QUERY_LIMIT = "per_page"
const val QUERY_PAGE = "page"
const val QUERY_ORDER = "order"

interface GitHubSearchApi {
  @GET(PATH_QUERY_REPOSITORIES)
  fun getSearchResults(
    @Query(QUERY_USER) queryString: String,
    @Query(QUERY_LIMIT) limit: Int,
    @Query(QUERY_PAGE) page: Int,
    @Query(QUERY_ORDER) order: String
  ): Observable<GitHubSearchResponseModel>

  @GET(PATH_USER)
  fun getUserDetails(@Path(PATH_ARGUMENT) userName: String?): Observable<UserDetailModel>
}
