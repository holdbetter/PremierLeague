package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.holdbetter.assets.assetsFont
import dev.holdbetter.assets.px
import dev.holdbetter.shared.feature_team_detail.DateHolder
import dev.holdbetter.shared.feature_team_detail_impl.databinding.CalendarItemBinding
import kotlinx.datetime.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.properties.Delegates

internal class DateAdapter(
    context: Context,
    private val onDateClickAction: (LocalDate) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    companion object {
        const val COLORED_PAYLOAD = "DATE_WITH_MATCH"
        const val SELECTED_PAYLOAD = "SELECTED_DATE"

        const val POSITION_MULTIPLIER = 20
    }

    private var dateColorWithMatch by Delegates.notNull<Int>()

    private var dateColorDefault by Delegates.notNull<Int>()
    private val numbersSpan = CustomTypefaceSpan(
        font = ResourcesCompat.getFont(context, assetsFont.raleway_regular),
        size = 20.px,
        isSameNumbers = true
    )

    private val dayWeekSpan = CustomTypefaceSpan(
        font = ResourcesCompat.getFont(context, assetsFont.raleway_bold),
        size = 11.px,
        isSameNumbers = false
    )

    private val asyncDiffer = OffsetAsyncListDiffer(this)

    val currentList: List<DateHolder>
        get() = asyncDiffer.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return with(LayoutInflater.from(parent.context)) {
            DateViewHolder(inflate(R.layout.calendar_item, parent, false))
        }
    }

    override fun onBindViewHolder(
        holder: DateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.onEach {
            val realPosition = position % currentList.count()
            val date = currentList[realPosition]
            when (it) {
                SELECTED_PAYLOAD -> holder.bindSelected(date)
                COLORED_PAYLOAD -> holder.bindColored(date)
            }
        }

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val realPosition = position % currentList.count()
        val date = currentList[realPosition]
        holder.bind(date)
    }

    override fun getItemCount() = if (currentList.isEmpty()) {
        0
    } else {
        currentList.size * POSITION_MULTIPLIER
    }

    fun submitData(
        calendar: List<DateHolder>,
        offset: Int,
        @ColorInt accentColor: Int,
        @ColorInt defaultTextColor: Int
    ) {
        this.dateColorWithMatch = accentColor
        this.dateColorDefault = defaultTextColor

        asyncDiffer.submitData(offset, calendar)
    }

    private fun Spannable.applyDateSpans(
        dayOfMonthCount: Int,
        dayOfWeekCount: Int,
        color: Int
    ): Spannable {
        setSpan(
            ForegroundColorSpan(color),
            0,
            count(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        setSpan(
            numbersSpan,
            0,
            dayOfMonthCount,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        setSpan(
            dayWeekSpan,
            count() - dayOfWeekCount,
            count(),
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        return this
    }

    inner class DateViewHolder(view: View) : ViewHolder(view) {

        private val binding = CalendarItemBinding.bind(view)

        fun bind(dateHolder: DateHolder) {
            val (date, isColored, isSelected) = dateHolder

            val dayWeek = date.dayOfWeek
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH)
                .substring(0..2)
                .lowercase()

            val dayOfMonthCount = date.dayOfMonth.toString().length
            val spannable = SpannableString("${date.dayOfMonth}\n$dayWeek")
                .applyDateSpans(
                    dayOfMonthCount = dayOfMonthCount,
                    dayOfWeekCount = dayWeek.length,
                    color = dateColor(isColored)
                )

            updateSelector(isSelected)

            binding.day.text = spannable
            binding.day.setTextColor(dateColor(isColored))
            binding.root.setOnClickListener { onDateClickAction(date) }
        }

        fun bindColored(dateHolder: DateHolder) {
            binding.day.setTextColor(dateColor(dateHolder.isColored))
        }

        fun bindSelected(dateHolder: DateHolder) {
            updateSelector(dateHolder.isSelected)
        }

        private fun updateSelector(isSelected: Boolean) {
            if (isSelected) {
                TextViewCompat.setCompoundDrawableTintList(
                    binding.day,
                    ColorStateList.valueOf(dateColorWithMatch)
                )
            } else {
                TextViewCompat.setCompoundDrawableTintList(
                    binding.day,
                    ColorStateList.valueOf(Color.TRANSPARENT)
                )
            }
        }

        private fun dateColor(isColored: Boolean): Int {
            return if (isColored) {
                dateColorWithMatch
            } else {
                dateColorDefault
            }
        }
    }
}