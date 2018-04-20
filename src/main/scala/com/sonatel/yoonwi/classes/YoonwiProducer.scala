package com.sonatel.yoonwi.classes

import java.util.Properties
import java.util.concurrent.ThreadLocalRandom

import com.sonatel.yoonwi.utils.{KafkaProducerCreator, PropertiesInitializer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.sql.DataFrame

import scala.util.Random

class YoonwiProducer (vehicles : DataFrame, topic: String, BOOTSTRAP_SERVERS : String, ACKS : String, VALUE_SERIALIZER_CLASS : String, KEY_SERIALIZER_CLASS : String){

  /*Mise en place d'un logger*/
  val logger : Logger = LogManager.getLogger(getClass)
  logger.info("Calling Yoonwi Producer")

  /*Initializing broker properties*/
  val props : Properties= new PropertiesInitializer(BOOTSTRAP_SERVERS, ACKS, VALUE_SERIALIZER_CLASS, KEY_SERIALIZER_CLASS).initializer()
   logger.info("Kafka broker properties initialized successfully")

  /*Creating a producer*/
  val producer : KafkaProducer[String, String]= new KafkaProducerCreator(props).creator()
  logger.info("Kafka Producer successfully created")

  /**
    * Method for sending message to a kafka topic
    * @param record String
    */
  def sendMessage(key : String, record : String): Unit ={
    logger.info("Sending message ...")
    /**
      * Data from record that goes to topic
      */
    val data: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, record)

    /**Send data into topic
     */
    producer.send(data)
  }

  /**
    * Method du producer
    */
  def prod(): Unit ={

    var i = 0
    vehicles.foreach(row=>

       sendMessage((ThreadLocalRandom.current().nextDouble(1,5000)).toString,row.getString(0)+","+row.getString(1)+","+row.getString(2)+","+row.getString(3)+","+row.getString(4))
    )
    logger.info("Message sent successfully")
  }

  /**
    * Producer Method for structured streaming
    */
  def yoonwiProducer(): Unit ={
    val ds = vehicles
      .selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers",BOOTSTRAP_SERVERS)
      .option("topic",topic)
      .start()

  }
}

