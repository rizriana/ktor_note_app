package com.learn.ktornoteapp.ui.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.data.local.local_model.LocalNote
import com.learn.ktornoteapp.databinding.ItemNoteBinding

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var onItemCLickListener: ((LocalNote) -> Unit)? = null
    private var listNotes = emptyList<LocalNote>()

    fun setOnItemClickListener(listener: (LocalNote) -> Unit) {
        onItemCLickListener = listener
    }

    fun setListNote(listNotes: List<LocalNote>) {
        val diffCallback = com.learn.ktornoteapp.utils.DiffUtil(this.listNotes, listNotes)
        val diffResult = calculateDiff(diffCallback)
        this.listNotes = listNotes
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = listNotes.size

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: LocalNote) {
            binding.apply {
                noteTitle.isVisible = note.noteTitle != null
                noteDescription.isVisible = note.description != null

                note.noteTitle?.let {
                    noteTitle.text = it
                }

                note.description?.let {
                    noteDescription.text = it
                }

                noteSync.setBackgroundResource(
                    if (note.connected) R.drawable.synced
                    else R.drawable.not_sync
                )

                root.setOnClickListener {
                    onItemCLickListener?.invoke(note)
                }
            }
        }
    }
}