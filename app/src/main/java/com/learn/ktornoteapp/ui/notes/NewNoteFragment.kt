package com.learn.ktornoteapp.ui.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentNewNoteBinding
import com.learn.ktornoteapp.ui.notes.viewmodel.NoteViewModel
import com.learn.ktornoteapp.utils.DateHelper.getCurrentDate

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {
    private var _binding: FragmentNewNoteBinding? = null
    private val binding: FragmentNewNoteBinding?
        get() = _binding

    private val noteViewModel: NoteViewModel by activityViewModels()
    private val args: NewNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewNoteBinding.bind(view)

        noteViewModel.oldNote = args.note
        noteViewModel.oldNote?.noteTitle?.let {
            binding?.etNewNote?.setText(it)
        }

        noteViewModel.oldNote?.description?.let {
            binding?.etNewNoteDescription?.setText(it)
        }

        binding?.date?.isVisible = noteViewModel.oldNote != null
        noteViewModel.oldNote?.date?.let {
            binding?.date?.text = getCurrentDate()
        }
    }

    override fun onPause() {
        super.onPause()
        if (noteViewModel.oldNote == null) {
            createNote()
        } else {
            updateNote()
        }
    }

    private fun createNote() {
        val noteTitle = binding?.etNewNote?.text?.toString()?.trim()
        val description = binding?.etNewNoteDescription?.text?.toString()?.trim()

        if(noteTitle.isNullOrEmpty() && description.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Note is Empty!", Toast.LENGTH_SHORT).show()
            return
        }

        noteViewModel.createNote(noteTitle,description)
    }

    private fun updateNote() {
        val noteTitle = binding?.etNewNote?.text.toString().trim()
        val description = binding?.etNewNoteDescription?.text.toString().trim()

        if (noteTitle.isEmpty() && description.isEmpty()) {
            return
        }

        noteViewModel.updateNote(noteTitle, description)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}