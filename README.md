# naytif.app — Android Notification Interceptor MVP

Unified notification inbox that intercepts notifications via `NotificationListenerService`.

## Features (MVP)
- Intercepts all incoming notifications (requires Notification Access permission)
- Lists them in a single inbox
- User sets importance level (Low / Normal / High / Critical) — colored bar indicator
- Favorite (star) to protect from auto-delete
- 5-minute auto-delete timer (extendable)
- Manual delete / dismiss
- Skips own notifications

## How to build
1. Open the `naytif-app` folder in Android Studio (or use the Gradle wrapper below)
2. Sync Gradle
3. Run on a device or emulator (API 26+)
4. Grant **Notification access** when prompted (Settings → Apps → Special access → Notification access)

### Command line
```bash
./gradlew assembleDebug   # APK -> app/build/outputs/apk/debug/
./gradlew installDebug    # install on a connected device/emulator
```

Toolchain: Gradle 8.11.1 (wrapper), AGP 8.7.3, Kotlin 2.1.0, JDK 17, compile/target SDK 35.
Dependency versions live in `gradle/libs.versions.toml`.

## Ads of Interest (Newly Released Apps)
- Browse newly released apps by category (Productivity, Social, Finance, Health, Utilities, Games)
- Free exposure for all listed apps
- Developer profile view + Download button
- Only pay for downloads or paid placement (tracked locally in MVP)
- Opens Play Store link and increments download count

Button on main screen: **Newly Released Apps**

## Privacy note
This app can read notification content (messages, codes, etc.). Keep processing on-device. Disclose clearly in Play Store listing and privacy policy.

Package: `app.naytif`
