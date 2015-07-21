package bayes


case class Classification[C, F](category: C, features: Seq[F], probability: Float = 1.0f) {

}