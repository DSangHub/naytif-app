package app.naytif

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.Notification
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class NaytifNotificationListener : NotificationListenerService() {

    companion object {
        @Volatile var instance: NaytifNotificationListener? = null

        // Read by the auto-delete timer on a background thread while the listener
        // adds/removes on the main thread; copy-on-write makes that safe to iterate.
        val notifications = CopyOnWriteArrayList<CapturedNotification>()

        @Volatile var onUpdate: (() -> Unit)? = null
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        instance = this
        Log.d("Naytif", "Listener connected")
        // Sync existing
        activeNotifications?.forEach { sbn ->
            addOrUpdate(sbn)
        }
        onUpdate?.invoke()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        instance = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        addOrUpdate(sbn)
        onUpdate?.invoke()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        notifications.removeIf { it.key == sbn.key }
        onUpdate?.invoke()
    }

    private fun addOrUpdate(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val packageName = sbn.packageName

        // Skip own notifications
        if (packageName == "app.naytif") return

        val captured = CapturedNotification(
            key = sbn.key,
            packageName = packageName,
            title = title,
            text = text,
            postTime = sbn.postTime,
            importance = Importance.NORMAL, // user can override
            isFavorite = false,
            autoDeleteAt = null
        )

        val existing = notifications.indexOfFirst { it.key == sbn.key }
        if (existing >= 0) {
            notifications[existing] = captured
        } else {
            notifications.add(0, captured) // newest first
        }
    }

    fun cancelByKey(key: String) {
        cancelNotification(key)
        notifications.removeIf { it.key == key }
        onUpdate?.invoke()
    }
}

data class CapturedNotification(
    val key: String,
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long,
    var importance: Importance = Importance.NORMAL,
    var isFavorite: Boolean = false,
    var autoDeleteAt: Long? = null
)

enum class Importance {
    LOW, NORMAL, HIGH, CRITICAL
}
