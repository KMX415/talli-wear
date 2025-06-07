# TalliWear - Android Wear OS Baby Care Companion

TalliWear is a comprehensive Android Wear OS application designed to extend baby care tracking capabilities to your smartwatch. This project provides seamless integration between a Wear OS app and a mobile companion app for logging and tracking baby care activities.

## 🌟 Features

### Wear OS App
- **Quick Activity Logging**: One-tap logging of common baby care activities
- **Real-time Sync**: Automatic synchronization with paired mobile device
- **Activity Summary**: Today's activity overview on your wrist
- **Connection Status**: Visual indicator of phone connectivity
- **Modern UI**: Beautiful Wear OS Compose interface optimized for round displays

### Mobile Companion App
- **Activity Dashboard**: View all logged activities from both phone and watch
- **Data Synchronization**: Seamless data sync using Wear OS Data Layer API
- **Connection Monitoring**: Real-time watch connectivity status
- **Material Design 3**: Modern Android UI following latest design guidelines

### Shared Features
- **9 Activity Types**: Feeding, Diaper Change, Sleep, Tummy Time, Medication, Pumping, Bath, Playtime, Crying
- **Local Storage**: Room database for offline functionality
- **Data Persistence**: Activities stored locally and synced across devices
- **Extensible Architecture**: Easy to add new activity types and features

## 🏗️ Architecture

The project follows a multi-module architecture with clean separation of concerns:

```
TalliWear/
├── shared/           # Common data models and database
├── wear/            # Wear OS application
├── mobile/          # Mobile companion app
└── gradle/          # Dependency management
```

### Technology Stack
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for both mobile and Wear OS
- **Wear Compose**: Wear OS specific Compose components
- **Room Database**: Local data persistence
- **Hilt**: Dependency injection
- **Data Layer API**: Wear OS communication protocol
- **Coroutines & Flow**: Asynchronous programming
- **Material Design 3**: Mobile UI components
- **Horologist**: Google's Wear OS UI library

## 📱 Supported Activity Types

| Activity | Emoji | Description |
|----------|-------|-------------|
| Feeding | 🍼 | Bottle feeding, nursing sessions |
| Diaper Change | 👶 | Diaper changes and cleanups |
| Sleep | 😴 | Nap times and sleep sessions |
| Tummy Time | 🤱 | Supervised tummy time activities |
| Medication | 💊 | Medicine administration |
| Pumping | 🥛 | Breast milk pumping sessions |
| Bath | 🛁 | Bath time and hygiene |
| Playtime | 🧸 | Interactive play sessions |
| Crying | 😢 | Crying episodes and comfort |

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34
- Wear OS emulator or physical Wear OS device
- Android device for companion app

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/talliwear.git
   cd talliwear
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Install on devices**
   - Deploy the `wear` module to your Wear OS device
   - Deploy the `mobile` module to your Android phone
   - Ensure both devices are paired

### Configuration

The apps will automatically discover each other when installed on paired devices. No additional configuration is required.

## 📖 Usage

### Wear OS App

1. **Quick Logging**
   - Open TalliWear on your watch
   - Tap "Quick Log"
   - Select the activity type
   - Activity is automatically logged with timestamp

2. **View Today's Activities**
   - Tap "Activities" from the home screen
   - Scroll through today's logged activities
   - Tap any activity for details

3. **Sync with Phone**
   - Connection status is shown at the top
   - Tap the connection chip to force sync
   - Green = connected, Red = disconnected

### Mobile Companion App

1. **View Activities**
   - Open TalliWear Companion on your phone
   - View all activities in chronological order
   - Activities from both phone and watch appear here

2. **Monitor Connection**
   - Watch connection status shown at top
   - Activities sync automatically when connected

## 🔧 Development

### Project Structure

```
shared/
├── data/
│   ├── ActivityType.kt          # Activity type enumeration
│   ├── BabyCareActivity.kt      # Main data model
│   └── DataLayerPaths.kt        # Communication constants
└── database/
    ├── ActivityDao.kt           # Database access object
    ├── Converters.kt           # Room type converters
    └── TalliWearDatabase.kt    # Room database

wear/
├── data/
│   ├── DataLayerRepository.kt   # Wear-side communication
│   └── DataLayerListenerService.kt
├── presentation/
│   ├── screens/                # Compose screens
│   ├── components/             # Reusable UI components
│   ├── navigation/             # Navigation setup
│   └── theme/                  # Wear OS theming
└── di/                         # Dependency injection

mobile/
├── data/                       # Mobile-side communication
├── ui/                         # Mobile UI components
└── di/                         # Dependency injection
```

### Adding New Activity Types

1. **Update ActivityType enum** in `shared/data/ActivityType.kt`
2. **Add emoji and color** for the new activity
3. **Update UI** to include the new activity in quick actions
4. **Test synchronization** between devices

### Extending Features

The architecture supports easy extension:
- **New screens**: Add to navigation in respective modules
- **Additional data**: Extend BabyCareActivity model
- **Custom UI**: Create new Compose components
- **Background sync**: Extend DataLayerListenerService

## 🔄 Data Synchronization

The app uses Wear OS Data Layer API for real-time synchronization:

### Communication Paths
- `/activity/added` - New activity from watch to phone
- `/sync/request` - Watch requests full sync from phone
- `/timer/start` - Timer operations between devices

### Sync Strategy
- **Immediate sync**: Activities sync immediately when devices are connected
- **Offline support**: Activities stored locally when disconnected
- **Conflict resolution**: Last-write-wins for simplicity
- **Automatic retry**: Failed syncs retry when connection restored

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### UI Tests
```bash
./gradlew connectedAndroidTest
```

### Manual Testing
1. Install on both devices
2. Ensure devices are paired
3. Log activities on watch
4. Verify they appear on phone
5. Test offline/online scenarios

## 🤝 Integration with Talli Ecosystem

This app is designed to complement the existing Talli baby care ecosystem:

### Potential Integrations
- **Talli Baby App**: Import/export activities
- **Talli Hardware**: Sync with physical tracking devices
- **Cloud Services**: Backup to Talli cloud platform
- **Family Sharing**: Multi-caregiver support

### API Compatibility
The data models are designed to be compatible with Talli's existing API structure, making integration straightforward.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google Wear OS Team** for excellent documentation and tools
- **Talli** for inspiration and baby care tracking innovation
- **Android Community** for open-source libraries and best practices

## 📞 Support

For questions, issues, or contributions:
- Open an issue on GitHub
- Check existing documentation
- Review Wear OS development guides

---

**Built with ❤️ for parents and caregivers everywhere** 