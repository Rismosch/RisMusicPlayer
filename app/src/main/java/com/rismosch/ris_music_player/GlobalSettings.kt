package com.rismosch.ris_music_player

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.microedition.khronos.opengles.GL

const val GLOBAL_SETTINGS_PATH = "global_settings"

const val SEPERATOR = "="
const val KEY_URI = "uri"

class GlobalSettings {
    var uri by mutableStateOf(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
    var switchValue by mutableStateOf(true)
}

class SettingsSerializer {
    fun Serialize(context: Context, value: GlobalSettings) {
        // serialize
        var content = ""
        content += "$KEY_URI $SEPERATOR ${value.uri}"

        // write file
        val file = File(context.filesDir, GLOBAL_SETTINGS_PATH)
        file.createNewFile()
        if (file.exists()) {
            val stream = FileOutputStream(file)
            val bytes = content.toByteArray()
            stream.write(bytes)
            stream.close()
        }
    }

    fun Deserialize(context: Context): GlobalSettings? {
        // read file
        val file = File(context.filesDir, GLOBAL_SETTINGS_PATH)
        if (!file.exists()) {
            return null
        }

        val stream = FileInputStream(file)
        val bytes = stream.readBytes()
        val content = String(bytes)

        // deserialize

        try {
            val result = GlobalSettings()

            content.trim().lines().forEach { line ->
                val splits = line.split(SEPERATOR, limit = 2)
                val key = splits[0].trim()
                val value = splits[1].trim()

                when (key) {
                    KEY_URI -> result.uri = Uri.parse(value)
                    else -> println("unkown key \"$key\"")
                }
            }

            return result
        } catch (e: Exception) {
            println("failed to deserialize settings: ${e}")
            return null;
        }
    }
}