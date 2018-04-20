package com.sonatel.yoonwi.classes

import java.text.DecimalFormat
import java.util.concurrent.ThreadLocalRandom

import com.sonatel.yoonwi.utils.{GPS, Zone}
import org.apache.spark.sql.{DataFrame, SparkSession}

class GeolocationsGenerator(spark : SparkSession, geoPath : String) {

  def readGeolocations(): List[GPS] ={

    var listGeo = List[GPS]()
    val geolocations : DataFrame = spark.read
      .option("header","true")
      .option("delimiter",";")
      .csv(geoPath)

    val liste = geolocations.collectAsList()

    for(i <- 0 to liste.size()-1){

      var g = new GPS(liste.get(i).getString(0).toDouble, liste.get(i).getString(1).toDouble)

      listGeo = g :: listGeo
    }
    listGeo
  }
  def retrieveLatitude(gps : List[GPS], rand : Int): Double ={

   gps(rand).latitude

  }
  def retrieveLongitude(gps : List[GPS], rand : Int): Double ={
    gps(rand).longitude
  }
}
