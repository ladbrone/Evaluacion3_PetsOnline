package com.example.evaluacion2_petsonline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.evaluacion2_petsonline.ui.theme.Evaluacion2_PetsOnlineTheme
import com.example.evaluacion2_petsonline.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Evaluacion2_PetsOnlineTheme {
                AppNavigation()
            }
        }
    }
}