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

//
//package config
//
//import com.google.auth.oauth2.GoogleCredentials
//import com.google.firebase.FirebaseApp
//import com.google.firebase.FirebaseOptions
//import io.github.cdimascio.dotenv.dotenv
//import jakarta.annotation.PostConstruct
//import org.springframework.context.annotation.Configuration
//import java.io.ByteArrayInputStream
//
//@Configuration(proxyBeanMethods = false)
//class FirebaseConfig {
//
//    private val dotenv = dotenv()
//
//    @PostConstruct
//    fun init() {
//        val firebasePrivateKey = dotenv["FIREBASE_PRIVATE_KEY"]?.replace("\\n", "\n")
//            ?: throw IllegalStateException("Missing FIREBASE_PRIVATE_KEY")
//        val firebaseClientEmail = dotenv["FIREBASE_CLIENT_EMAIL"]
//            ?: throw IllegalStateException("Missing FIREBASE_CLIENT_EMAIL")
//        val firebaseProjectId = dotenv["FIREBASE_PROJECT_ID"]
//            ?: throw IllegalStateException("Missing FIREBASE_PROJECT_ID")
//
//        val serviceAccountJson = """
//            {
//              "type": "${dotenv["FIREBASE_TYPE"]}",
//              "project_id": "$firebaseProjectId",
//              "private_key_id": "${dotenv["FIREBASE_PRIVATE_KEY_ID"]}",
//              "private_key": "$firebasePrivateKey",
//              "client_email": "$firebaseClientEmail",
//              "client_id": "${dotenv["FIREBASE_CLIENT_ID"]}",
//              "auth_uri": "${dotenv["FIREBASE_AUTH_URI"]}",
//              "token_uri": "${dotenv["FIREBASE_TOKEN_URI"]}",
//              "auth_provider_x509_cert_url": "${dotenv["FIREBASE_AUTH_PROVIDER_CERT_URL"]}",
//              "client_x509_cert_url": "${dotenv["FIREBASE_CLIENT_CERT_URL"]}",
//              "universe_domain": "${dotenv["FIREBASE_UNIVERSE_DOMAIN"]}"
//            }
//        """.trimIndent()
//
//        val credentialsStream = ByteArrayInputStream(serviceAccountJson.toByteArray())
//        val options = FirebaseOptions.builder()
//            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
//            .build()
//
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options)
//        }
//    }
//}
