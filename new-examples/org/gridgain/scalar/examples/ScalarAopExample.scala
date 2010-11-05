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
import org.gridgain.grid.gridify.aop.GridifyDefaultTask
import org.gridgain.grid.gridify.{GridifyInterceptor, Gridify}

/**
 * Demonstrates a simple use of GridGain grid in Scala with <tt>Gridify</tt>
 * annotation. 
 * <p>
 * String "Hello, World!" is passed as an argument to
 * <tt>sayIt(String)</tt> method. Since this method is annotated with
 * <tt>Gridify</tt> annotation it is automatically grid-enabled and
 * will be executed on remote node. Note, that the only thing user had
 * to do is annotate method <tt>sayIt(String)</tt> with {@link Gridify}
 * annotation, everything else is taken care of by the system.
 * <p>
 * <h1 class="header">Starting Remote Nodes</h1>
 * To try this example you should (but don't have to) start remote grid instances.
 * You can start as many as you like by executing the following script:
 * <pre class="snippet">{GRIDGAIN_HOME}/bin/ggstart.{bat|sh}</pre>
 * Once remote instances are started, you can execute this example from
 * Eclipse, Idea, or NetBeans (or any other IDE) by simply hitting run
 * button. You will witness that all nodes discover each other and
 * some of the nodes will participate in task execution (check node
 * output).
 * <p>
 * <h1 class="header">AOP Configuration</h1>
 * In order for this example to execute on the grid, any of the following
 * AOP configurations must be provided (only on the task initiating node).
 * <h2 class="header">Jboss AOP</h2>
 * The following configuration needs to be applied to enable JBoss byte code
 * weaving. Note that GridGain is not shipped with JBoss and necessary
 * libraries will have to be downloaded separately (they come standard
 * if you have JBoss installed already):
 * <ul>
 * <li>
 *      The following JVM configuration must be present:
 *      <ul>
 *      <li><tt>-javaagent:[path to jboss-aop-jdk50-4.x.x.jar]</tt></li>
 *      <li><tt>-Djboss.aop.class.path=[path to gridgain.jar]</tt></li>
 *      <li><tt>-Djboss.aop.exclude=org,com -Djboss.aop.include=org.gridgain.examples</tt></li>
 *      </ul>
 * </li>
 * <li>
 *      The following JARs should be in a classpath:
 *      <ul>
 *      <li><tt>javassist-3.x.x.jar</tt></li>
 *      <li><tt>jboss-aop-jdk50-4.x.x.jar</tt></li>
 *      <li><tt>jboss-aspect-library-jdk50-4.x.x.jar</tt></li>
 *      <li><tt>jboss-common-4.x.x.jar</tt></li>
 *      <li><tt>trove-1.0.2.jar</tt></li>
 *      </ul>
 * </li>
 * </ul>
 * <p>
 * <h2 class="header">AspectJ AOP</h2>
 * The following configuration needs to be applied to enable AspectJ byte code
 * weaving.
 * <ul>
 * <li>
 *      JVM configuration should include:
 *      <tt>-javaagent:[GRIDGAIN_HOME]/libs/aspectjweaver-1.6.8.jar</tt>
 * </li>
 * <li>
 *      Classpath should contain the <tt>[GRIDGAIN_HOME]/config/aop/aspectj</tt> folder.
 * </li>
 * </ul>
 *
 * @author 2005-2010 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.0c.04102010
 */
object ScalarAopExample {
    /**
     * Main entry point to application.
     *
     * @param args Command like argument (not used).
     */
    def main(args: Array[String]) {
        scalar {
            say("Hello Cloud World!")
        }
    }

    /**
     * This method mocks business logic for the purpose of this example.
     */
    @Gridify {
        val taskName = "",
        val interceptor = classOf[GridifyInterceptor],
        val taskClass = classOf[GridifyDefaultTask],
        val timeout = 0,
        val gridName = ""
    }
    def say(msg: String) = println("\n" + msg + "\n")
}