package actors

import akka.actor.{Actor, ActorLogging}
import play.api.db.Database

import scala.util.Properties

/**
  * Created by carlos on 30/01/17.
  */
class CQRSEventHandler(database: Database) extends Actor with ActorLogging {

  override def preStart(): Unit = {
    // Subscribes to all messages that match the Event trait and delivers them to this actor
    context.system.eventStream.subscribe(self, classOf[Event])
  }

  override def receive = {
    case UserRegistered(phoneNumber, userName, timestamp) => //TODO

    case ClientEvent(phoneNumber, userName, MentionSubscribed(timestamp), _) =>
      database.withTransaction { sql =>
        // Creates an INSERT INTO ... SELECT statement

        sql.insertInto(MENTION_SUBSCRIPTIONS)
          .columns(
            MENTION_SUBSCRIPTIONS.USER_ID,
            MENTION_SUBSCRIPTIONS.CREATED_ON
          )
          .select(
            // Creates the SELECT statement (the select method is provided by the wildcard import of the DSL class)
            select(
              TWITTER_USER.ID,
              // Inserts the timestamp as a constant value using the value method
              value(new Timestamp(timestamp.getMillis))
            )
            .from(TWITTER_USER)
            .where(
              TWITTER_USER.PHONE_NUMBER.equal(phoneNumber)
              .and(
                TWITTER_USER.TWITTER_USER_NAME.equal(userName)
              )
            )
          ).execute()
      }

    case ClientEvent(phoneNumber, userName, MentionReceived(id, created_on, from, text, timestamp), _) =>
      //TODO
  }

}
