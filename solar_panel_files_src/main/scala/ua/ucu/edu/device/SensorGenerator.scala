package ua.ucu.edu.device

class SensorGenerator extends SensorApi {
  override def readCurrentValue(sensorType: String, deviceId: String): Float = sensorType match {
    case "voltage" => 220 + scala.util.Random.nextFloat() * 10 //voltage
    case "temperature" => 15 + scala.util.Random.nextFloat() * 10 //temperature
    case "percentage" => 80 + scala.util.Random.nextFloat() * 20 //percentage
  }
}
