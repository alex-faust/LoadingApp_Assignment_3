package com.udacity.activity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.ButtonState
import com.udacity.LoadingButton
import com.udacity.R
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.sendNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customButton: LoadingButton

    private var downloadID: Long = 0
    private val REQUEST_CODE = 0
    private val context: Context = this

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        customButton = binding.included.customButton

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel("channelID", "channelName")





        customButton.setOnClickListener {
            //TODO: Look into how to use the radio group instead of individual ones
            ButtonState.Clicked
            if (binding.radioBtnGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
                    .show()
            } else {
                when (binding.radioBtnGroup.checkedRadioButtonId) {
                    R.id.glide_radio_btn -> {
                        ButtonState.Loading
                        download(glideURL)
                        //customButton.isEnabled = false
                    }
                    R.id.loadapp_radio_btn -> {
                        ButtonState.Loading
                        download(loadAppURL)
                    }
                    R.id.retrofit_radio_btn -> {
                        ButtonState.Loading
                        download(retrofitURL)
                    }
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            customButton.setText(getString(R.string.button_loading))
            customButton.updateStatus(ButtonState.Completed)

            try {
                if (id == downloadID) {
                    val query = DownloadManager.Query()
                    query.setFilterById(id)
                    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val cursor = downloadManager.query(query)

                    if (cursor.moveToFirst()) {
                        val statusColumn = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        val detailActivityIntent = Intent(baseContext, DetailActivity::class.java)

                        when(cursor.getInt(statusColumn)) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                detailActivityIntent.putExtra(getString(
                                    R.string.downloaded_file_status_key),
                                    getString(R.string.download_status_success)
                                )
                            }
                            DownloadManager.STATUS_FAILED -> {
                                detailActivityIntent.putExtra(getString(
                                    R.string.downloaded_file_status_key),
                                    getString(R.string.download_status_fail)
                                )
                            }
                            else -> {}
                        }
                        detailActivityIntent.putExtra(getString(R.string.downloaded_file_name_key), getString(R.string.glide_radio_text))
                        detailActivityIntent.putExtra("KEY", 69)

                        val titleColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)
                        val title = cursor.getString(titleColumn)

                        val builder = NotificationCompat.Builder(baseContext, "channel ID")
                            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
                            .setContentTitle("")
                            .setContentText("")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .addAction(
                                R.drawable.ic_assistant_black_24dp,
                                title,
                                PendingIntent.getActivity(baseContext, 0, detailActivityIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
                            )

                        val notificationManager = getSystemService(NotificationManager::class.java)
                        notificationManager.sendNotification(getString(R.string.channel_id), baseContext)
                        notificationManager.notify(69, builder.build())

                    }
                }
            } catch (exception: Exception){
                Toast.makeText(baseContext, "LoadApp is selected", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val glideURL = "https://github.com/bumptech/glide"
        private const val loadAppURL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val retrofitURL = "https://github.com/square/retrofit"

        private const val CHANNEL_ID = "channelId"
    }
}

private fun createChannel(channelId: String, channelName: String) {

    //channels are only available past version 26 which is why you need to do a version check
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH)
            .apply {
            setShowBadge(false)
        }
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Notification description"


        //val notificationManager = getSystemService(context, NotificationManager::class.java)



        //notificationManager?.createNotificationChannel(notificationChannel)
    }
}


