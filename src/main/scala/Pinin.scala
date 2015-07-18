import java.net.URL

import de.l3s.boilerpipe.extractors.ArticleExtractor
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.jsoup.{Connection, Jsoup}
import collection.JavaConversions._
import scala.collection.Iterable

object Pinin {

  def main(args: Array[String]): Unit = {
    println(s"Running pinin for ${args(0)}")
    val url: String = args(0)
    val connect: Connection = Jsoup.connect(url)
    val document: Document = connect.get()
    val tag: Elements = document.body().getElementsByTag("H1")
    val text: String = ArticleExtractor.INSTANCE.getText(new URL(url))
    println(text)
    val classifier: BayesClassifier[String, String] = new BayesClassifier[String, String]
    val split: Array[String] = "συνθήκη Βερσαλλιών συμφωνία προαπαιτούμενα μέτρα υπουργός Οικονομικών Γιάνης Βαρουφάκης ομιλία ελληνικού λαού".split("\\s")
    classifier.learn("sports", "πηγαδάκια Φύσσα Βόκολο Εσιέν μέσος στην αίθουσα αγωνιστική κατάσταση Εσιέν φουλ πρόγραμμα.".split("\\s").toSeq)
    classifier.learn("politics", text.split("\\s").toSeq)
    println(classifier.classify(text.split("\\s").toSeq))
  }

}


class Classifier[C, F] {

  def learn(classification: Classification[C,F]) {

  }

}

case class Classification[C, F](category: C, features: F, probability: Float = 1.0) {

}