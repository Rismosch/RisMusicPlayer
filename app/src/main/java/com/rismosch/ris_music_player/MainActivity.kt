package com.rismosch.ris_music_player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.rismosch.ris_music_player.ui.theme.RisMusicPlayerTheme

const val PICK_DIRECTORY_REQUEST_CODE = 42

class GlobalSettings {
    var uri by mutableStateOf(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
    var switchValue by mutableStateOf(true)
}

class MainActivity : ComponentActivity() {
    private lateinit var openDirectoryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var globalSettings = GlobalSettings();

        openDirectoryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.also { uri ->
                        globalSettings.uri = uri;
                    }
                }
            }

        enableEdgeToEdge()
        setContent {
            RisMusicPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(
                        openDirectoryLauncher,
                        globalSettings,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.also { uri -> println("hello $resultCode $uri") }
        }
    }

    fun openDirectory(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(this, intent, PICK_DIRECTORY_REQUEST_CODE, null)
    }
}

@Composable
fun SettingsScreen(
    openDirectoryLauncher: ActivityResultLauncher<Intent>,
    globalSettings: GlobalSettings,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, globalSettings.uri)
                    }

                    openDirectoryLauncher.launch(intent)
                }
        ) {
            Text(
                text = "Directory",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(text = globalSettings.uri.toString())
        }

        SettingSwitch(
            title = "this is a switch",
            checked = globalSettings.switchValue,
        ) { newValue ->
            globalSettings.switchValue = newValue
        }
    }
}

@Composable
fun SettingSwitch(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
