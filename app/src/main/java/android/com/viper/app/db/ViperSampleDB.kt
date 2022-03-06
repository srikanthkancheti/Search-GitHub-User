package android.com.viper.app.db

import android.com.viper.model.response.GitHubSearchResultItem
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.math.BigDecimal
import java.util.Date

@Database(
  entities = [GitHubSearchResultItem::class],
  version = 2,
  exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ViperSampleDB : RoomDatabase() {

  companion object {
    private const val DATABASE_NAME = "viperSample.db"

    fun create(context: Context): RoomDatabase.Builder<ViperSampleDB> {
      return Room.databaseBuilder(context, ViperSampleDB::class.java, DATABASE_NAME)
    }

    fun delete(context: Context) = context.deleteDatabase(DATABASE_NAME)
  }

  abstract fun points(): SearchResultsDao
}

class Converters {

  @TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time
  }

  @TypeConverter
  fun stringToBigDecimal(value: String?): BigDecimal? {
    return value?.toBigDecimal()
  }

  @TypeConverter
  fun bigDecimalToString(value: BigDecimal?): String? {
    return value?.toString()
  }
}