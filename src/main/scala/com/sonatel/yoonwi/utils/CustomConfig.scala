package com.sonatel.yoonwi.utils

import java.io.File

import com.typesafe.config.ConfigFactory

object CustomConfig {

  /*HDFS*/
  var SPARK_WAREHOUSE_PATH=""
  var GEOLOCATIONS_PATH=""
  var PEAGES_PATH=""

  /**
    * Kafka broker configuration
    */
  var TOPIC_NAME=""
  var BOOTSTRAP_SERVERS=""
  var CLIENT_ID=""
  var ACKS=""
  var VALUE_SERIALIZER_CLASS=""
  var KEY_SERIALIZER_CLASS=""
  var INTERVAL=0
  /**
    * Loading configuration file
    * @param inputPath String
    */
  def load(inputPath : String) : Unit ={
    val myConfigFile = new File(inputPath)
    val configFile = ConfigFactory.parseFile(myConfigFile)

    SPARK_WAREHOUSE_PATH=configFile.getString("YOONWI.HDFS.SPARK_WAREHOUSE_PATH")
    GEOLOCATIONS_PATH=configFile.getString("YOONWI.HDFS.GEOLOCATIONS_PATH")
    PEAGES_PATH=configFile.getString("YOONWI.HDFS.PEAGES_PATH")

    TOPIC_NAME=configFile.getString("YOONWI.KAFKA.TOPIC_NAME")
    BOOTSTRAP_SERVERS=configFile.getString("YOONWI.KAFKA.BOOTSTRAP_SERVERS")
    CLIENT_ID=configFile.getString("YOONWI.KAFKA.CLIENT_ID")
    ACKS=configFile.getString("YOONWI.KAFKA.ACKS")
    VALUE_SERIALIZER_CLASS=configFile.getString("YOONWI.KAFKA.VALUE_SERIALIZER_CLASS")
    KEY_SERIALIZER_CLASS=configFile.getString("YOONWI.KAFKA.KEY_SERIALIZER_CLASS")
    INTERVAL=configFile.getInt("YOONWI.KAFKA.INTERVAL")
  }
}
