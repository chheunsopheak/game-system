package config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
open class FirebaseConfig {
    @PostConstruct
    open fun init() {
        val file = "game-development-1ffb6-firebase-adminsdk-fbsvc-2673a79846.json"
        val serviceAccount = javaClass.classLoader.getResourceAsStream(file)
            ?: throw IllegalStateException("Firebase service account file not found")

        val options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }
}
