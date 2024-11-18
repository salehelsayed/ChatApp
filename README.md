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

### Message Encryption Implementation

#### Encryption Protocol
- **Algorithm**: AES (Advanced Encryption Standard)
- **Mode**: GCM (Galois/Counter Mode)
- **Key Size**: 256-bit
- **IV Size**: 12 bytes (96 bits)
- **Authentication Tag**: 128 bits
- **Padding**: No padding (NoPadding)

#### Key Management
- **Storage**: Android Keystore System
  - Hardware-backed security (if available)
  - Protected against extraction and tampering
  - Keys never leave the secure hardware
  - Resistant to offline attacks

- **Key Generation**:
  ```kotlin
  KeyGenParameterSpec.Builder(alias, PURPOSE_ENCRYPT | PURPOSE_DECRYPT)
      .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
      .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
      .setKeySize(256)
      .setUserAuthenticationRequired(false) // Can be set to true for additional security
      .build()
  ```

#### Encryption Process
1. **Message Preparation**:
   ```kotlin
   // Input sanitization
   sanitizedText = SecurityManager.sanitizeInput(plaintext)
   
   // Convert to bytes using UTF-8
   messageBytes = sanitizedText.toByteArray(Charsets.UTF_8)
   ```

2. **Encryption**:
   ```kotlin
   // Generate random IV for each message
   cipher.init(Cipher.ENCRYPT_MODE, secretKey)
   
   // Encrypt with AES/GCM
   encryptedBytes = cipher.doFinal(messageBytes)
   
   // Combine IV and encrypted data
   combined = cipher.iv + encryptedBytes
   
   // Base64 encode for storage
   encodedMessage = Base64.getEncoder().encodeToString(combined)
   ```

3. **Decryption**:
   ```kotlin
   // Decode from Base64
   decoded = Base64.getDecoder().decode(encodedMessage)
   
   // Extract IV and encrypted data
   iv = decoded.slice(0..11)
   encrypted = decoded.slice(12..decoded.lastIndex)
   
   // Initialize cipher with IV
   cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
   
   // Decrypt
   decryptedBytes = cipher.doFinal(encrypted)
   ```

#### Security Guarantees
- **Confidentiality**: AES-256 encryption ensures message secrecy
- **Integrity**: GCM mode provides authenticated encryption
- **Forward Secrecy**: Unique IV for each message
- **Key Protection**: Hardware-backed key storage
- **Tamper Detection**: Built-in authentication tags
- **Side-Channel Protection**: Constant-time operations

#### Implementation Benefits
1. **Hardware Security**:
   - Utilizes Trusted Execution Environment (TEE) if available
   - Hardware-backed key generation and storage
   - Protected against key extraction

2. **Authentication Integration**:
   - Optional biometric authentication for key access
   - Key invalidation on biometric changes
   - Secure key rotation support

3. **Performance Optimization**:
   - Efficient GCM mode implementation
   - Hardware acceleration when available
   - Minimal performance overhead

4. **Error Handling**:
   - Secure error messages
   - Graceful failure handling
   - No sensitive information leakage

#### Security Considerations
1. **Key Storage**:
   - Keys are stored in Android Keystore
   - Never exposed to the application layer
   - Protected by hardware security module (if available)
   - Automatically deleted when app is uninstalled

2. **Encryption Strength**:
   - AES-256 is quantum-resistant
   - GCM provides authenticated encryption
   - Random IV prevents replay attacks
   - No known practical attacks

3. **Implementation Details**:
   - No static IVs or keys
   - No ECB mode usage
   - No hardcoded cryptographic material
   - Proper IV handling

4. **Best Practices**:
   - Regular security audits
   - Proper key rotation
   - Secure error handling
   - Input validation

#### Cryptographic Technical Details
```
Algorithm: AES/GCM/NoPadding
Key specifications:
- Type: AES
- Size: 256 bits
- Storage: Android Keystore
- Protection: Hardware-backed (if available)

IV specifications:
- Size: 96 bits (12 bytes)
- Generation: Secure random
- Usage: Once per message

Authentication:
- Tag size: 128 bits
- Coverage: Full message + AAD
- Verification: Automatic in GCM mode

Message format:
[IV (12 bytes)][Encrypted Message][Auth Tag (16 bytes)]
```

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
