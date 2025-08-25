package rocks.drnd.whereisivan.client.di

import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rocks.drnd.whereisivan.client.service.ActivityService
import rocks.drnd.whereisivan.client.viewmodel.ActivityListViewModel
import rocks.drnd.whereisivan.client.viewmodel.ActivityViewModel


val viewModelModule = module {
    single { ActivityService(get(), get()) }
    viewModel { ActivityViewModel(get()) }
    viewModel { ActivityListViewModel(get()) }

    factory {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

}