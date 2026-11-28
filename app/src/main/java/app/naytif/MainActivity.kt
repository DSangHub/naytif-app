package app.naytif

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.naytif.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: NotificationAdapter = NotificationAdapter(
        onTag = { notif, importance ->
            notif.importance = importance
            adapter.notifyDataSetChanged()
        },
        onFavorite = { notif ->
            notif.isFavorite = !notif.isFavorite
            adapter.notifyDataSetChanged()
        },
        onDelete = { notif ->
            NaytifNotificationListener.instance?.cancelByKey(notif.key)
        },
        onTimer = { notif, minutes ->
            notif.autoDeleteAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes.toLong())
            adapter.notifyDataSetChanged()
        }
    )

    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.btnEnable.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        binding.btnApps.setOnClickListener {
            startActivity(Intent(this, AppsOfInterestActivity::class.java))
        }

        NaytifNotificationListener.onUpdate = {
            runOnUiThread { refresh() }
        }

        // Auto-delete checker
        scheduler.scheduleAtFixedRate({
            // An uncaught exception would silently stop all future runs, so log and carry on.
            try {
                val now = System.currentTimeMillis()
                val toDelete = NaytifNotificationListener.notifications.filter { n ->
                    val deleteAt = n.autoDeleteAt
                    deleteAt != null && deleteAt <= now && !n.isFavorite
                }
                toDelete.forEach { notif ->
                    NaytifNotificationListener.instance?.cancelByKey(notif.key)
                }
            } catch (e: Exception) {
                Log.e("Naytif", "Auto-delete check failed", e)
            }
        }, 0, 30, TimeUnit.SECONDS)

        refresh()
    }

    private fun refresh() {
        val enabled = isNotificationServiceEnabled()
        binding.btnEnable.visibility = if (enabled) android.view.View.GONE else android.view.View.VISIBLE
        binding.status.text = if (enabled) "Listening…" else "Notification access required"
        adapter.submit(NaytifNotificationListener.notifications.toList())
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val enabled = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return enabled?.contains(packageName) == true
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduler.shutdown()
        NaytifNotificationListener.onUpdate = null
    }
}
