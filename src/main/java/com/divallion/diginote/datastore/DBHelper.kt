package com.divallion.diginote.datastore

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.divallion.diginote.model.Notes

class DBHelper(context: Context, dbName:String,
               factory:SQLiteDatabase.CursorFactory?, version: Int) :
        SQLiteOpenHelper(context, dbName, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DBStatements.createNoteTable())
    }

    fun insertNote(title: String, desc: String) = writableDatabase.execSQL(DBStatements.insertNote("'$title'", "'$desc'"))

    fun updateNote(id: Int, title: String, desc: String) = writableDatabase.execSQL(DBStatements.updateNote(id, "'$title'", "'$desc'"))

    fun deleteNote(id: Int) = writableDatabase.execSQL(DBStatements.deleteNote(id))

    fun deleteAllNotes() = writableDatabase.execSQL(DBStatements.deleteAllNotes())

    fun getAllNotes() : List<Notes> {
        val noteList: MutableList<Notes> = mutableListOf()

        val cursor : Cursor = writableDatabase.rawQuery(DBStatements.getAllNotes(), null)

        while (cursor.moveToNext()){

            val note = Notes( cursor.getInt(cursor.getColumnIndex(DBConstants.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.COL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.COL_DESC)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.COL_DATETIME)), false)
            noteList.add(note)

        }

        cursor.close()

        return noteList
    }

    override fun onUpgrade(db: SQLiteDatabase?, oVersion: Int, nVersion: Int) {}


}