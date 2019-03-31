package ua.ucu.edu

import akka.actor._

import ua.ucu.edu.kafka.DummyDataProducer
import ua.ucu.edu.actor.PlantManagerActor
import ua.ucu.edu.model.Location


trait HasId {
  def id(): String
}

class plant_id extends HasId {
  import scala.util.Random
  private val id_ = Random.nextInt(1024)

  def id(): String = id_.toString
}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  //system.actorOf(Props(classOf[PlantManagerActor], "plant1", Location("0", "0")), "plant1-manager")

  def idActor(): HasId = TypedActor(system).typedActorOf(TypedProps[plant_id]())

  // prepare routees
  val routees: List[HasId] = List.fill(20) { idActor() }
  val routeePaths: List[ActorRef] = routees map { r =>
    system.actorOf(Props(classOf[PlantManagerActor],"plant_" + r.id(),Location((23 + scala.util.Random.nextFloat()* 12).toString,(48 + scala.util.Random.nextFloat()* 3).toString)))
  }

}