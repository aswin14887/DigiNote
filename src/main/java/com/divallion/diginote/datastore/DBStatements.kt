package com.divallion.diginote.datastore

object DBStatements{

    // create table if not exists notes_t( id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR NOT NULL,
    //              description VARCHAR NOT NULL, timestamp_c TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

    fun createNoteTable() = "create table if not exists "+ DBConstants.TABLE_NAME + "("+
            DBConstants.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DBConstants.COL_TITLE + " VARCHAR NOT NULL, "+
            DBConstants.COL_DESC + " VARCHAR NOT NULL, "+
            DBConstants.COL_DATETIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"

    // insert into notes_t( title , description) values (title, desc)

    fun insertNote(title: String, desc: String) = "insert into "+ DBConstants.TABLE_NAME+"("+
            DBConstants.COL_TITLE+", "+DBConstants.COL_DESC+") values("+title+", "+desc+")"

    // delete from notes_t where id = 2

    fun deleteNote(id: Int) = "delete from "+DBConstants.TABLE_NAME+" where "+DBConstants.COL_ID+" = "+id

    // delete from notes_t

    fun deleteAllNotes() = "delete from "+DBConstants.TABLE_NAME

    // update notes_t set title = title, desc = desc where id = id

    fun updateNote(id: Int, title: String, desc: String) = "update "+DBConstants.TABLE_NAME+
            " set "+DBConstants.COL_TITLE+" = "+title+", "+DBConstants.COL_DESC+" = "+desc+" where "+DBConstants.COL_ID+" = "+id

    // select * from notes_t
    fun getAllNotes() = "select * from "+DBConstants.TABLE_NAME

}