package ua.ucu.edu.actor

import akka.actor.Actor
import ua.ucu.edu.device.{SensorApi, SensorGenerator}
import ua.ucu.edu.model.{QueryFromPanel, ReadMeasurement, RespondMeasurement}
import ua.ucu.edu.model.SensorRecord

import scala.language.postfixOps

class SensorActor(
  val deviceId: String,
  sensorType: String
) extends Actor {

  val api: SensorApi = new SensorGenerator

  override def receive: Receive = {
    case QueryFromPanel(plantId, location, panelId) => {
      sender ! SensorRecord(plantId, location, panelId,deviceId, sensorType,api.readCurrentValue(sensorType,deviceId).toString)
    }
  }
}
