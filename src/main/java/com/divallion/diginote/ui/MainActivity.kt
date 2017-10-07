package com.divallion.diginote.ui

import android.app.AlertDialog
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.divallion.diginote.App
import com.divallion.diginote.R
import com.divallion.diginote.model.NotesList
import com.divallion.diginote.ui.presenter.NotePresenter
import com.divallion.diginote.ui.presenter.ViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ViewHelper {

    @Inject lateinit var notePresenter: NotePresenter
    private val app: App get() = application as App
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app.appComponent().inject(this)
        setSupportActionBar(toolbar)
        noteList?.layoutManager = LinearLayoutManager(this)
        notePresenter.setNoteHelper(this)
        notePresenter.getNotes()

        noteList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    if (fabCreateNote.isShown) {
                        fabCreateNote.hide()
                    }
                }
                else if (dy <0) {
                    if (!fabCreateNote.isShown) {
                        fabCreateNote.show()
                    }
                }
            }
        })

        fabCreateNote.setOnClickListener {
            showNewNoteDialog()
        }
    }

    override fun showNotes() {
        if (NotesList.list.isEmpty()) {
            noteList.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        }
        else {
            noteList.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
            noteList.adapter = RViewAdapter()
        }
    }

    override fun notifyUpdate() {
        noteList.adapter = RViewAdapter()
    }

    private fun showNewNoteDialog(){
        val dialog = Dialog(this)
        dialog.setTitle("Add note")
        dialog.setContentView(R.layout.new_note)
        val nTitle = dialog.findViewById<EditText>(R.id.newTitle)
        val nDesc = dialog.findViewById<EditText>(R.id.newDesc)

        dialog.findViewById<Button>(R.id.nAdd).setOnClickListener {
            if (validateInput(nTitle, nDesc)) {
                notePresenter.addNote(nTitle.text.toString(), nDesc.text.toString())
                dialog.dismiss()
            }
        }

        dialog.findViewById<Button>(R.id.nCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validateInput(vararg editTexts: EditText) : Boolean {
        editTexts.forEach {
            if(it.text.toString().isEmpty()) {
                it.requestFocus()
                it.setError("Cannot be empty", null)
                return false
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mMenu = menu
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when {
            item?.itemId == R.id.delete -> {
                noteList.adapter = RViewDeleteAdapter()
                updateMenuForDelete()
            }
            item?.itemId == R.id.deleteAll -> deleteAllConfirm()
            item?.itemId == R.id.deleteDone -> {
                deleteSelectedItem()
                updateMenuNormal()
            }

        }

        return true
    }

    private fun deleteAllConfirm() {
        val aDialog = AlertDialog.Builder (this)
        aDialog.setTitle("Confirm?")
        aDialog.setMessage("Are you sure to delete all notes?")
        aDialog.setPositiveButton("Yes") { _, _ -> notePresenter.deleteAll() }
        aDialog.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel() }
        aDialog.create().show()
    }

    private fun deleteSelectedItem() {
        NotesList.list.forEach {
            if(it.isSelected){
                notePresenter.deleteNote(it.id)
            }
        }
    }

    private fun updateMenuForDelete() {
        mMenu?.findItem(R.id.delete)?.isVisible = false
        mMenu?.findItem(R.id.deleteAll)?.isVisible = false
        mMenu?.findItem(R.id.deleteDone)?.isVisible = true
    }

    private fun updateMenuNormal() {
        mMenu?.findItem(R.id.delete)?.isVisible = true
        mMenu?.findItem(R.id.deleteAll)?.isVisible = true
        mMenu?.findItem(R.id.deleteDone)?.isVisible = false
    }

    override fun onBackPressed() {
        if (noteList.adapter is RViewDeleteAdapter) {
            noteList.adapter = RViewAdapter()
            updateMenuNormal()
        }
        else
            super.onBackPressed()
    }

}
