// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*
 * ________________________ ______ _______ ________
 * __  ___/__  ____/___    |___  / ___    |___  __ \
 * _____ \ _  /     __  /| |__  /  __  /| |__  /_/ /
 * ____/ / / /___   _  ___ |_  /____  ___ |_  _, _/
 * /____/  \____/   /_/  |_|/_____//_/  |_|/_/ |_|  
 *
 */
 
package org.gridgain.scalar.examples

import org.gridgain.scalar.scalar
import scalar._
import org.gridgain.grid.lang.{GridFunc => F}
import org.gridgain.grid.GridRichNode

/**
 * Demonstrates various closure executions on the cloud using Scalar.
 *
 * @author 2005-2010 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.0c.04102010
 */
object ScalarClosureExample {
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
    
    /**
     * Example entry point. No arguments required.
     */
    def main(args: Array[String]) {
        scalar {
            topology
            helloWorld
            broadcast
            unicast
            println("Count of non-whitespace is: " + count("Scalar is cool!")) // Should be 13 :-)
            greetRemotes
            greetRemotesAgain
        }
    }

    /**
     * Prints grid topology.
     */
    def topology = grid foreach ((n: GridRichNode) => println("Node: " + n.id8))

    /**
     *  Obligatory example - cloud enabled Hello World! 
     */
    def helloWorld : Unit = ("Hello World!" split " ") mapJobs { println(w) } spread

    /**
     * One way to execute closures on the grid.
     */
    def broadcast : Unit = job(println("Broadcasting!!!")) broadcast

    /**
     * One way to execute closures on the grid.
     */
    def unicast : Unit = job{println("Howdy!")} unicast grid.localNode

    /**
     * Count non-whitespace characters by spreading workload to the cloud.
     */
    def count(msg: String) : Int =
        msg.split("\\s", 0) mapJobs {
			println("...calculating for: '" + w + '\'') 
            w.length
        } spreadReduce { _.sum }

    /**
     *  Greats all remote nodes only.
     */
    def greetRemotes : Unit = {
        val me = grid.localNode.id
        job {println("Greetings from: " + me)} broadcast(grid.remoteProjection)
    }

    /**
     * Same as previous greetings for all remote nodes but projection is created manually.
     */
    def greetRemotesAgain = {
        val me = grid.localNode.id

        // Just show that we can create any projections we like...        
		job{println("Greetings again from: " + me)} broadcast(grid ~~ ((n: GridRichNode) => n.id != me))
    }
}