package com.froyder.personaltrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.froyder.personaltrainer.data.local.appContext
import com.froyder.personaltrainer.data.local.createSettings
import com.froyder.personaltrainer.data.repository.LocalRepository

lateinit var mainActivity: MainActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        mainActivity = this
        setContent {
            App(localRepository = LocalRepository(createSettings()))
        }
    }
}