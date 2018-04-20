package com.sonatel.yoonwi.classes

import java.text.DecimalFormat

import org.apache.spark.sql.types._
import com.sonatel.yoonwi.utils.{GPS, Peage, Winding}
import org.apache.spark.sql.SparkSession
import scala.util.{Random=>r}

class Velocity (spark : SparkSession, peagesPath : String, lat : Double, long : Double) {

  val df = new DecimalFormat()
  var peages= List[Peage]()

  val vehicule = new GPS(lat, long)

  df.setMaximumFractionDigits(0)
  df.setMinimumFractionDigits(0)


  /**
    * Génération de vitesse aléatoire
    *
    * @return String
    */
  def generate(): Int = {

    val peagesDF = spark.read
                .option("header","true")
              .option("delimiter",";")
                .csv(peagesPath)
    var speed = 0

    val peagesToList = peagesDF.collectAsList()

    for (i<-0 until peagesToList.size){
      var peage = new Peage(peagesToList.get(i).getString(0).toDouble, peagesToList.get(i).getString(1).toDouble,
        peagesToList.get(i).getString(2).toDouble, peagesToList.get(i).getString(3).toDouble,
        peagesToList.get(i).getString(4).toDouble, peagesToList.get(i).getString(5).toDouble,
        peagesToList.get(i).getString(6).toDouble, peagesToList.get(i).getString(7).toDouble)

      peages = peage :: peages

    }//List des péages

    for (i<-peages.indices){
      var polygone = List[GPS]()

      var PO = new GPS(peages(i).latitudePO, peages(i).longitudePO)
      var DO = new GPS(peages(i).latitudeDO, peages(i).longitudeDO)
      var PE = new GPS(peages(i).latitudePE, peages(i).longitudePE)
      var DE = new GPS(peages(i).latitudeDE, peages(i).longitudeDE)

      polygone = PO :: polygone
      polygone = PE :: polygone
      polygone = DE :: polygone
      polygone = DO :: polygone
      polygone = PO :: polygone  //close polygon

      val winding : Int= new Winding(polygone, vehicule ).contains()

      if(winding != 0){//coordonnées simulées dans une zone de péage

       speed = r.nextInt(20) //la vitesse est limitée à 20km/h
      }
      else{ //coordonnées simulées hors d'une zone de péage

        speed = r.nextInt(130) //la vitesse est limitée à 130km/h
      }
    }
speed
  }

}
