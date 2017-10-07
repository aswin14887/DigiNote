package com.divallion.diginote.ui.presenter

interface NotePresenter{
    fun setNoteHelper(helper: ViewHelper)
    fun getNotes()

    fun addNote(title: String, desc: String)

    fun deleteNote(id: Int)

    fun deleteAll()

    fun updateNote(id: Int, title: String, desc: String)
}