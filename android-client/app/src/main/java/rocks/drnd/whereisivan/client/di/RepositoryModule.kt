package rocks.drnd.whereisivan.client.di

import org.koin.dsl.module
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository

val repositoryModule = module {
    single { LocalActivityRepository(get(), get()) }
    single { RemoteActivityRepository(get()) }
}


