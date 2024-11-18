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
- 🔒 Enhanced Security Features:
  - Message encryption using Android Keystore
  - Biometric authentication
  - Input sanitization
  - Secure API key storage
  - Privacy mode with session management

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
  - AndroidX Security Crypto for encryption
  - AndroidX Biometric for authentication

## Security Features

### Message Encryption
- All messages are encrypted using AES/GCM encryption
- Encryption keys are securely stored in Android Keystore
- Messages are automatically encrypted before storage and decrypted when displayed
- Zero-knowledge encryption implementation

### Biometric Authentication
- Supports fingerprint and face recognition (device-dependent)
- Required for accessing chat history
- Automatic session management
- Authentication is cleared when app is paused
- Fallback options for devices without biometric capabilities

### Data Protection
- Input sanitization to prevent XSS and injection attacks
- Secure API key storage using `secrets.properties`
- Automatic message sanitization
- Privacy mode with session management
- Secure message display

### Best Practices
- No hardcoded sensitive information
- Proper exception handling for security operations
- Regular security state validation
- Secure error messages
- Protection against SQL injection

## Security Setup

1. Biometric Authentication:
```kotlin
// Enable biometric authentication in your app settings
// The app will automatically prompt for authentication when required
```

2. API Key Security:
```properties
# In secrets.properties (create this file, do not commit to git)
OPENAI_API_KEY=your_api_key_here
```

3. Message Encryption:
```kotlin
// Messages are automatically encrypted/decrypted
// No additional setup required
```

4. Privacy Mode:
```kotlin
// Enabled by default
// Requires authentication after app pause
// Automatic session management
```

## Security Considerations

1. **API Key Storage**:
   - Store your OpenAI API key in `secrets.properties`
   - Never commit this file to version control
   - Add `secrets.properties` to `.gitignore`

2. **Message Security**:
   - All messages are automatically encrypted
   - Encryption keys are securely stored in Android Keystore
   - Messages are only decrypted when displayed

3. **Authentication**:
   - Biometric authentication is required by default
   - Session is cleared when app is paused
   - Re-authentication required after session timeout

4. **Data Protection**:
   - All user input is sanitized
   - Protection against XSS and injection attacks
   - Secure error handling

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
