package com.sonatel.yoonwi.utils

import java.util.Properties

import org.apache.kafka.clients.producer.ProducerConfig

/**
  * This class initialize kafka broker properties
  * @param BOOTSTRAP_SERVERS String
  * @param ACKS String
  * @param VALUE_SERIALIZER_CLASS String
  * @param KEY_SERIALIZER_CLASS String
  */
class PropertiesInitializer(BOOTSTRAP_SERVERS : String, ACKS : String, VALUE_SERIALIZER_CLASS : String, KEY_SERIALIZER_CLASS : String) {

  val props = new Properties()

  def initializer():Properties={

    /*Assign localhost id*/
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
    /*Set acknowledgements for producer requests*/
    props.put(ProducerConfig.ACKS_CONFIG, ACKS)
    /*Client ID*/
   // props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID)
    /*Value serializer*/
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS)
    /*Key serializer*/
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS)
    /*If the request fails, the producer can automaticaly retry*/
    props.put(ProducerConfig.RETRIES_CONFIG, 0:java.lang.Integer)

    props
  }
}
