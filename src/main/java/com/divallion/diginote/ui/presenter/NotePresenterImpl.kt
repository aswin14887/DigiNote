package com.divallion.diginote.ui.presenter

import android.content.Context
import com.divallion.diginote.App
import com.divallion.diginote.datastore.DBHelper
import com.divallion.diginote.model.NotesList
import javax.inject.Inject

class NotePresenterImpl(private val context: Context) : NotePresenter {

    @Inject lateinit var dbHelper: DBHelper

    private lateinit var mHelper: ViewHelper

    private val app: App get() = context.applicationContext as App

    override fun setNoteHelper(helper: ViewHelper) {
        app.appComponent().inject(this)
        mHelper = helper
    }

    override fun getNotes() {
        getAllNotes()
        mHelper.showNotes()
    }

    override fun addNote(title: String, desc: String) {
        dbHelper.insertNote(title, desc)
        getAllNotes()
        mHelper.notifyUpdate()
    }

    override fun deleteNote(id: Int) {
        dbHelper.deleteNote(id)
        getAllNotes()
        mHelper.notifyUpdate()
    }

    override fun deleteAll() {
        dbHelper.deleteAllNotes()
       getAllNotes()
        mHelper.notifyUpdate()
    }

    override fun updateNote(id: Int, title: String, desc: String) {
        dbHelper.updateNote(id, title, desc)
        getAllNotes()
        mHelper.notifyUpdate()
    }

    private fun getAllNotes() {
        NotesList.list = dbHelper.getAllNotes()
    }

}

