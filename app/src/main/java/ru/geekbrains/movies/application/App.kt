package ru.geekbrains.movies.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.geekbrains.movies.di.Di

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    Di.viewModelModule(),
                    Di.apiModule(),
                    Di.repositoryModule(),
                    Di.useCasesModule()
                )
            )
        }
    }
}