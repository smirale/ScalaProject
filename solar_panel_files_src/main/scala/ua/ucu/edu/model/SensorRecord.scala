package ua.ucu.edu.model

import ua.ucu.edu.???

/**
  * To be used as a message in device topic
  */
case class SensorRecord(plantId:String, location: Location, panelId:String,deviceId: String, sensorType: String, measurement: String)