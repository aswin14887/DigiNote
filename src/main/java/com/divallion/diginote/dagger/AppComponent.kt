package com.divallion.diginote.dagger

import com.divallion.diginote.App
import com.divallion.diginote.ui.MainActivity
import com.divallion.diginote.ui.presenter.NotePresenterImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(app:App)

    fun inject(activity: MainActivity)

    fun inject(presenterImpl: NotePresenterImpl)

}