package spoker

import spoker.hand.Hand
import spoker.hand.HandRanking._

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import org.scalacheck.Gen

@RunWith(classOf[JUnitRunner])
class HandSpec extends FlatSpec with Matchers with PropertyChecks {

  "two equal hands" should "rank equally" in {
    forAll(distinctCards(3)) { cards =>
      val hand1 = new Card(Three, Spades) +: new Card(Two, Diamonds) +: cards
      val hand2 = new Card(Three, Diamonds) +: new Card(Two, Spades) +: cards

      Hand(hand1) should equal(Hand(hand2))
    }
  }

  val anyRank: Gen[Rank.Value] = Gen.oneOf(Rank.values.toSeq)
  val anySuit: Gen[Suit.Value] = Gen.oneOf(Suit.values.toSeq)

  val anyCard: Gen[Card] = for {
    r <- anyRank
    s <- anySuit
  } yield new Card(r, s)

  val allCards = for {
    r <- Rank.values
    s <- Suit.values
  } yield new Card(r, s)

  def anyCards(n: Int): Gen[Cards] = Gen.listOfN(n, anyCard)

  def distinctCards(n: Int): Gen[Cards] = Gen.pick(n, allCards)

  implicit override val generatorDrivenConfig = PropertyCheckConfiguration(
    minSuccessful = 100,
    maxDiscardedFactor = 15)
}