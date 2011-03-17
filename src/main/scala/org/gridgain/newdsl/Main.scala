package org.gridgain.newdsl

import org.gridgain.scalar.scalar
import scalar._
import org.gridgain.grid.GridRichNode

object Main {


  def main(args : Array[String]) = scalar {
    implicit def fullProjection = scalar.grid

    def remoteProjection = {
      val me = grid.localNode.id
      grid ~~ ((n: GridRichNode) => n.id != me)
    }

    new Examples("this is a test string").runAll
    //new Examples("this is a test string")(remoteProjection).runAll
  }


}
