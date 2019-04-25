package ua.ucu.edu.actor

import akka.actor.FSM.Failure
import akka.actor.Status.Success
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import ua.ucu.edu.model._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.{Await, ExecutionContext, Future}
import akka.pattern.ask
import akka.util.Timeout

import scala.util.Try


/**
  * Keeps a list of device sensor actors, schedules sensor reads and pushes updates into sensor data topic
  */
class SolarPanelActor(
  val panelId: String
) extends Actor {

  val system = ActorSystem("SolarPanelActor")

  val sensor1: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "1", sensorType = "voltage")))
  val sensor2: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "2", sensorType = "temperature")))
  val sensor3: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "3", sensorType = "percentage")))

  var myDad: ActorRef = context.parent

  implicit val timeout = Timeout(1 seconds)
  implicit val ec = ExecutionContext.global

  val deviceToActorRef: mutable.Map[String, ActorRef] = mutable.Map("1" -> sensor1, "2" -> sensor2, "3" -> sensor3)

  override def preStart(): Unit = {
    super.preStart()

    // todo - schedule measurement reads
    context.system.scheduler.schedule(0 second, 1 seconds, self, ReadMeasurement)(
      context.dispatcher, self)
  }
  var sensor_records: List[String] = List("","","")

  var measurements: String = ""


  override def receive: Receive = {
    case QueryFromPlant(plantId, location) => {
      myDad = sender
      sensor1 ! QueryFromPanel(plantId, location, panelId)
      sensor2 ! QueryFromPanel(plantId, location, panelId)
      sensor3 ! QueryFromPanel(plantId, location, panelId)


      //PREVIOUS VERSION
      //      var s1: Future[Any] = akka.pattern.ask(sensor1, QueryFromPanel(plantId, location, panelId))
      //      var s2: Future[Any] = akka.pattern.ask(sensor2, QueryFromPanel(plantId, location, panelId))
      //      var s3: Future[Any] = akka.pattern.ask(sensor3, QueryFromPanel(plantId, location, panelId))
      //      var aggFut: Future[(Any, Any, Any)] = for {
      //        f1Result <- s1
      //        f2Result <- s2
      //        f3Result <- s3
      //
      //      } yield (f1Result, f2Result, f3Result)
      //
      //      var vals: List[String] = List()

      /*
      for (i <- result) vals = vals.::(i.toString())
      print("valllssss----------\n")
      print(vals)
      print("valllssss----------\n")
      print("panel:message" + panelId + "\n")*/


    }
    case SensorRecord(plantId, location, panelId,deviceId,sensorType,measurement)  => {
      sensor_records = sensor_records.updated(deviceId.toInt - 1,measurement.toString)

      if (sensor_records.contains("")==false){

          measurements = s"""{"voltage":${sensor_records(0)},"temperature":${sensor_records(1)},"percentage":${sensor_records(2)}}"""
        sensor_records = List("","","")

        myDad ! QueryFromPanelToPlant(plantId, location, panelId,deviceId,sensorType,measurements)
      }
      }



  }

}
