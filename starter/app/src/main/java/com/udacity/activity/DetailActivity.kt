package com.udacity.activity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        intent.extras?.let {
            binding.fileName.text = it.getString(getString(R.string.downloaded_file_name_key))!!
            binding.status.text = it.getString(getString(R.string.downloaded_file_status_key))!!

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.cancel(it.getInt(getString(R.string.notification_id)))
        }

        binding.okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
