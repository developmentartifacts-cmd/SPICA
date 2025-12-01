package com.gibson.spica

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.gibson.spica.navigation.AppNavHost
import com.gibson.spica.ui.theme.SpicaTheme

@Composable
fun App() {
    SpicaTheme {
        Surface {
            AppNavHost()
        }
    }
}
