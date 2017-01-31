package actors

import akka.actor.Actor
import org.jooq.util.postgres.PostgresDataType
import play.api.db.Database

import scala.concurrent.Future

/**
  * Created by carlos on 31/01/17.
  */
class CQRSQueryHandler(database: Database) extends Actor {

  implicit val ec = context.dispatcher

  override def receive = {
    case MentionsToday(phoneNumber) =>



  }


  def countMentions(phoneNumber: String): Future[Int] = {
    database.query { sql =>
      sql.selectCount().from(MENTIONS).where(
        // Fetches all of today's mentions
        MENTIONS.CREATED_ON.greaterOrEqual(currentDate()
          // Casts the variable type
          .cast(PostgresDataType.TIMESTAMP)
        )
          .and(MENTIONS.USER_ID.equal(
            sql.select (TWITTER_USER.ID)
              .from(TWITTER_USER)
              // Uses a subquery to retrieve the user's database identifier, given their phone number
              .where(TWITTER_USER.PHONE_NUMBER.equal(phoneNumber))
          ))
      ).fetchOne().value1()
    }
  }

}
