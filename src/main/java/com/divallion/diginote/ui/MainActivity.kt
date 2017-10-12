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
import android.widget.Toast
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
            notifyUpdate()
        }
    }

    override fun notifyUpdate() {
        noteList.adapter = RViewAdapter({
            showEditNoteDialog(it.id, it.title, it.desc)
        })
    }

    private fun showEditNoteDialog(id:Int, oldTitle: String, oldDesc: String) {
        val dialog = Dialog(this)
        dialog.setTitle(getString(R.string.editNote_title))
        dialog.setContentView(R.layout.new_note)
        val nTitle = dialog.findViewById<EditText>(R.id.newTitle)
        val nDesc = dialog.findViewById<EditText>(R.id.newDesc)

        nTitle.setText(oldTitle); nDesc.setText(oldDesc)
        val butn = dialog.findViewById<Button>(R.id.nAdd)
        butn.text = getString(R.string.done)
        butn.setOnClickListener {
            if (validateInput(nTitle, nDesc)) {
                notePresenter.updateNote(id, nTitle.text.toString(), nDesc.text.toString())
                notifyUpdate()
                Toast.makeText(this@MainActivity, getString(R.string.edit_confirm), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.findViewById<Button>(R.id.nCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showNewNoteDialog(){
        val dialog = Dialog(this)
        dialog.setTitle(getString(R.string.newNote_title))
        dialog.setContentView(R.layout.new_note)
        val nTitle = dialog.findViewById<EditText>(R.id.newTitle)
        val nDesc = dialog.findViewById<EditText>(R.id.newDesc)

        dialog.findViewById<Button>(R.id.nAdd).setOnClickListener {
            if (validateInput(nTitle, nDesc)) {
                notePresenter.addNote(nTitle.text.toString(), nDesc.text.toString())
                showNotes()
                Toast.makeText(this@MainActivity, getString(R.string.confirm_new_note), Toast.LENGTH_SHORT).show()
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
            if(it.text.toString().trim().isEmpty()) {
                it.requestFocus()
                it.setError(getString(R.string.empty_error_msg), null)
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
            item?.itemId == R.id.deleteAll -> { deleteAllConfirm(); showNotes()}
            item?.itemId == R.id.deleteDone -> {
                deleteSelectedItem()
                updateMenuNormal()
            }

        }

        return true
    }

    private fun deleteAllConfirm() {
        val aDialog = AlertDialog.Builder (this)
        aDialog.setTitle(getString(R.string.deleteAll_title))
        aDialog.setMessage(getString(R.string.deleteAll_msg))
        aDialog.setPositiveButton(getString(R.string.yes)) { _, _ -> run { notePresenter.deleteAll(); showNotes()} }
        aDialog.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog?.cancel() }
        aDialog.create().show()
    }

    private fun deleteSelectedItem() {
        NotesList.list.forEach {
            if(it.isSelected){
                notePresenter.deleteNote(it.id)
            }
        }
        if (!fabCreateNote.isShown) {
            showNotes()
            fabCreateNote.show()
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
        showNotes()
    }

    override fun onBackPressed() {
        if (noteList.adapter is RViewDeleteAdapter) {
            showNotes()
            updateMenuNormal()
        }
        else
            super.onBackPressed()
    }

}
