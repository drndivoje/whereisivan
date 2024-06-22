package rocks.drnd.whereisivan.client.di


import io.ktor.client.HttpClient
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.network.httpClientAndroid

val networkModule = module {
    single { provideApiService(get()) }
    single { provideHttpClient() }
}

fun provideHttpClient(): HttpClient {
    return httpClientAndroid
}

fun provideApiService(httpClient: HttpClient): ActivityApi {
    return ActivityApi(httpClient)
}