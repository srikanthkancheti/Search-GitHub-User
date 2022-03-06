package android.com.viper.di.modules

import android.app.Application
import android.com.viper.app.db.ViperSampleDB
import android.com.viper.di.annotation.IoThread
import android.com.viper.di.annotation.UiThread
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteException
import android.text.Editable.Factory
import androidx.room.RoomDatabase.Builder
import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

  @Provides @Singleton fun provideApplication(): Application {
    return application
  }

  @Provides @Singleton fun providesContext(): Context {
    return application.applicationContext
  }

  @Provides @UiThread fun provideUiThread(): Scheduler {
    return AndroidSchedulers.mainThread()
  }

  @Provides @IoThread fun provideIoThread(): Scheduler {
    return Schedulers.io()
  }

  @Provides fun provideCompositeSubscription(): CompositeSubscription {
    return CompositeSubscription()
  }

  @Provides @UiThread fun provideUiThreadRx2(): io.reactivex.Scheduler {
    return io.reactivex.android.schedulers.AndroidSchedulers.mainThread()
  }

  @Provides @IoThread fun provideIoThreadRx2(): io.reactivex.Scheduler {
    return io.reactivex.schedulers.Schedulers.io()
  }

  @Provides @Singleton fun providesSharedPreferences(): SharedPreferences {
    return application.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
  }

  @Provides @Singleton fun provideTransactionDatabase(
    context: Context
  ): ViperSampleDB {
    val transactionsDbBuilder: Builder<ViperSampleDB> = ViperSampleDB.create(context)

    val db: ViperSampleDB = transactionsDbBuilder
      .fallbackToDestructiveMigration()
      .build()
    try { // Checking the database encryption key is valid and we can open database.
      db.openHelper.readableDatabase
    } catch (e: SQLiteException){
      // SQLiteException - for encryption issues.
      // If we catch the exception - delete database and recreate it.
      ViperSampleDB.delete(context)
      return provideTransactionDatabase(context)
    } catch (e: IllegalStateException) {
      // IllegalStateException - for database schema version issues.
      // If we catch the exception - delete database and recreate it.
      ViperSampleDB.delete(context)
      return provideTransactionDatabase(context)
    }
    return db
  }
}
