package com.example.weatherApp.baseapplication

import android.app.Application
import com.example.weatherApp.data.db.database.WeatherDatabase
import com.example.weatherApp.data.network.DarkskyWeatherApiService
import com.example.weatherApp.data.network.connectivity.*
import com.example.weatherApp.data.repository.WeatherRepository
import com.example.weatherApp.data.repository.WeatherRepositoryImpl
import com.example.weatherApp.ui.weatherSearchResult.WeatherSearchActivityViewmodelFactory
import com.example.weatherApp.ui.weatherSearchResult.weatherSearchResult.WeatherSearchResultViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastDI : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@ForecastDI))

        bind() from singleton { WeatherDatabase(instance())}
        bind() from singleton { instance<WeatherDatabase>().getCurrentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { DarkskyWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl() }
        bind() from singleton { instance<WeatherDatabase>().getWeatherLocationDao() }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(), instance()) }
        bind() from provider { WeatherSearchResultViewModelFactory(instance()) }
        bind() from provider { WeatherSearchActivityViewmodelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}