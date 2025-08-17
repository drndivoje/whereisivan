package rocks.drnd.whereisivan.client.di


import io.ktor.client.HttpClient
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.BuildConfig
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
    BuildConfig.REMOTE_BASE_HOST.let { remoteHost ->
        if (remoteHost.isNotEmpty()) {
            return ActivityApi(httpClient, remoteHost)
        }
    }
    throw IllegalStateException("Remote host is not configured. Please set the REMOTE_BASE_HOST in BuildConfig.")
}