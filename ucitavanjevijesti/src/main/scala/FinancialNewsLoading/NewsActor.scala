package FinancialNewsLoading

import java.text.SimpleDateFormat

import FinancialNewsLoading.NewsActor.LoadNews
import akka.actor.Actor

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.{Failure, Success}

object NewsActor {

  case class LoadNews()
}

class NewsActor extends Actor {
  val news: Seq[Future[Int]] = (1 to 10).map( br => loadFile(s"project\\Zadaca2File${br}.txt").recover {case ex: Throwable => 0})
  val loadedNews: Future[Seq[Int]] = Future.sequence(news)

  override def receive: Receive = {
    case LoadNews() => {
      sender ! loadedNews.onComplete {
        case Success(ln) => sender ! true
        case Failure(ex) => sender ! false
      }
    }
  }

  def loadFile(fileName: String): Future[Int] = Future {
    val source = Source.fromFile(fileName)
    val newsSequence: Seq[String] = source.getLines().toSeq

    var fileContent: List[String] = List()
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val news: Seq[NewsColumns] = newsSequence.takeRight(newsSequence.size - 2).map { seqStr =>
      val parts: Seq[String] = seqStr.split("\\|")

        new NewsColumns(parts(0).toInt, parts(1).trim, parts(2).trim, dateFormat.parse(parts(3)), parts(4).trim)
    }

    for (pieceOfNews <- news) {
      fileContent = fileContent :+ (pieceOfNews.getId + "\t" + pieceOfNews.getSymbol + "\t" + pieceOfNews.getTitle + "\t" +
        pieceOfNews.getPublicationDate + "\t" + pieceOfNews.getProviderUrl)
    }
    fileContent.size
  }

}