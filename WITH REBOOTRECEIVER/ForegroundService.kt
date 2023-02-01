package android.systems.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import io.socket.client.Socket
import io.socket.client.IO
import org.json.JSONObject


class ForegroundService:Service() {



    override fun onCreate() {

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel();

        //run settings when notification is pressed
        val intent = Intent(Settings.ACTION_SETTINGS)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)

        //building notification
        val notification  = NotificationCompat.Builder(this,"123")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1,notification);

      
        //run socket io client
        val socketClass = SocketConnectionSetup(this)
        socketClass.runSocket()

        return START_STICKY;
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("123","settings", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationmanager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationmanager.createNotificationChannel(notificationChannel);
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}



