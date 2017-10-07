package com.divallion.diginote.dagger

import com.divallion.diginote.App
import com.divallion.diginote.datastore.DBHelper
import com.divallion.diginote.ui.presenter.NotePresenter
import com.divallion.diginote.ui.presenter.NotePresenterImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(app: App){

    private val mApp : App = app

    @Provides
    @Singleton
    fun providesDBHelper() = DBHelper(mApp, "notes",null, 1)

    @Provides
    @Singleton
    fun providePresenter(): NotePresenter = NotePresenterImpl(mApp)

}