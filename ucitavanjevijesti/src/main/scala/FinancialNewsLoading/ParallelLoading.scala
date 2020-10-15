package FinancialNewsLoading

import java.text.SimpleDateFormat
import java.util.Date

import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ParallelLoading {

  case class FinancialNews(id: Int, symbol: String, title: String, publicationDate: Date, providerUrl: String)

    def loadFile(fileName: String): Future[Int] = Future {
      val source = Source.fromFile(fileName)
      val newsSequence: Seq[String] = source.getLines().toSeq.tail

      var fileContent: List[String] = List()
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

      val news: Seq[FinancialNews] = newsSequence.tail.map { seqStr =>
        val parts: Seq[String] = seqStr.split("\\|")

          FinancialNews(parts(0).toInt, parts(1).trim, parts(2).trim, dateFormat.parse(parts(3)), parts(4).trim)
      }

      for (pieceOfNews <- news) {
        fileContent = fileContent :+ (pieceOfNews.id + "\t" + pieceOfNews.symbol + "\t" + pieceOfNews.title + "\t" + pieceOfNews.publicationDate + "\t" + pieceOfNews.providerUrl)
      }
      fileContent.size
    }

    def main(args: Array[String]): Unit = {
      val starter = System.currentTimeMillis()
      val news: Seq[Future[Int]] = (1 to 10).map( br => loadFile(s"project\\Zadaca2File${br}.txt").recover {case ex: Throwable => 0})
      val loadedNews: Future[Seq[Int]] = Future.sequence(news)

      loadedNews.onComplete {
        case Success(ln) => println(s"Loaded files number: ${ln.size}. Loaded news number: ${ln.sum}. Total time duration: ${System.currentTimeMillis() - starter} milliseconds.")
        case Failure(ex) => println(s"Error: ${ex.getMessage}")
      }

      Thread.sleep(5000)
    }

}
