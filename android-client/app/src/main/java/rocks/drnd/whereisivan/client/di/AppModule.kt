package rocks.drnd.whereisivan.client.di

import org.koin.dsl.module

val appModule = module {
    includes(viewModelModule, networkModule, repositoryModule, persistenceModule)
}