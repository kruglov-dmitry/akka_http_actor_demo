import dkruglov.akka_http_actor_demo.entities.Credentials

object TestHelpers extends randomSeed {

  def generateValidUsername(): String = {
    // [a-zA-Z][a-zA-Z0-9\.,\-_]{5,31}
    val lettersOnly = ('a' to 'z') ++ ('B' to 'Z')
    val possibleSymbols = lettersOnly ++ ('0' to '9') ++ Seq(',', '-', '_', '.')

    val MIN_LENGTH = 5

    val acc_length = 5 + rand.nextInt(26)
    val partName = for (i <- 0 to acc_length) yield possibleSymbols(rand.nextInt(possibleSymbols.size))

    val res = lettersOnly(rand.nextInt(lettersOnly.size)) :: partName.toList

    res.mkString("")
  }

  def getRandomValidCredentials: Credentials = {
    val userId = generateValidUsername()
    val password = userId.toUpperCase

    Credentials(userId, password)
  }

  def getRandomInvalidCredentials: Credentials = {
    val userId = generateValidUsername()
    val password = generateValidUsername()

    Credentials(userId, password)
  }
}
