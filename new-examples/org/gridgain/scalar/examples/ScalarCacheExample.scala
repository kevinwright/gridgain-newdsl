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
import org.gridgain.grid.cache._

/**
 * Demonstrates basic Data Grid (a.k.a cache) operations with Scalar.
 *
 * @author 2005-2010 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.0c.04102010
 */
object ScalarCacheExample {
    def main(args: Array[String]) {
        scalar {
            val lt30 = (k: String, v: Int) => v < 30
            val gt10 = (e: GridCacheEntry[String, Int]) => e.peek() > 10

            // Create default cache predicate-based projection (all values > 30).
            val c = cache[String, Int] ~/ lt30

            // Add few values.
            c += "1" -> 1
            c += "2" -> 2

            // Update values.
            c("1") = 11
            c("2") = 22

            // These should be filtered out by projection.
            c("3") = 31
            c("4") = 32

            // Add three more...
            c + ("a" -> 1) + ("b" -> 1) + ("c" -> 1)

            // Update them.
            c += ("a" -> 11, "b" -> 11, "c" -> 11)

            // Remove couple.
            c.cache() -= ("b", "c")

            // These should not pass due to predicate.
            c += ("5" -> 5, gt10)
            c += ("6" -> 4, gt10)

            // Get with option...
            c ?? "bla" match {
                case Some(v) => error("Should never happen.")
                case None => println("Correct")
            }

            println(c.size) // Should be 3.

            // Print all projection values.
            convert(c.values) foreach println

            // Get typed cache projection of [String, String] type.
            val t = cache ~| (classOf[String], classOf[String])

            t += ("skey" -> "sval")

            // This won't compile as 'Int' != 'String'
            //t += ("s" -> 2)

            // That should be 1 since we only have 1 value in type projection.
            println(t.size)

            // Print all typed projection values.
            convert(t.values) foreach println

            // This is another way to print the same values as above
            // using Java's side functional APIs on Scalar cache projection.
            t forEach ((e: GridCacheEntry[String, String]) => println(e.getValue))
        }
    }
}