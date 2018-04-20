package com.sonatel.yoonwi.launcher

import com.sonatel.yoonwi.classes.{RandomCarsKafkaProducer}
import com.sonatel.yoonwi.utils.{CustomConfig, Zone}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{SparkSession, types}
import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.sql.types._

object Generator {

  /*Mise en place d'un logger*/
  val logger : Logger = LogManager.getLogger(getClass)

  val hadoopConfig = new Configuration()

  case class Params(mongoUri: String = "")

  case class Vehicle(date:String, heure: String, vitesse : Int, latitude : Double, longitude : Double)

  def main(args: Array[String]) : Unit = {

    /**
      * Verifying number of arguments in the command line :
      * Check if configuration file is provided
      */
    if(args.length == 0){
      logger.info("Le programme requiert un fichier de configuration pour s'exécuter")
      System.exit(0)
    }

    /**
      * Retrieving configuration file directory
      */
    val hdpConfigPath = args(0)

    /**
      * Loading configuration file
      */
    CustomConfig.load(hdpConfigPath+"/application.conf")

    /**
      * Retrieving configuration parameters values
      */
    /*Spark warehouse path into hdfs*/
    val SPARK_WAREHOUSE_PATH = CustomConfig.SPARK_WAREHOUSE_PATH
    /*Vehicles geolocations path*/
    val GEOLOCATIONS_PATH  = CustomConfig.GEOLOCATIONS_PATH

    val PEAGES_PATH = CustomConfig.PEAGES_PATH

    /*Kafka parameters*/
    val TOPIC_NAME = CustomConfig.TOPIC_NAME
    val BOOTSTRAP_SERVERS = CustomConfig.BOOTSTRAP_SERVERS
    val CLIENT_ID = CustomConfig.CLIENT_ID
    val VALUE_SERIALIZER_CLASS = CustomConfig.VALUE_SERIALIZER_CLASS
    val KEY_SERIALIZER_CLASS = CustomConfig.KEY_SERIALIZER_CLASS
    val ACKS = CustomConfig.ACKS
    val INTERVAL=CustomConfig.INTERVAL //interval de génération d'événement véhicule

    /**
      * Traitement
      */
    /*Instantiate a spark session*/
    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("YOONWI")
      .config("spark.sql.warehouse.dir", SPARK_WAREHOUSE_PATH)
      .getOrCreate()

    val randomProducer = new RandomCarsKafkaProducer(sparkSession, GEOLOCATIONS_PATH, PEAGES_PATH, INTERVAL,TOPIC_NAME,BOOTSTRAP_SERVERS,ACKS,VALUE_SERIALIZER_CLASS,KEY_SERIALIZER_CLASS)

    randomProducer.produceRecord(None)
  }

}
