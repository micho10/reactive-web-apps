package modules

import javax.inject.Inject

import com.google.inject.AbstractModule
import play.api.db.Database
import play.api.libs.Crypto
// Imports the generated Table classes in order to use them in the DSL
//import generated.Tables._

/**
  * Created by carlos on 23/01/17.
  */
class Fixtures @Inject() (val cryto: Crypto, val db: Database) extends DatabaseFixtures {
  // Obtains a transaction from Play's DB helper
  db.withTransaction { connection =>
    // Creates a jOOQ DSLContext using the JDBC connection
    val sql = DSL.using(connection, SQLDialect.POSTGRES_9_4)

    // Checks the number of existing users with jOOQ
    if (sql.fetchCount(USER) == 0) {
      val hashedPassword = crypto.sign("secret")
      sql
        .insertInto(USER)
        .columns(
          USER.EMAIL, USER.FIRSTNAME, USER.LASTNAME, USER.PASSWORD
        ).values(
        "bob@marley.org", "Bob", "Marley", hashedPassword
        // Executes the statement
        ).execute()
    }
  }

}



trait DatabaseFixtures



class FixturesModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[DatabaseFixtures])
      .to(classOf[Fixtures]).asEagerSingleton()
  }
}
