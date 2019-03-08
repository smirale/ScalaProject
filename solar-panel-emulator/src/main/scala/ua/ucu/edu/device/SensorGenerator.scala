package ua.ucu.edu.device

class SensorGenerator extends SensorApi {
  override def readCurrentValue(sensorType: String, deviceId: String): String = sensorType + deviceId
}
