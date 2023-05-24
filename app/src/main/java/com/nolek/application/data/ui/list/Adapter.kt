package com.nolek.application.data.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nolek.application.data.model.PLCBundle
import com.nolek.application.databinding.ListItemBinding

class Adapter(
    var items: List<PLCBundle>,
    private val layoutInflater: LayoutInflater,
    private val onItemClickListener: (PLCBundle) -> Unit
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemBinding,
        val onItemClickListener: (PLCBundle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(MES: PLCBundle) {
            binding.root.text = MES.index
            binding.root.isSelected = false
            binding.root.setOnClickListener {
                onItemClickListener(MES)
                binding.root.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(layoutInflater)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
    }

    override fun getItemCount(): Int = items.size

}