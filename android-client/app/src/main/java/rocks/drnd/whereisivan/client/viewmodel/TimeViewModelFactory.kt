package rocks.drnd.whereisivan.client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient

class TimeViewModelFactory ( private val locationClient: FusedLocationProviderClient) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.getConstructor(FusedLocationProviderClient::class.java)
            .newInstance(locationClient)
}