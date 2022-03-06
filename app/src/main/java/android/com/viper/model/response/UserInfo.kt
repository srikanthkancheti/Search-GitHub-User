package android.com.viper.model.response

interface UserInfo {
  var company: String?
  var blog: String?
  var bio: String?

  fun getUserCompany() : String? {
    return company
  }

  fun getUserBlog() : String? {
    return blog
  }

  fun getUserBio() : String? {
    return bio
  }
}