package ir.andromeda.movieado.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.databinding.ItemGenreDetailBinding

class GenreInDetailAdapter : RecyclerView.Adapter<GenreInDetailAdapter.Holder>() {

    var genres = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemGenreDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindGenre(genres[position])
    }

    override fun getItemCount(): Int = genres.size

    inner class Holder(private val binding: ItemGenreDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindGenre(title: String) {
            binding.tvTitle.text = title
        }
    }

}