package ua.ucu.edu.model

import ua.ucu.edu._

import dispatch._, Defaults._
import scala.util.control.Breaks.break
import java.io.FileInputStream
import play.api.libs.json.Json
import scala.io.Source
import scala.concurrent.Future

object DefinedLocations {

  val metainfo_path = "city.list.json"

  val file = Source.fromResource(metainfo_path).getLines().mkString("\n")
  val json_input = Json.parse(file)

  def getCityId(i: Int): String = {
    val result = (json_input(i) \ "id").as[Long].toString()
    result
  }

  def getCoords(i: Int): (String, String) = {
    val lon = (json_input(i) \ "coord" \ "lon").as[Float].toString()
    val lat = (json_input(i) \ "coord" \ "lat").as[Float].toString()
    val result = (lon, lat)
    result
  }

}

object Parser {

  val weather_url_cityid = "http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s"
  val weather_url_coord = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s"

  val api = "248d548925b2613aa412849a982f0bc4"

  implicit val classAWrites= Json.writes[WeatherData]

  def getByCity(cityId: String): String = {

    val formatted_url = weather_url_cityid.format(cityId, api)
    val svc = url(formatted_url)
    val info = Http.default(svc OK as.String)
    val parsed = Json.parse(info())

    val lon = (parsed \ "coord" \  "lon").as[Float]
    val lat = (parsed \ "coord" \  "lat").as[Float]
    val weather = ((parsed \ "weather")(0) \ "main").as[String]
    val description = ((parsed \ "weather")(0) \ "description").as[String]
    val temperature = (parsed \ "main"  \ "temp").as[Float]
    val pressure = (parsed \ "main" \ "pressure").as[Float]
    val humidity = (parsed \ "main" \ "humidity").as[Float]

    val classObject = WeatherData(cityId.toLong, lon, lat, weather, description, temperature, pressure, humidity)
    val jsonObjectAsString = Json.toJson(classObject).toString()
    jsonObjectAsString
  }

  def getByCoords(lon: String, lat: String): String = {

    val formatted_url = weather_url_coord.format(lat, lon, api)
    val svc = url(formatted_url)
    val info = Http.default(svc OK as.String)
    val parsed = Json.parse(info())

    val id = (parsed \ "id").as[Long]
    val weather = ((parsed \ "weather")(0) \ "main").as[String]
    val description = ((parsed \ "weather")(0) \ "description").as[String]
    val temperature = (parsed \ "main"  \ "temp").as[Float]
    val pressure = (parsed \ "main" \ "pressure").as[Float]
    val humidity = (parsed \ "main" \ "humidity").as[Float]

    val classObject = WeatherData(id, lon.toFloat, lat.toFloat, weather, description, temperature, pressure, humidity)
    val jsonObjectAsString = Json.toJson(classObject).toString()
    jsonObjectAsString

  }

}
/**
  * To be used as a record in kafka topic
  */
case class WeatherData(cityId: Long,
                       lon: Float,
                       lat: Float,
                       weather: String,
                       description: String,
                       temperature: Float,
                       pressure: Float,
                       humidity: Float)


