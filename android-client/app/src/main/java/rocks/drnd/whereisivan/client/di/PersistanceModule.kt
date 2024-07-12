package rocks.drnd.whereisivan.client.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.datasource.AppDatabase

val persistenceModule = module {
    val DATABASE_NAME = "activity-database"
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
    single { get<AppDatabase>().activityDao() }
    single { get<AppDatabase>().waypointDao() }

}