package com.sonatel.yoonwi.utils

/**
  * Cette classe servira à représenter la zone qui nous interésse,
  * c'est-à-dire la zone délimitant l'autoroute à péage
  * Elle est utilisée pour la génération de coordonnées de géolocalisations
  * pour les véhicules sur l'autoroute
  * @param nom String
  * @param minLat Double
  * @param maxLat Double
  * @param minLong Double
  * @param maxLong Double
  */
class Zone(var nom : String, var minLat : Double, var maxLat : Double, var minLong : Double, var maxLong : Double) {

  /**
    * Méthode d'affichage
    */
    def affichage():Unit ={
      println(nom + " " + minLat + " " + minLong + " " + minLong + " " + maxLong)
    }
}
