package controllers

import play.api._
import play.api.mvc._

import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.Directory
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.util.Version

import java.io.File


object Application extends Controller {

  def search(indexDir: String, resNum: Int, q: String) : String = {
    val dir: Directory = FSDirectory.open(new File(indexDir))
    val reader: IndexReader = DirectoryReader.open(dir)
    val iSearcher: IndexSearcher = new IndexSearcher(reader)

    val parser: QueryParser = new QueryParser(Version.LUCENE_46,
                                              "contents",
                                              new EnglishAnalyzer(Version.LUCENE_46))
    val query: Query = parser.parse(q)
    val start = System.currentTimeMillis()
    val hits: TopDocs = iSearcher.search(query, resNum)
    val end = System.currentTimeMillis()

    System.out.println("Found " + hits.totalHits +
      " document(s) (in " + (end - start) +
      " milliseconds) that matched query '" +
      q + "':")

    val res = for { scoreDoc : ScoreDoc <- hits.scoreDocs
                    doc: Document = iSearcher.doc(scoreDoc.doc)} yield doc.get("header")
    reader.close()
    res.mkString(" ")
  }


  def showHome2 = Action {

    val indexDir = "/Users/oleg/kalinin/idx"
    val num = 20
    val query = "murder expirience"

    val xxx = search(indexDir, num, query)

    Ok(views.html.home(xxx))
  }


}