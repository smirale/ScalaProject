package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import ua.ucu.edu.model.{Location, PanelRespond, Query, QueryFromPlant}

import scala.collection.mutable

/**
  * This actor manages solar plant, holds a list of panels and knows about its location
  * todo - the main purpose right now to initialize panel actors
  */
class PlantManagerActor(
  plantName: String,
  location: Location
) extends Actor with ActorLogging {

  val system = ActorSystem()

  val panel1: ActorRef = system.actorOf(Props(new SolarPanelActor("1")))
  val panel2: ActorRef = system.actorOf(Props(new SolarPanelActor("2")))

  val panelToActorRef: mutable.Map[String, ActorRef] = mutable.Map("1" -> panel1, "2" -> panel2)

  override def preStart(): Unit = {
    log.info(s"========== Solar Plant Manager starting ===========")
    super.preStart()
  }

  def pushPanels(): Unit = {
    for ((_, panel) <- panelToActorRef){
      panel ! QueryFromPlant(plantName, location)
    }
  }

  override def receive: Receive = {
    case Query() => pushPanels()
  }

}
