package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, TypedActor, TypedProps}
import com.fasterxml.jackson.databind.util.Named
import ua.ucu.edu.kafka.DummyDataProducer
import ua.ucu.edu.model._

import scala.collection.mutable
import scala.concurrent.duration._


/**
  * This actor manages solar plant, holds a list of panels and knows about its location
  * todo - the main purpose right now to initialize panel actors
  */

trait HasId {
  def id(): String
}

class panel_id extends HasId {
  import scala.util.Random
  private val id_ = Random.nextInt(1024)

  def id(): String = id_.toString
}



class PlantManagerActor(
  plantName: String,
  location: Location
) extends Actor with ActorLogging {
  val system = ActorSystem("test")
  // todo - populate a list of panels on this plant
  //var location = Location((23 + scala.util.Random.nextFloat()* 12).toString,(48 + scala.util.Random.nextFloat()* 3).toString)

    /*
  val panel_1: ActorRef = system.actorOf(Props(new SolarPanelActor("panel_1")))
  val panel_2: ActorRef = system.actorOf(Props(new SolarPanelActor("panel_2")))
  val panelToActorRef: mutable.Map[String, ActorRef] = mutable.Map("panel_1" -> panel_1, "panel_2" -> panel_2)*/


  def idActor(): HasId = TypedActor(system).typedActorOf(TypedProps[panel_id]())

  // prepare routees
  val routees: List[HasId] = List.fill(50) { idActor() }
  val routeePaths: List[ActorRef] = routees map { r =>
    //system.actorOf(Props(new SolarPanelActor("panel_" + r.id())),"SolarPanelActor")
    system.actorOf(Props(classOf[SolarPanelActor],("panel_" + r.id())))
  }

  var kafka_actor = system.actorOf(Props(classOf[DummyDataProducer]))



  override def preStart(): Unit = {
    log.info(s"========== Solar Plant Manager starting ===========")
    super.preStart()
  }

  def pushPanels(): Unit = {
    log.info(s"========== Solar Plant Pushh panels===========\n")
    for (panel <- routeePaths){
      panel ! QueryFromPlant(plantName, location)
    }
  }


  import system.dispatcher
  system.scheduler.schedule(0 seconds, 5 seconds) {
    pushPanels()
  }

  override def receive: Receive = {
    case Query() => pushPanels()
    case QueryFromPanelToPlant(plantId, location, panelId,deviceId,sensorType,measurements) =>
      kafka_actor ! QueryFromPanelToPlant(plantId, location, panelId,deviceId,sensorType,measurements)
    }


}
