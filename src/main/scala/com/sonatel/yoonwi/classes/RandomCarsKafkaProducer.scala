package com.sonatel.yoonwi.classes

import java.util.Properties
import java.util.concurrent.ThreadLocalRandom

import com.sonatel.yoonwi.utils.{GPS, PropertiesInitializer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SparkSession

import scala.util.{Random => r}

class RandomCarsKafkaProducer(spark : SparkSession, geoPath : String, peagesPath : String, INTERVAL : Int, TOPIC_NAME : String, BOOTSTRAP_SERVERS : String, ACKS : String, VALUE_SERIALIZER_CLASS : String, KEY_SERIALIZER_CLASS : String){

  import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp

  val props : Properties= new PropertiesInitializer(BOOTSTRAP_SERVERS, ACKS, VALUE_SERIALIZER_CLASS, KEY_SERIALIZER_CLASS).initializer()
  val producer = new KafkaProducer[String, String](props)
  case class CarEvent(carName:String, speed : Int, latitude : Double, longitude : Double, eventTime : Timestamp)

  /*List of geolocations*/
  val geoReader = new GeolocationsGenerator(spark, geoPath)

  val geolocations : List[GPS]= geoReader.readGeolocations()


  /**
    * Generator infos in Macroscopic/Mesoscopic Modeling
    * @param numRecToProduce Number of records to produce
    */

  def produceRecord(numRecToProduce: Option[Int]): Unit = {
  def generateCarRecord(topic: String): ProducerRecord[String, String] = {
  val carId = r.nextInt
    //Randomly choose geolocalisations in list
  val rand = r.nextInt(geolocations.size -1) //randomly choose an geolocation from list
  val latitude = geoReader.retrieveLatitude(geolocations,rand) //retrieve corresponding latitude
  val longitude = geoReader.retrieveLongitude(geolocations,rand) //retrieve corresponding longitude

    /***
      * Simulation de la vitesse en fonction des coordonnées choisies
      * < 20km/h dans la zone des gares de péages
      * < 130km/h dans les autres zones hors gares de péages
      */
    val speed = new Velocity(spark,peagesPath, latitude, longitude).generate()

  val value = s"$carId,$speed,$latitude,$longitude,${System.currentTimeMillis()}"//System.currentTimeMillis() permet de récupérer le moment de génération, il est l'eventTime
  print(s"Writing $value\n")
  val d = r.nextFloat() * 100
  if (d < 2) {
  //induction de décalage aléatoire : les véhicules ne se géolocalisent pas
  println("Some network dealy !")
  Thread.sleep((d*100).toLong)
}
  new ProducerRecord[String, String](TOPIC_NAME,"key", value)
}
  numRecToProduce match {
  case Some(x) if x > 0 ⇒
  producer.send(generateCarRecord(TOPIC_NAME))
  Thread.sleep(INTERVAL)
  produceRecord(Some(x - 1))
  case None ⇒
  producer.send(generateCarRecord(TOPIC_NAME))
  Thread.sleep(INTERVAL)
  produceRecord(None)
  case _ ⇒
}
}

  /**
    * Generator of cars infos in Microscopic Modeling
    * @param numRecToProduce Number of records to produce None for infinity
    */
  def produce(numRecToProduce : Option[Int]): Unit ={
    def generateCarRecord(topic: String): ProducerRecord[String, String] = {
      val carName = s"car${r.nextInt(10)}"
      val speed = r.nextInt(110)//(150)
      val acc = r.nextFloat * 100
      val latitude = ThreadLocalRandom.current().nextDouble(6, 8) //r.nextDouble(zone.minLat, zone.maxLat)
      val longitude = ThreadLocalRandom.current().nextDouble(6, 8)
      val value = s"$carName,$speed,$acc,$latitude,$longitude,${System.currentTimeMillis()}" //microscopic

      print(s"Writing $value\n")
      val d = r.nextFloat() * 100
      if (d < 2) {
        //induce random delay
        println("Argh! some network dealy")
        Thread.sleep((d*100).toLong)
      }
      new ProducerRecord[String, String](TOPIC_NAME,"key", value)
    }
    numRecToProduce match {
      case Some(x) if x > 0 ⇒
        producer.send(generateCarRecord(TOPIC_NAME))
        Thread.sleep(INTERVAL)
        produceRecord(Some(x - 1))
      case None ⇒
        producer.send(generateCarRecord(TOPIC_NAME))
        Thread.sleep(INTERVAL)
        produceRecord(None)
      case _ ⇒
    }
  }

}
