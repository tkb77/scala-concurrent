import akka.actor.{ActorRef, ActorSystem, Inbox, Props}

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object FaultHandlingStudy extends App {

  val system = ActorSystem("faultHandlingStudy")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val supervisor = system.actorOf(Props[Supervisor], "supervisor")

  supervisor ! Props[Child]
  val child = inbox.receive(5.seconds).asInstanceOf[ActorRef]

  child ! 42 // set state to 42
  child ! "get"
  println("set state to 42: " + inbox.receive(5.seconds)) // 42 expected

  child ! new ArithmeticException // crash it
  child ! "get"
  println("crash it: " + inbox.receive(5.seconds)) // 42 expected

  child ! new NullPointerException // crash it harder
  child ! "get"
  println("crash it harder: " + inbox.receive(5.seconds)) // 0 expected

  inbox.watch(child) // have Inbox watch “child”
  child ! new IllegalArgumentException // break it
  println("watch and break it: " + inbox.receive(5.seconds))

  supervisor ! Props[Child]
  val child2 = inbox.receive(5.seconds).asInstanceOf[ActorRef]
  inbox.watch(child2)
  child2 ! "get" // verify it is alive
  println("new child: " + inbox.receive(5.seconds)) // 0 expected

  child2 ! new Exception("CRASH") // escalate failure
  println("escalate failure: " + inbox.receive(5.seconds))

  Await.ready(system.terminate(), Duration.Inf)
}