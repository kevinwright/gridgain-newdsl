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
import java.util.UUID
import java.util.concurrent.CountDownLatch
import org.gridgain.grid.{GridRichNode, GridListenActor}

/**
 * Demonstrates simple protocol-based exchange in playing a ping-pong between
 * two nodes. It is analogous to <tt>GridMessagingPingPongExample</tt> on Java side.
 *
 * @author 2005-2010 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.0c.04102010
 */
object ScalarPingPongExample {
    def main(args: Array[String]) {
        scalar {
            if (grid.nodes().size < 2) {
                error("I need a partner to play a ping pong!")

                return
            }

            // Pick first remote node as a partner.
            val loc = grid.localNode
            val rmt = grid.remoteNodes().iterator.next

            // Set up remote player: configure remote node 'rmt' to listen
            // for messages from local node 'loc'.
            rmt.remoteListenAsync(loc, new GridListenActor[String]() {
                def receive(nodeId: UUID, msg: String) {
                    println(msg)

                    msg match {
                        case "PING" => respond("PONG")
                        case "STOP" => stop
                    }
                }
            }).get

            val MAX_PLAYS = 10

            val cnt = new CountDownLatch(MAX_PLAYS)

            // Set up local player: configure local node 'loc'
            // to listen for messages from remote node 'rmt'.
            rmt.listen(new GridListenActor[String]() {
                def receive(nodeId: UUID, msg: String) {
                    println(msg)

                    if (cnt.getCount() == 1)
                        stop("STOP")
                    else
                        msg match {
                            case "PONG" => respond("PING")
                        }

                    cnt.countDown();
                }
            })

            // Serve!
            rmt !< "PING"

            // Wait til the game is over.
            cnt.await()
        }
    }
}