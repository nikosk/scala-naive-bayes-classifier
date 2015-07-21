package bayes

import scala.collection.mutable

class NaiveBayesClassifier[C, F] {

  val classCount = mutable.HashMap[C, mutable.HashMap[F, Int]]()
  val featureCount = mutable.HashMap[F, Int]()
  val classes = mutable.HashMap[C, Int]()

  def learn(c: Classification[C, F]) {
    c.features.foreach(f => {
      val feat: mutable.HashMap[F, Int] = classCount.getOrElseUpdate(c.category, mutable.HashMap[F, Int]())
      feat.put(f, 1 + feat.getOrElse(f, 0))
      featureCount.put(f, 1 + featureCount.getOrElse(f, 0))
    })
    classes.put(c.category, 1 + classes.getOrElse(c.category, 0))
  }

  def classify(doc: Seq[F]): (C,Double) = {
    classifyInternal(doc).maxBy(_._2)
  }

  def classifyDetailed(doc:Seq[F]): Iterable[(C, Double)] = {
    classifyInternal(doc).sortWith((a,b) => a._2 > b._2)
  }

  private def classifyInternal(doc: Seq[F]): Seq[(C, Double)] = {
    val vocabulary = featureCount.keys.size
    val map: Iterable[(C, Double)] = classes.keys.map(c => {
      val featureSet: mutable.HashMap[F, Int] = classCount.get(c).head
      val classPrior: Double = classes.getOrElse(c, 0) / classes.values.sum.toFloat
      val wordCount: Int = featureSet.values.sum
      val denominator: Int = wordCount + vocabulary
      val fl: Double = doc.foldLeft(classPrior)((p, f) => {
        val fc: Int = 1 + featureSet.getOrElse(f, 0)
        p * (fc.toDouble / denominator)
      })
      (c,fl)
    })
    map.toSeq
  }

}
