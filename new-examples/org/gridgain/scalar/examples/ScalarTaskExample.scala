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
import org.gridgain.scalar.scalar._
import java.lang.String
import org.gridgain.grid._
import java.{util => ju}
import collection.JavaConversions._

/**
 * Demonstrates use of full grid task API using Scalar. Note that using task-based
 * grid enabling gives you all the advanced features of GridGain such as custom topology
 * and collision resolution, custom failover, mapping, reduction, load balancing, etc.
 * As a trade off in such cases the more code needs to be written vs. simple closure execution.
 *
 * @author 2005-2010 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.0c.04102010
 */
object ScalarTaskExample {
    /**
     * Main entry point to application. No arguments required.
     *
     * @param args Command like argument (not used).
     */
    def main(args: Array[String]) {
        scalar {
            grid.execute(classOf[GridHelloWorld], "Hello Cloud World!").get
        }
    }

    /**
     * This task encapsulates the logic of MapReduce.
     */
    class GridHelloWorld extends GridTaskNoReduceSplitAdapter[String] {
        /**
         * Splits input arguments into collection of closures.
         */
        def split(gridSize: Int, arg: String): ju.Collection[_ <: GridJob] = {
            (arg split " ") mapJobs { println(w) }
        }
    }
}
