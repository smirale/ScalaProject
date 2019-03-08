package ua.ucu.edu

import akka.actor._
import org.apache.kafka.clients.producer.KafkaProducer
import ua.ucu.edu.actor.PlantManagerActor
//import ua.ucu.edu.kafka.DummyDataProducer
import ua.ucu.edu.model.{Location, Query}

import java.util.Properties

import scala.concurrent.duration._


object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  val plant: ActorRef = system.actorOf(Props(new PlantManagerActor("plant1", location = Location(0, 0))))

  def pushPlant(): Unit ={
    plant ! Query()
  }

  import system.dispatcher
  system.scheduler.schedule(0 seconds, 1 seconds) {
    pushPlant()
  }

}