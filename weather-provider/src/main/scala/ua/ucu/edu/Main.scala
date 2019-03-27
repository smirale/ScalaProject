package ua.ucu.edu

import ua.ucu.edu.kafka.WeatherDataProducer

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory

import scala.concurrent.duration
import scala.language.postfixOps

object Main extends App {

  val logger = LoggerFactory.getLogger(getClass)
  logger.info("======== Weather Provider App Init ========")

  WeatherDataProducer.pushWeatherData()

}
