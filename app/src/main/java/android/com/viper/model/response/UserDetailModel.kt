package android.com.viper.model.response

import com.google.gson.annotations.SerializedName

data class UserDetailModel(
  @SerializedName("avatar_url") var avatar_url: String,
  @SerializedName("company") override var company: String?,
  @SerializedName("bio") override var bio: String?,
  @SerializedName("blog") override var blog: String?,
  @SerializedName("location") var location: String,
  @SerializedName("followers") var followers: String,
  @SerializedName("following") var following: String,
  @SerializedName("public_repos") var public_repos: String
) : UserInfo {
  override fun getUserCompany(): String? {
    return if (this.company.isNullOrEmpty()) "No company found" else company
  }

  override fun getUserBlog(): String? {
    return if (this.blog.isNullOrEmpty()) "No blog found" else blog
  }

  override fun getUserBio(): String? {
    return if (this.bio.isNullOrEmpty()) "No blog found" else bio
  }
}