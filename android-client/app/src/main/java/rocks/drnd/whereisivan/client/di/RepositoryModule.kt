package rocks.drnd.whereisivan.client.di

import org.koin.dsl.module
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.LocalActivityDataSource
import rocks.drnd.whereisivan.client.repository.ActivityRepository

val repositoryModule = module {
    single { provideRemoteRepository(get()) }
}

fun provideRemoteRepository(apiService: ActivityApi): ActivityRepository {
    return ActivityRepository(LocalActivityDataSource(), apiService)
}
