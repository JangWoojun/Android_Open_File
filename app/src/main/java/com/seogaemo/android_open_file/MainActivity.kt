package com.seogaemo.android_open_file

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seogaemo.android_open_file.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val REQUESTCODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, REQUESTCODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val fileName = getFileNameFromUri(this, uri)
                Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }

        return fileName
    }
}