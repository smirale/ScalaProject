package ua.ucu.edu.device

trait SensorApi {
  def readCurrentValue(sensorType: String, deviceId: String): String
}
