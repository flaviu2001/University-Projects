package ro.ubb.flaviu.mealplanner.ui.mealList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ubb.flaviu.mealplanner.data.models.Meal
import ro.ubb.flaviu.mealplanner.databinding.MealCardBinding

class MealsAdapter(private val clickListener: ClickMealListener): RecyclerView.Adapter<MealsAdapter.MealViewHolder>(){
    var meals = emptyList<Meal>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = meals.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(clickListener, meal)
    }


    class MealViewHolder private constructor(private val binding: MealCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ClickMealListener, item: Meal) {
            binding.meal = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MealViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MealCardBinding.inflate(layoutInflater, parent, false)
                return MealViewHolder(binding)
            }
        }
    }
}

class ClickMealListener(val clickListener: (mealId: String) -> Unit) {
    fun onClick(meal: Meal) = clickListener(meal._id)
}
