package ua.ucu.edu.kafka

import java.util.Properties

import akka.actor.Actor
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}
import ua.ucu.edu.model
import ua.ucu.edu.model.{QueryFromPanelToPlant, QueryFromPlant}

// delete_me - for testing purposes



class DummyDataProducer extends Actor {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  // This is just for testing purposes
  override def receive: Receive = {
    case QueryFromPanelToPlant(plantId, location, panelId,deviceId,sensorType,measurements) => {
      var str =
        s"""{"plantId":${plantId},
            "location":
                  {"longitude":${location.longitude},
                  "latitude":${location.latitude}},
            "panelId":${panelId},
            "deviceId":${deviceId},
            "sensorType":${sensorType},
            "measurements":${measurements}
           }
        """.stripMargin


      print(str)
//      val BrokerList: String = System.getenv(Config.KafkaBrokers)
//      val Topic = "sensor-data"
//
//      val props = new Properties()
//      props.put("bootstrap.servers", BrokerList)
//      props.put("client.id", "solar-panel-1")
//      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//
//      logger.info("initializing producer")
//
//      val producer = new KafkaProducer[String, String](props)
//
//
//
//      val data = new ProducerRecord[String, String](Topic, measurements)
//      producer.send(data)
//      logger.info(s"[$Topic] $measurements")
    }
  }

//  override def receive: Receive = {
//    Thread.sleep(10000)
//    logger.info(s"[$Topic] $testMsg")
//    val data = new ProducerRecord[String, String](Topic, testMsg)
//    producer.send(data)
//  }
}

object Config {
  val KafkaBrokers = "KAFKA_BROKERS"
}
