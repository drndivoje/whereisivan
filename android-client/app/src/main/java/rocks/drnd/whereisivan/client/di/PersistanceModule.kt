package rocks.drnd.whereisivan.client.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.datasource.AppDatabase

val persistenceModule = module {
    val databaseName = "activity-database"
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            databaseName
        ).build()
    }
    single { get<AppDatabase>().activityDao() }
    single { get<AppDatabase>().waypointDao() }

}