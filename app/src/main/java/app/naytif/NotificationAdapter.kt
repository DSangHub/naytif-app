package app.naytif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.naytif.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val onTag: (CapturedNotification, Importance) -> Unit,
    private val onFavorite: (CapturedNotification) -> Unit,
    private val onDelete: (CapturedNotification) -> Unit,
    private val onTimer: (CapturedNotification, Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.VH>() {

    private var items = listOf<CapturedNotification>()

    fun submit(list: List<CapturedNotification>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class VH(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(n: CapturedNotification) {
            binding.title.text = n.title.ifEmpty { "(no title)" }
            binding.text.text = n.text.ifEmpty { "(no text)" }
            binding.pkg.text = n.packageName.substringAfterLast('.')
            binding.importanceIndicator.setBackgroundColor(colorFor(n.importance))
            binding.fav.text = if (n.isFavorite) "★" else "☆"

            binding.fav.setOnClickListener { onFavorite(n) }
            binding.btnLow.setOnClickListener { onTag(n, Importance.LOW) }
            binding.btnNormal.setOnClickListener { onTag(n, Importance.NORMAL) }
            binding.btnHigh.setOnClickListener { onTag(n, Importance.HIGH) }
            binding.btnCritical.setOnClickListener { onTag(n, Importance.CRITICAL) }
            binding.btnDelete.setOnClickListener { onDelete(n) }
            binding.btnTimer.setOnClickListener { onTimer(n, 5) } // 5 min example

            binding.timerInfo.text = n.autoDeleteAt?.let {
                val mins = ((it - System.currentTimeMillis()) / 60000).toInt()
                if (mins > 0) "Deletes in ${mins}m" else "Deleting…"
            } ?: ""
        }

        private fun colorFor(imp: Importance): Int = when (imp) {
            Importance.LOW -> 0xFF9E9E9E.toInt()      // gray
            Importance.NORMAL -> 0xFF2196F3.toInt()  // blue
            Importance.HIGH -> 0xFFFF9800.toInt()    // orange
            Importance.CRITICAL -> 0xFFF44336.toInt() // red
        }
    }
}
