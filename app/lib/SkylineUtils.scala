package lib

import java.io.{PrintWriter, File}
import scala.collection.mutable
import scala.io.Source

import com.redis._


/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 17/01/14
 * Time: 21:27
 */

object SkylineUtils {

  val GENRES_DIR = "/Users/oleg/genres/"

  type Vocabulary = mutable.HashMap[String, Double]
  type LangBasis = mutable.HashMap[String, Vocabulary]

  val redis = new RedisClient("localhost", 6379)

  def andTree(dir: File, ext: String): List[File] = {
    val fileList = dir.listFiles.toList
    (fileList filter(f => f.isFile && f.getName.endsWith(ext))) :::
      (fileList filter(f => f.isDirectory) flatMap (f => andTree(f, ext) ))
  }


  def loadVector(path: String): Vocabulary = {
    val res = new Vocabulary
    for ((line,i)  <- Source.fromFile(path).getLines().zipWithIndex) {
      val a :: b :: _ = line.split(' ').toList
      res(a) = b.toDouble
    }
    redis.hmset(path, res)
    res
  }
  
  
  def saveVector(dict: Vocabulary, filePath: String) {
    val writer = new PrintWriter(new File(filePath))
    try {
      for ((word, count) <- dict) {
        writer.println(word + " " + count)
      }
    } finally {
      writer.close()
    }
  }
  
  
  def updateVector(res: Vocabulary, dict1: Vocabulary) {
    for ((word, count) <- dict1) {
      res(word) = res.getOrElse(word, 0.0) + count
    }
  }


  def vectorLength(vec: Vocabulary): Double = {
    math.sqrt(vec.values.map(x => x * x).sum)
//  vec.values.sum
  }


  def normalizeVector(vec: Vocabulary): Vocabulary = {
    val norm = vectorLength(vec)
    for ((genre, v) <- vec)
      yield (genre, v / norm)
  }

  def reverseVector(vec: Vocabulary): Vocabulary = {
    for ((genre, v) <- vec)
    yield (genre, -v)
  }


}
