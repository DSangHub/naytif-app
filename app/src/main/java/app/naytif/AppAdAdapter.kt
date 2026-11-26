package app.naytif

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.naytif.databinding.ItemAppAdBinding

class AppAdAdapter(
    private val onDownload: (AppAd) -> Unit
) : RecyclerView.Adapter<AppAdAdapter.VH>() {

    private var items = listOf<AppAd>()

    fun submit(list: List<AppAd>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAppAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class VH(private val binding: ItemAppAdBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ad: AppAd) {
            binding.name.text = ad.name
            binding.developer.text = ad.developer
            binding.shortDesc.text = ad.shortDesc
            binding.release.text = "Released ${ad.releaseDate}"
            binding.downloads.text = "${ad.downloadCount} downloads"
            binding.paidBadge.visibility = if (ad.isPaidPlacement) android.view.View.VISIBLE else android.view.View.GONE
            binding.icon.setBackgroundColor(ad.iconColor)

            binding.btnDownload.setOnClickListener { onDownload(ad) }
            binding.btnProfile.setOnClickListener {
                // Simple profile expansion / toast for MVP
                android.widget.Toast.makeText(
                    binding.root.context,
                    "${ad.name} by ${ad.developer}\n${ad.fullDesc}",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
