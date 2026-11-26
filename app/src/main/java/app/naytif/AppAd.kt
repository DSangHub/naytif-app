package app.naytif

data class AppAd(
    val id: String,
    val name: String,
    val category: String,
    val shortDesc: String,
    val fullDesc: String,
    val developer: String,
    val releaseDate: String,
    val playStoreUrl: String, // or deep link
    val isPaidPlacement: Boolean = false, // true = paid for placement
    var downloadCount: Int = 0,
    val iconColor: Int = 0xFF6200EE.toInt() // simple colored placeholder
)

object AppAdRepository {
    val categories = listOf("Productivity", "Social", "Finance", "Health", "Utilities", "Games")

    val ads = mutableListOf(
        AppAd("1", "FocusFlow", "Productivity", "Deep work timer with ambient sound",
            "Block distractions and enter flow state with customizable sessions.", "Focus Labs", "2026-11-20",
            "https://play.google.com/store/apps/details?id=com.example.focusflow", isPaidPlacement = true),
        AppAd("2", "QuietInbox", "Utilities", "Smart notification filter",
            "AI-powered rules that keep only what matters.", "Naytif Team", "2026-11-18",
            "https://play.google.com/store/apps/details?id=app.naytif", isPaidPlacement = false),
        AppAd("3", "BudgetBee", "Finance", "Simple expense tracker",
            "Scan receipts and categorize spending automatically.", "Bee Finance", "2026-11-15",
            "https://play.google.com/store/apps/details?id=com.example.budgetbee"),
        AppAd("4", "Pulse Health", "Health", "Daily habit & vitals log",
            "Track sleep, steps, and mood in one clean timeline.", "Pulse Co", "2026-11-12",
            "https://play.google.com/store/apps/details?id=com.example.pulse"),
        AppAd("5", "ChatWave", "Social", "Lightweight group chats",
            "End-to-end encrypted rooms with minimal features.", "Wave Labs", "2026-11-10",
            "https://play.google.com/store/apps/details?id=com.example.chatwave", isPaidPlacement = true),
        AppAd("6", "Pixel Quest", "Games", "Minimalist puzzle adventure",
            "Solve optical puzzles across 50 handcrafted levels.", "Pixel Studio", "2026-11-08",
            "https://play.google.com/store/apps/details?id=com.example.pixelquest")
    )

    fun byCategory(cat: String) = ads.filter { it.category == cat }
    fun recordDownload(id: String) {
        ads.find { it.id == id }?.let { it.downloadCount++ }
    }
}
