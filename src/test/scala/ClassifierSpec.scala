import bayes._
import org.scalatest._

import scala.collection.mutable
import scala.io.Source
import scala.reflect.io.File

class ClassifierSpec extends FlatSpec with Matchers {

  it should "should count features correctly" in {
    val classifier: NaiveBayesClassifier[String, String] = new NaiveBayesClassifier()
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Beijing", "Chinese")))
    classifier.classCount.get("c") shouldBe  Some(mutable.HashMap(("Chinese",2),("Beijing", 1)))
  }

  it should "count training documents correctly" in {
    val classifier: NaiveBayesClassifier[String, String] = new NaiveBayesClassifier()
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Beijing", "Chinese")))
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Beijing", "Chinese")))
    classifier.classes.get("c") shouldBe Some(2)
  }

  it should "classify documents correctly" in {
    val classifier: NaiveBayesClassifier[String, String] = new NaiveBayesClassifier()
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Beijing", "Chinese")))
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Chinese", "Shanghai")))
    classifier.learn(Classification[String, String]("c", Seq("Chinese", "Macao")))
    classifier.learn(Classification[String, String]("j", Seq("Tokyo", "Japan", "Chinese")))
    classifier.classify(Seq("Chinese", "Chinese", "Chinese", "Tokyo", "Japan"))._1 shouldBe "c"
  }

  it should "classify correctly the test corpus" in {
    val classifier: NaiveBayesClassifier[String, String] = new NaiveBayesClassifier()
    loadFile("r8-train-all-terms.txt").foreach(c => {
      classifier.learn(c)
    })
    val booleanToInt: Map[Boolean, Int] = loadFile("r8-test-all-terms.txt").map(c => {
      val detailed: Iterable[(String, Double)] = classifier.classifyDetailed(c.features)
      //println(s"Expected: ${c.category}. Actual: ${detailed.head._1}")
      c.category == detailed.head._1
    }).groupBy(b => b).map(t => (t._1, t._2.length))
    val correct = booleanToInt.get(true).head
    val wrong= booleanToInt.get(false).head
    println(s"${(correct.toDouble / (correct + wrong)) * 100}")
    (correct.toDouble / (correct + wrong)) * 100 should be > 75.0
  }

  it should "classify correctly the news247 corpus" in {
    val classifier: NaiveBayesClassifier[String, String] = new NaiveBayesClassifier()
    loadFile("news247-train.txt").foreach(c => {
      classifier.learn(c)
    })
    val booleanToInt: Map[Boolean, Int] = loadFile("news247-test.txt").map(c => {
      val detailed: Iterable[(String, Double)] = classifier.classifyDetailed(c.features)
      println(s"Expected: ${c.category}. Actual: ${detailed.head._1}")
      c.category == detailed.head._1
    }).groupBy(b => b).map(t => (t._1, t._2.length))
    val correct = booleanToInt.getOrElse(true, 0)
    val wrong= booleanToInt.getOrElse(false, 0)
    println(s"${(correct.toDouble / (correct + wrong)) * 100}")
    (correct.toDouble / (correct + wrong)) * 100 should be >= 50.0
  }

  def loadFile(f: String) : Seq[Classification[String, String]]  = {
    val source = Source.fromURL(getClass.getResource(s"/${f}"))
    source.getLines().map(t => {
      val seq: Seq[String] = t.split("\t").toSeq
      (seq.head, seq(1))
    }).map(l => {
      Classification(l._1, l._2.split(" ").toSeq)
    }).toSeq
  }

}
