package com.example.androidwebviewplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewFeature          // 追加
import androidx.webkit.WebSettingsCompat
import com.example.androidwebviewplayground.ui.theme.AndroidWebViewPlayGroundTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url") ?: "https://www.google.com"
        val showUrlBar = intent.getBooleanExtra("showUrlBar", true)
        val showNavBar = intent.getBooleanExtra("showNavBar", true)
        val darkTheme = intent.getBooleanExtra("darkTheme", false)

        setContent {
            AndroidWebViewPlayGroundTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WebViewScreen(
                        url = url,
                        showUrlBar = showUrlBar,
                        showNavBar = showNavBar,
                        darkTheme = darkTheme,
                        onClose = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun WebViewScreen(
    url: String,
    showUrlBar: Boolean,
    showNavBar: Boolean,
    darkTheme: Boolean,
    onClose: () -> Unit
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // URL バー
        if (showUrlBar) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Button(
                        onClick = onClose,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }

        // WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                    }

                    webViewClient = WebViewClient()

                    // ダークモード対応（API 29以上）
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(
                            settings,
                            if (darkTheme) WebSettingsCompat.FORCE_DARK_ON
                            else WebSettingsCompat.FORCE_DARK_OFF
                        )
                    }

                    loadUrl(url)
                    webViewRef = this
                }
            },
            modifier = Modifier.weight(1f)
        )

        // ナビゲーションバー
        if (showNavBar) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (webViewRef?.canGoBack() == true) {
                                webViewRef?.goBack()
                            }
                        },
                        enabled = webViewRef?.canGoBack() ?: false
                    ) {
                        Text("Back")
                    }

                    Button(
                        onClick = {
                            if (webViewRef?.canGoForward() == true) {
                                webViewRef?.goForward()
                            }
                        },
                        enabled = webViewRef?.canGoForward() ?: false
                    ) {
                        Text("Forward")
                    }

                    Button(onClick = { webViewRef?.reload() }) {
                        Text("Reload")
                    }

                    Button(onClick = onClose) {
                        Text("Close")
                    }
                }
            }
        }
    }
}