package com.learn.ktornoteapp.ui.notes

import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentAllNotesBinding
import com.learn.ktornoteapp.ui.notes.adapter.NoteAdapter
import com.learn.ktornoteapp.ui.notes.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNotesFragment : Fragment(R.layout.fragment_all_notes) {
    private var _binding: FragmentAllNotesBinding? = null
    private val binding: FragmentAllNotesBinding?
        get() = _binding

    private val noteViewModel: NoteViewModel by activityViewModels()
    private lateinit var noteAdapter: NoteAdapter

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val note = noteAdapter.listNotes[position]
            noteViewModel.deleteNote(note.noteId)
            Snackbar.make(
                requireView(),
                "Note Deleted Successfully!",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction(
                    "Undo"
                ) {
                    noteViewModel.undoDelete(note)
                }
                show()
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX/2, dY, actionState, isCurrentlyActive)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllNotesBinding.bind(view)
        (activity as AppCompatActivity).setSupportActionBar(binding?.customToolbar)
        val menuHost: MenuHost = requireActivity()

        binding?.noteFab?.setOnClickListener {
            findNavController().navigate(R.id.action_allNotesFragment_to_newNoteFragment)
        }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.account -> {
                        findNavController().navigate(R.id.action_allNotesFragment_to_userInfoFragment)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupRecyclerView()
        subscribeToToken()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        noteAdapter.setOnItemClickListener {
            val toNewNoteFragment =
                AllNotesFragmentDirections.actionAllNotesFragmentToNewNoteFragment(it)
            findNavController().navigate(toNewNoteFragment)
        }

        binding?.noteRecyclerView?.apply {
            setHasFixedSize(true)
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(this)
        }
    }

    private fun subscribeToToken() = lifecycleScope.launch {
        noteViewModel.notes.collect { listNotes ->
            noteAdapter.setListNote(listNotes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}