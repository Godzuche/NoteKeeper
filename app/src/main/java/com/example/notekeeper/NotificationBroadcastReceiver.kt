package com.example.notekeeper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val bundle = RemoteInput.getResultsFromIntent(intent)
        if (bundle != null) {
            val notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)
            val text = bundle
                .getCharSequence(KEY_TEXT_REPLY).toString()
            DataManager.messages.add(0, Message(null, System.currentTimeMillis(), text))
            val user: Person = Person.Builder()
                .setKey("${Math.random()}")
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_baseline_person_24))
                .setName("Jonathan").build()

//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //added the new reply to the messaging style
            style.addMessage(text, System.currentTimeMillis(), user)

            with(NotificationManagerCompat.from(context)){
                notify(notificationId, builder.setStyle(style).build())
            }
            bundle.clear()
        }
    }
}