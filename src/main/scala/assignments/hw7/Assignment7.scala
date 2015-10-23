package assignments.hw7

import java.io.PrintWriter

import scalacache._
import memoization._
import guava._


case class CandyBag(cherry: Double, lime: Double, likelihood: Double)

object Assignment7 extends App {

  val hypotheses = IndexedSeq(
    CandyBag(cherry = 1.0, lime = 0.0, likelihood = .10),
    CandyBag(cherry = .75, lime = .25, likelihood = .20),
    CandyBag(cherry = .50, lime = .50, likelihood = .40),
    CandyBag(cherry = .25, lime = .75, likelihood = .20),
    CandyBag(cherry = 0.0, lime = 1.0, likelihood = .10)
  )

  // type alias
  type Hypothesis = CandyBag
  type Observation = CandyBag => Double

  // two types of observations, cherry or lime candy
  val Cherry: Observation = (cb: CandyBag) => cb.cherry // function that gets the cherry percentage given a candy bag
  val Lime  : Observation = (cb: CandyBag) => cb.lime   // likewise for lime

  // parse the optional input argument of observations
  val observations = args.headOption.getOrElse("").flatMap {
    case 'C' => Some(Cherry)
    case 'L' => Some(Lime)
    case _   => None
  }

  // use to cache hypothesisPr function calls to significantly reduce
  // the number of recursive calls as the number of observations increases.
  implicit val scalaCache = ScalaCache(GuavaCache())

  // compute the probability of a hypothesis given sequence of observations.
  def hypothesisPr(obs: Seq[Observation], hypo: Hypothesis): Double = memoizeSync {
    if (obs.isEmpty) hypo.likelihood
    else obs(0)(hypo) * hypothesisPr(obs.tail, hypo) / obsPr(obs.head, obs.tail)
  }

  // compute the probability that the next observation will be of a specific type,
  // priors for different hypotheses, and given a sequence of observations.
  def obsPr(nextObs: Observation, givenObs: Seq[Observation]): Double =
    hypotheses.map(hypo => nextObs(hypo) * hypothesisPr(givenObs, hypo)).sum

  val output =
    f"""|Observation sequence Q: ${args.headOption.getOrElse("")}
        |Length of Q: ${observations.size}
        |
        |P(h1 | Q) = ${hypothesisPr(observations, hypotheses(0))}%.5f
        |P(h2 | Q) = ${hypothesisPr(observations, hypotheses(1))}%.5f
        |P(h3 | Q) = ${hypothesisPr(observations, hypotheses(2))}%.5f
        |P(h4 | Q) = ${hypothesisPr(observations, hypotheses(3))}%.5f
        |P(h5 | Q) = ${hypothesisPr(observations, hypotheses(4))}%.5f
        |
        |Probability that the next candy we pick will be C, given Q: ${obsPr(Cherry, observations)}%.5f
        |Probability that the next candy we pick will be L, given Q: ${obsPr(Lime, observations)}%.5f
    """.stripMargin

  println(output)
  new PrintWriter("result.txt") { write(output); close() } // write to file
}
