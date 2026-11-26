package app.naytif

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.naytif.databinding.ActivityAppsBinding

class AppsOfInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppsBinding
    private lateinit var adapter: AppAdAdapter
    private var currentCategory = "Productivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Newly Released Apps"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = AppAdAdapter { ad ->
            // Free exposure always available; paid only if developer pays for downloads or placement
            AppAdRepository.recordDownload(ad.id)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ad.playStoreUrl))
            try {
                startActivity(intent)
                Toast.makeText(this, "Opening Play Store… (download tracked)", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Could not open store link", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        // Category chips / buttons
        binding.chipProductivity.setOnClickListener { loadCategory("Productivity") }
        binding.chipSocial.setOnClickListener { loadCategory("Social") }
        binding.chipFinance.setOnClickListener { loadCategory("Finance") }
        binding.chipHealth.setOnClickListener { loadCategory("Health") }
        binding.chipUtilities.setOnClickListener { loadCategory("Utilities") }
        binding.chipGames.setOnClickListener { loadCategory("Games") }

        loadCategory(currentCategory)
    }

    private fun loadCategory(cat: String) {
        currentCategory = cat
        binding.categoryTitle.text = "Newly Released · $cat"
        adapter.submit(AppAdRepository.byCategory(cat))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
