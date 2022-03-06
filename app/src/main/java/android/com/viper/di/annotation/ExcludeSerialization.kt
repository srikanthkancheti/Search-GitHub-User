package android.com.viper.di.annotation

import kotlin.annotation.Retention

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ExcludeSerialization
