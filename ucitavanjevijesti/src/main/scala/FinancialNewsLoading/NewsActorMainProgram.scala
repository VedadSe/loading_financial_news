package FinancialNewsLoading

import java.util.{Calendar, Date}

import FinancialNewsLoading.NewsActor.LoadNews
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object NewsActorMainProgram {

  def main(args: Array[String]): Unit = {
    val as = ActorSystem("news-loader")
    implicit val timeout = new Timeout(3.seconds)

    val calendar: Calendar = Calendar.getInstance()
    val today: Date = calendar.getTime

    val news = as.actorOf(Props(new NewsActor()))

    val response: Future[Any] = news ? LoadNews()

//    val starter = System.currentTimeMillis()

    response.onComplete {
      case Success(r) => println(r)
      case Failure(ex) => println(s"greska: ${ex.getMessage}")
    }
  }

  //=> println(s"Loaded files number: ${ln.size}. Loaded news number: ${ln.sum}.")
  //=> println(s"Error: ${ex.getMessage}")

}
