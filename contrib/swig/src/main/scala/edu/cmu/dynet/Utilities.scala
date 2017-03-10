package edu.cmu.dynet

import scala.language.implicitConversions

/** Helpers that don't fit anywhere else but that might make your life easier */
object Utilities {

  /** Shuffle indices (for, say, training in random order) */
  def shuffle(vs: IntVector): Unit = {
    val values = for (i <- 0 until vs.size) yield vs(i)
    scala.util.Random.shuffle(values).zipWithIndex.foreach { case (v, i) => vs.update(i, v) }
  }
  
  /** Sample from a discrete distribution */
  def sample(v: FloatVector): Int = {
    // random pick
    val p = scala.util.Random.nextFloat

    // Seq(0f, p(0), p(0) + p(1), .... )
    val cumulative = v.scanLeft(0f)(_ + _)

    // Return the largest index where the cumulative probability is <= p.
    // Since cumulative(0) is 0f, there's always at least one element in the
    // takeWhile, so it's ok to use .last
    cumulative.zipWithIndex
        .takeWhile { case (c, i) => c <= p }
        .last
        ._2
  }

  /** Augment numbers so that they can do arithmetic with expressions. */
  implicit class NumbersAsExpressions[T](x: T)(implicit n: Numeric[T]) {
    import n._
    def +(e: Expression): Expression = Expression.exprPlus(x.toFloat, e)
    def *(e: Expression): Expression = Expression.exprTimes(x.toFloat, e)
    def -(e: Expression): Expression = Expression.exprMinus(x.toFloat, e)
  }
}
