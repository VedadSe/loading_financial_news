package FinancialNewsLoading

import java.util.{Calendar, Date}

import FinancialNewsLoading.LoadingNewsViaActor.LoadNews
import FinancialNewsLoading.ParallelLoading.loadFile
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object LoadingNewsViaActorMainProgram {

  def main(args: Array[String]): Unit = {
    val as = ActorSystem("loading-via-actor")
    implicit val timeout = new Timeout(3.seconds)

    val calendar: Calendar = Calendar.getInstance()
    val today: Date = calendar.getTime

    val news = as.actorOf(Props(new LoadingNewsViaActor(17302558, "iotausd", "Virginia Louise Ward Brown", today, "www.thecabin.net")))

    val response: Future[Any] = news ? LoadNews()

    val starter = System.currentTimeMillis()
    val preparedNews: Seq[Future[Int]] = (1 to 10).map( br => loadFile(s"project\\Zadaca2File${br}.txt").recover {case ex: Throwable => 0})
    val loadedNews: Future[Seq[Int]] = Future.sequence(preparedNews)



    loadedNews.onComplete {
      case Success(ln) => println(s"Loaded files number: ${ln.size}. Loaded news number: ${ln.sum}. Total time duration: ${System.currentTimeMillis() - starter} milliseconds.")
      case Failure(ex) => println(s"Error: ${ex.getMessage}")
    }

    Thread.sleep(5000)

//    response.onComplete {
//      case Success(r) => println(r)
//      case Failure(ex) => println(s"greska: ${ex.getMessage}")
//    }
  }

}
