package com.froyder.personaltrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.froyder.personaltrainer.data.local.appContext
import com.froyder.personaltrainer.data.local.createSettings
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.google.firebase.FirebaseApp

lateinit var mainActivity: MainActivity

class MainActivity : ComponentActivity() {
    private val localRepository by lazy {
        LocalRepository(createSettings())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        mainActivity = this

        // Verify Firebase is configured
        try {
            FirebaseApp.getInstance()
        } catch (e: Exception) {
            throw IllegalStateException(
                "Firebase not configured. Add google-services.json to composeApp/ folder. See README for setup instructions."
            )
        }

        setContent {
            App(localRepository = localRepository)
        }
    }
}