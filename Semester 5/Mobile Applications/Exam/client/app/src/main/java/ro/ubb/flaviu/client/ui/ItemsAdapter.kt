package ro.ubb.flaviu.client.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ubb.flaviu.client.data.models.DisplayItem
import ro.ubb.flaviu.client.databinding.ItemBinding

class ItemsAdapter(private val clickListener: ClickItemListener, val viewModel: HomeViewModel): RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>(){
    var items = emptyList<DisplayItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meal = items[position]
        holder.bind(clickListener, meal)
    }


    class ItemViewHolder private constructor(private val binding: ItemBinding, val viewModel: HomeViewModel): RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ClickItemListener, item: DisplayItem) {
            binding.item = item
//            binding.clickListener = ClickItemListener {
//                binding.quantity.isEnabled = true
//            }
            binding.button.setOnClickListener {
                val items = viewModel.items.value
                val newItems = items!!.toList()
                for (thing in newItems) {
                    if (thing.name == binding.name.text.toString())
                        thing.quantity = binding.quantity.text.toString()
                }
                viewModel.setItems(newItems)
//                binding.quantity.isEnabled = false
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: HomeViewModel): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding, viewModel)
            }
        }
    }
}

class ClickItemListener(val clickListener: (item: DisplayItem) -> Unit) {
    fun onClick(item: DisplayItem) = clickListener(item)
}
