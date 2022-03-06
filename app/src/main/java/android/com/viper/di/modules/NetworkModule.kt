package android.com.viper.di.modules

import android.app.Application
import android.com.viper.BuildConfig
import android.com.viper.di.annotation.ExcludeDeserialization
import android.com.viper.di.annotation.ExcludeSerialization
import android.com.viper.model.network.GitHubSearchRepository
import android.com.viper.model.network.GitHubSearchApi
import android.com.viper.model.network.ErrorMessages
import android.com.viper.model.network.ErrorMessagesUtil
import android.com.viper.model.network.Repository
import android.com.viper.model.network.interceptor.ErrorResponseInterceptor
import android.com.viper.model.network.interceptor.NetworkAvailabilityMonitor
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule::class])
open class NetworkModule {

  val TIME_OUT_SECONDS = 60

  @Provides @Singleton fun provideSharedPreferences(app: Application): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(app)
  }

  @Provides
  @Named("SerializationExclusionStrategy")
  internal fun provideSerializationExclusionStrategy(): ExclusionStrategy {
    return object : ExclusionStrategy {
      override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
      }

      override fun shouldSkipField(field: FieldAttributes): Boolean {
        return field.getAnnotation(ExcludeSerialization::class.java) != null
      }
    }
  }

  @Provides
  @Named("DeserializationExclusionStrategy")
  internal fun provideDeserializationExclusionStrategy(): ExclusionStrategy {
    return object : ExclusionStrategy {
      override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
      }

      override fun shouldSkipField(field: FieldAttributes): Boolean {
        return field.getAnnotation(ExcludeDeserialization::class.java) != null
      }
    }
  }

  @Provides
  @Singleton
  internal fun provideGson(
    @Named("SerializationExclusionStrategy") serializationExclusion: ExclusionStrategy,
    @Named("DeserializationExclusionStrategy") deserializationExclusion: ExclusionStrategy
  ): Gson {
    return GsonBuilder()
      .addSerializationExclusionStrategy(serializationExclusion)
      .addDeserializationExclusionStrategy(deserializationExclusion)
      .create()
  }

  @Provides open fun provideNetworkMonitor(context: Context): NetworkAvailabilityMonitor {
    val appContext = context.applicationContext
    return object : NetworkAvailabilityMonitor {
      override fun isOnline(): Boolean {
        var isConnected = false
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        isConnected = activeNetwork != null && activeNetwork.isConnected
        return isConnected
      }
    }
  }

  @Provides @Singleton fun provideOkHttpClient(
    interceptors: List<@JvmSuppressWildcards Interceptor>,
    @NetworkInterceptors networkInterceptors: List<@JvmSuppressWildcards Interceptor>,
    cache: Cache
  ): OkHttpClient {

    val builder = Builder()

    Observable.from<Interceptor>(interceptors)
      .forEach { interceptor -> builder.addInterceptor(interceptor) }

    Observable.from(networkInterceptors)
      .forEach { interceptor -> builder.addNetworkInterceptor(interceptor) }

    return builder
      .connectTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
      .readTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
      .cache(cache)
      .build()
  }

  @Provides @Singleton fun provideHttpCache(app: Application): Cache {
    val cacheSize = 10 * 10 * 1024

    return Cache(app.cacheDir, cacheSize.toLong())
  }

  @Provides @Singleton fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.ENDPOINT)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .client(okHttpClient)
      .build()
  }

  @Provides @Singleton fun provideRepository(retrofit: Retrofit, context: Context): Repository {
    return GitHubSearchRepository(retrofit, GitHubSearchApi::class.java, context)
  }

  @Provides fun provideInterceptors(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    errorResponseInterceptor: ErrorResponseInterceptor
  ): List<Interceptor> {
    return listOf(httpLoggingInterceptor, errorResponseInterceptor)
  }

  @Provides @NetworkInterceptors fun provideNetworkInterceptors(): List<Interceptor> {
    return listOf(StethoInterceptor())
  }

  @Provides fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(
      if (BuildConfig.DEBUG) BODY else NONE
    )
    return interceptor
  }

  @Provides @Singleton fun provideErrorMessages(context: Context): ErrorMessages {
    return ErrorMessagesUtil(context)
  }
}
