package org.gridgain.newdsl

import Implicits._
import org.gridgain.grid.GridProjection

class Examples(testString:String)(implicit grid : GridProjection) {
  def runAll = {
    spread(testString)
    println(count(testString))
  }

  def spread(msg:String) =
    (msg split " ") mapJobs { println(_) } spread


  def count(msg: String) =
    (msg split " ") mapJobs { word =>
      val length = word.length
      println("length of [" + word + "] is " + length)
      length
    } spreadReduce {_.reduceLeft(_+_)}
}

