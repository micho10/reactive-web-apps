package helpers

import javax.inject.Inject

import scala.concurrent.Future

/**
  * Created by carlos on 23/01/17.
  */
class Database @Inject() (db: play.api.db.Database) {

  def query[A](block: DSLContext => A): Future[A] = Future {
    db.withConnection { connection =>
      // Creates the jOOQ DSLContext
      val sql = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      // Invokes the function in the context of a database connection
      block(sql)
    }
    // Explicitly passes the custom database ExecutionContext so that the Future will be executed against it
  }(Contexts.database)


  def withTransaction[A](block: DSLContext => A): Future[A] = Future {
    db.withTransaction { connection =>
      val sql = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      block(sql)
    }
  }(Contexts.database)

}
