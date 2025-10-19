package com.example.androidwebviewplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidwebviewplayground.ui.theme.AndroidWebViewPlayGroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            AndroidWebViewPlayGroundTheme(darkTheme = darkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var url by remember { mutableStateOf("https://www.google.com") }
                    var showUrlBar by remember { mutableStateOf(true) }
                    var showNavBar by remember { mutableStateOf(true) }

                    FormScreen(
                        url = url,
                        onUrlChange = { url = it },
                        showUrlBar = showUrlBar,
                        onShowUrlBarChange = { showUrlBar = it },
                        showNavBar = showNavBar,
                        onShowNavBarChange = { showNavBar = it },
                        darkTheme = darkTheme,
                        onDarkThemeChange = { darkTheme = it },
                        onOpenWebView = {
                            val context = this@MainActivity
                            val intent = android.content.Intent(context, WebViewActivity::class.java).apply {
                                putExtra("url", url)
                                putExtra("showUrlBar", showUrlBar)
                                putExtra("showNavBar", showNavBar)
                                putExtra("darkTheme", darkTheme)
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FormScreen(
    url: String,
    onUrlChange: (String) -> Unit,
    showUrlBar: Boolean,
    onShowUrlBarChange: (Boolean) -> Unit,
    showNavBar: Boolean,
    onShowNavBarChange: (Boolean) -> Unit,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onOpenWebView: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("WebView Playground", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            label = { Text("URL") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Display URL Bar")
            Switch(checked = showUrlBar, onCheckedChange = onShowUrlBarChange)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Display Navigation Bar")
            Switch(checked = showNavBar, onCheckedChange = onShowNavBarChange)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Light / Dark Theme")
            Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Button(
            onClick = onOpenWebView,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open WebView")
        }
    }
}
