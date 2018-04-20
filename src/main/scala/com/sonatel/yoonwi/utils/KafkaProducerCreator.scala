package com.sonatel.yoonwi.utils

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer

class KafkaProducerCreator (props : Properties){

  /**
    * This method aim to create a Kafka Producer with available properties
    * @return
    */
  def creator(): KafkaProducer[String, String] ={

    val producer = new KafkaProducer[String, String](props)

    producer
  }
}
