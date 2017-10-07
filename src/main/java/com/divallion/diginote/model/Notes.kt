package com.divallion.diginote.model

data class Notes(
        var id: Int,
        var title: String,
        var desc: String,
        var dateTime: String,
        var isSelected: Boolean
)

object NotesList {
    lateinit var list: List<Notes>
}