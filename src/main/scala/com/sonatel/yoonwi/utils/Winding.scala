package com.sonatel.yoonwi.utils

class Winding (polygone : List[GPS], vehicule : GPS){

  def contains(): Int ={
    /*tests if a point is Left|On|Right of an infinite line(p0,p1)
    * > 0 for p2 left of the line through p0 and p1
    * = 0 for p2 on the line
    * < 0 for p2 right of the line*/
    def isLeft(p0 : GPS, p1 : GPS, p2 : GPS): Double ={

      (p1.latitude-p0.latitude)*(p2.longitude - p0.longitude) - (p2.latitude - p0.latitude)*(p1.longitude - p0.longitude)
    }

    var wn : Int= 0

    //loop throuht all edges of the polygon
    for(i <- 0 to polygone.size - 2){
      if(polygone(i).longitude <= vehicule.longitude){//start longitude<=vehicule.longitude
        if(polygone(i+1).longitude > vehicule.longitude){//an upward crossing
          if(isLeft(polygone(i),polygone(i+1),vehicule) > 0){//vehicule left of edge
            wn+=1
          }
          else{//start longitude > vehicule.longitude(no test needed)
            if(polygone(i+1).longitude <= vehicule.longitude){ //an ownward crossing
              if(isLeft(polygone(i),polygone(i+1),vehicule) < 0){//vehicule right of edge
                wn-=1
              }
            }
          }
        }
      }
    }
    wn

  }

}
