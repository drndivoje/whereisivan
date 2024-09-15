package rocks.drnd.whereisivan.client.di

import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel


val viewModelModule = module {
    viewModel { ActivityViewModel(get(), get()) }
    factory {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }
}