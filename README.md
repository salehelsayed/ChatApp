# Windsurf Chat App

A modern Android chat application that enables users to interact with multiple AI assistants powered by ChatGPT. Each AI assistant can be configured with different roles and personalities, providing a unique conversational experience.

## Features

- 🤖 Multiple AI Assistants with customizable roles
- 💬 Real-time chat interface
- 🎨 Modern Material Design UI
- 📱 Native Android implementation using Kotlin
- 💾 Local message history storage
- 🔄 Smooth message synchronization
- ⚡ Efficient message rendering with RecyclerView

## Technical Stack

- **Language**: Kotlin
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: Android Views + Jetpack Compose
- **Dependencies**:
  - AndroidX Core KTX
  - AndroidX Lifecycle
  - AndroidX Compose
  - OkHttp for network requests
  - Room for local database
  - ViewBinding for view handling

## Setup

1. Clone the repository:
```bash
git clone https://github.com/salehelsayed/ChatApp.git
```

2. Open the project in Android Studio

3. Create a `secrets.properties` file in the root directory with your OpenAI API key:
```properties
OPENAI_API_KEY=your_api_key_here
```

4. Build and run the project

## Project Structure

- `app/src/main/java/com/example/windsurf/`
  - `api/` - ChatGPT service integration
  - `adapters/` - RecyclerView adapters
  - `data/` - Database and preferences management
  - `models/` - Data models
  - `ui/theme/` - UI theming
  - `viewmodels/` - ViewModels for MVVM architecture

## Requirements

- Android Studio Arctic Fox or newer
- JDK 11
- OpenAI API key
- Android device or emulator running Android 7.0 (API 24) or higher

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Contact

Saleh - saleh.fekry@gmail.com

Project Link: https://github.com/salehelsayed/ChatApp
