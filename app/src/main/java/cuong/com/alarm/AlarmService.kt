package cuong.com.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Toast

/**
 * Created by user on 3/29/18.
 */
class AlarmService : Service(){
    var mCountDownTimer: CountDownTimer? = null
    override fun onCreate() {
        super.onCreate()
        if (mCountDownTimer != null)
            mCountDownTimer!!.cancel()
        mCountDownTimer = object : CountDownTimer(3000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.e("onTick", millisUntilFinished.toString())
            }

            override fun onFinish() {
               Toast.makeText(this@AlarmService, "Ring ring !!!", Toast.LENGTH_SHORT).show()
                mCountDownTimer!!.start()
            }
        }.start()

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e("services", "onTaskRemoved")
        val restartService = Intent(this, this.javaClass)
        restartService.`package` = packageName
        val restartServicePI = PendingIntent.getService(
                this, 1, restartService,
                PendingIntent.FLAG_ONE_SHOT)
        val alarmService = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 2000, restartServicePI)
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("services", "destroy  service")
        val restartService = Intent("RestartService")
        sendBroadcast(restartService)
        mCountDownTimer = null
    }


}