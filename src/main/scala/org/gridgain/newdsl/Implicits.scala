package org.gridgain.newdsl

import org.gridgain.scalar.scalar
import scalar._
import org.gridgain.grid.GridProjection

object Implicits {
    /*
                       Operators:
    ---------------------------------------------------
    |  !<  - message sending                          |
    |  !!! - broadcast(!) closure {call|run}(!)       |
    |  !!< - unicast(<) closure {call|run}(!)         |
    |  !!~ - spread(~) closure {call|run}(!)          |
    |  !!^ - load balance(^) closure {call|run}(!)    |
    |                                                 |
    |  !*! - reduce(*) closure via broadcast(!)       |
    |  !*< - reduce(*) closure via unicast(<)         |
    |  !*~ - reduce(*) closure via spread(~)          |
    |  !*^ - reduce(*) closure via load balancing(^)  |
    |                                                 |
    |  ~~  - projection(~) creation(~)                |
    |  ~+  - projections(~) merge(+)                  |
    |  ~*  - projection(~) crossing(*)                |
    ---------------------------------------------------
    */

  def job[T](body: => T) = () => body

  implicit def seqIsJobMappable[T](seq:Seq[T]) = new Object {
    def mapJobs[R](func: T => R) = seq map { e => job(func(e)) }
  }

  implicit def arrayIsJobMappable[T](arr:Array[T]) = new Object {
    def mapJobs[R](func: T => R) = arr map { e => job(func(e)) }
  }

  implicit def seqIsSpreadable[T](seq:Seq[Function0[T]]) = new Object {
    def seqNoReturn = seq map {_.asInstanceOf[()=>Unit]}
    def spread(implicit grid:GridProjection) = grid !!~ seqNoReturn
    def spreadReduce[R](reducer: Seq[T] => R)(implicit grid:GridProjection) = grid !*~ (seq, reducer)
  }

  implicit def arrayIsSpreadable[T](arr:Array[Function0[T]]) = seqIsSpreadable(arr.toSeq)

}
