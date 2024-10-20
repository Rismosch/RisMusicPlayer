package com.rismosch.ris_music_player

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivity
import com.rismosch.ris_music_player.ui.theme.RisMusicPlayerTheme

class BrowserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RisMusicPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BrowserUi(
                        activity = this,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun BrowserUi(activity: ComponentActivity, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(activity, intent, null)
        }) {
            Text(text = "Settings")
        }

        Text(
            text = "this is the browser",
            modifier = modifier
        )
    }

}
