package dev.holdbetter.feature_standings_impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.holdbetter.common.GameResult
import dev.holdbetter.feature_standings_impl.databinding.LastResultItemBinding

internal class LastResultAdapter(context: Context) : RecyclerView.Adapter<LastResultAdapter.ResultViewHolder>() {

    private var results = emptyList<GameResult>()

    private val winColor = context.getColor(R.color.last_result_win)
    private val loseColor = context.getColor(R.color.last_result_lose)
    private val drawColor = context.getColor(R.color.last_result_draw)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(
            LastResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(result: List<GameResult>) {
        this.results = result
        notifyDataSetChanged()
    }

    private fun GameResult.toColor() = when(this) {
        GameResult.WIN -> ColorStateList.valueOf(winColor)
        GameResult.LOSE -> ColorStateList.valueOf(loseColor)
        GameResult.DRAW -> ColorStateList.valueOf(drawColor)
    }

    inner class ResultViewHolder(
        private val binding: LastResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: GameResult) {
            binding.root.imageTintList = result.toColor()
        }
    }
}