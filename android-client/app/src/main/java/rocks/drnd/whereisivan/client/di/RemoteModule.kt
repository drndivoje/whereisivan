package rocks.drnd.whereisivan.client.di


import io.ktor.client.HttpClient
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.network.httpClientAndroid

val remoteModule = module {
    single { provideRemoteService(get()) }
    single { provideHttpClient() }
}

fun provideHttpClient(): HttpClient {
    return httpClientAndroid
}

fun provideRemoteService(httpClient: HttpClient): ActivityApi {
    return ActivityApi(httpClient)
}