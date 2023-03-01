package be.vives.ti.nfc_aanwezigheid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.ti.nfc_aanwezigheid.databinding.IdInListBinding

class IdListAdapter() : ListAdapter<Aanwezigheid, IdListAdapter.ViewHolder>(ToDoDiffCallback()){
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(var binding: IdInListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(
            toDo: Aanwezigheid,
            //clickListener: ToDoClickListener
        ) {
            binding.toDo = toDo
            //binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IdInListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}
class ToDoDiffCallback : DiffUtil.ItemCallback<Aanwezigheid>() {
    override fun areItemsTheSame(oldItem: Aanwezigheid, newItem: Aanwezigheid): Boolean {
        return false
    }
    override fun areContentsTheSame(oldItem: Aanwezigheid, newItem: Aanwezigheid): Boolean
    {
        return false
    }
}

class ToDoClickListener(val clickListener: (toDo: Aanwezigheid) -> Unit) {
    fun onClick(toDo: Aanwezigheid) = clickListener(toDo)
}