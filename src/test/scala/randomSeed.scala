import scala.util.Random

trait randomSeed {
  val rand = new Random(System.currentTimeMillis())
}
