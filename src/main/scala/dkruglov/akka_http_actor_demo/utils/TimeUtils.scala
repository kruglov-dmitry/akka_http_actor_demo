package dkruglov.akka_http_actor_demo.utils

import java.text.SimpleDateFormat
import java.util.{Date, SimpleTimeZone}


object TimeUtils {
  def GetCurrentTimeEpoch(): Long = System.currentTimeMillis()
  def GetCurrentTime(formatString: String = "yyyy-MM-dd'T'HH:mm:ssZ"): String = {
    val dtf = new SimpleDateFormat(formatString)
    dtf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"))

    dtf.format(new Date(GetCurrentTimeEpoch()))
  }

  def SleepForRandomTimeout(maxSleepInMs:Int=5000): Unit = {
    val r = new scala.util.Random

    val numSeconds = r.nextInt(maxSleepInMs)

    Thread.sleep(numSeconds)
  }
}
