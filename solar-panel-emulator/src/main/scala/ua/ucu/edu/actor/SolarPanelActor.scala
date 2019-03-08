package ua.ucu.edu.actor

import java.util.Properties

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import ua.ucu.edu.model._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps
import ua.ucu.edu.actor.SensorActor

/**
  * Keeps a list of device sensor actors, schedules sensor reads and pushes updates into sensor data topic
  */
class SolarPanelActor(
  val panelId: String
) extends Actor {

  val BrokerList: String = System.getenv("KAFKA_BROKERS")
  val topic = System.getenv("SENSOR_TOPIC_NAME")
  val props = new Properties()

  props.put("bootstrap.servers", BrokerList)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val system = ActorSystem("sensor-test")

  val sensor1: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "1", sensorType = "a")))
  val sensor2: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "2", sensorType = "a")))
  val sensor3: ActorRef = system.actorOf(Props(new SensorActor(deviceId = "3", sensorType = "a")))

  val deviceToActorRef: mutable.Map[String, ActorRef] = mutable.Map("1" -> sensor1, "2" -> sensor2, "3" -> sensor3)

  override def preStart(): Unit = {
    super.preStart()
  }

  override def receive: Receive = {
    case QueryFromPlant(plantId, location) => {
      for ((_, sensor) <- deviceToActorRef) {
        sensor ! QueryFromPanel(plantId, location, panelId)
      }
    }
    case SensorRespond(deviceId, sensorType, value) => {
      val data = new ProducerRecord[String, String](topic, )
      producer.send(data)
    }
    }
  }

