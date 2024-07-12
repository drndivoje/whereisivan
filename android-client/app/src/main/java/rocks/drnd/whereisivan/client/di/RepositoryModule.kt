package rocks.drnd.whereisivan.client.di

import org.koin.dsl.module
import rocks.drnd.whereisivan.client.repository.ActivityRepository
import rocks.drnd.whereisivan.client.repository.WaypointRepository

val repositoryModule = module {
    single { ActivityRepository(get(), get()) }
    single { WaypointRepository(get(), get()) }
}


