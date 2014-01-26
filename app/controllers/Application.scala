package controllers

import play.api._
import play.api.mvc._

import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.search._
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.Directory
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.util.Version

import java.io.{StringReader, File}
import org.apache.lucene.queries.mlt.{MoreLikeThis, MoreLikeThisQuery}
import lib.LangCluster
import com.mongodb.casbah.Imports._


object Application extends Controller {

  def search(indexDir: String, resNum: Int, q: String) : String = {
    val dir: Directory = FSDirectory.open(new File(indexDir))
    val reader: IndexReader = DirectoryReader.open(dir)
    val iSearcher: IndexSearcher = new IndexSearcher(reader)

    val mlt: MoreLikeThisQuery = new MoreLikeThisQuery("success", Array("contents"), new EnglishAnalyzer(Version.LUCENE_46), "contents")

//    val parser: QueryParser = new QueryParser(Version.LUCENE_46,
//                                              "contents",
//                                              new EnglishAnalyzer(Version.LUCENE_46))
//    val mlt: Query = parser.parse(q)

    val start = System.currentTimeMillis()
    val hits: TopDocs = iSearcher.search(mlt, resNum)
    val end = System.currentTimeMillis()

    System.out.println("Found " + hits.totalHits +
      " document(s) (in " + (end - start) +
      " milliseconds) that matched query '" +
      q + "':")

    val res = for { scoreDoc : ScoreDoc <- hits.scoreDocs
                    doc: Document = iSearcher.doc(scoreDoc.doc)}
                yield doc.get("header")
    reader.close()
    res.mkString("\n<BR />")
  }


  def showHome2 = Action {

//    val langModel = new LangCluster
//    langModel.loadBasis()

    val indexDir = "/Users/oleg/kalinin/idx"
    val num = 20
    val query = "murder expirience"

//    val xxx = search(indexDir, num, query)

    val mongoClient = MongoClient()
    val db = mongoClient("bookmine")

    val meta = db("meta")

    val res2 = for {x <- meta.find().take(10)
                          } yield x.as[String]("_id")


    Ok(views.html.home(res2.toIterable))
  }


}