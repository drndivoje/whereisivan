package rocks.drnd.whereisivan.client

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import rocks.drnd.whereisivan.client.di.appModule

class WhereIsIvanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WhereIsIvanApplication)
            modules(appModule)
        }
    }
}