import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._

case object Greet1
case class WhoToGreet1(who: String)
case class Greeting1(message: String)

class Greeter1 extends Actor {
  var greeting = ""

  def receive = {
    case WhoToGreet1(who) => greeting = s"hello, $who"
    case Greet1           => sender ! Greeting1(greeting)
  }
}

class GreetPrinter1 extends Actor {
  def receive = {
    case Greeting(message) => println(message)
  }
}

object ReplyActorApp extends App {
  val system = ActorSystem("helloAkka")

  val greeter = system.actorOf(Props[Greeter], "greeter")

  val greetPrinter = system.actorOf(Props[GreetPrinter])
  system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)
}