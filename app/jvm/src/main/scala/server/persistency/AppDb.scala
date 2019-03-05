package server.persistency

import java.util.UUID

import com.typesafe.config.ConfigFactory
import shared.data.Person
import slick.jdbc.DatabaseUrlDataSource
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import slogging.LazyLogging

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

/**
  * Lets us save and retrieve data from a database.
  * <p>
  * Created by Matthias Braun on 1/12/2017.
  */
object AppDb extends LazyLogging {

  // Load from application.conf postgres base url configuration
  val dbDatasource=new DatabaseUrlDataSource()
  private val db = Database.forDataSource(dbDatasource,maxConnections = Some(20))

  private val persons = TableQuery[PersonTable]

  setUpDb()

  private def setUpDb(): Unit = {
    try {
      // Get all existing tables
      // We block here since the schema will be created when this object is used for the first time and we don't
      // want the schema to be unavailable when, for example, saving a person
      val tables = Await.result(db.run(MTable.getTables), 10 seconds)

      val personTableName = persons.baseTableRow.tableName
      if(tables.exists(existingTable => existingTable.name.name == personTableName)) {
        logger.info("Table '{}' already exists", personTableName)
      } else {
        logger.info("Creating table '{}'", personTableName)
        Await.result(db.run(persons.schema.create), 10 seconds)
      }
      logger.info("Finished setting up database")
    } catch {
      case error: Throwable => logger.warn("Could not set up database. " +
        "Is the log directory defined in docker-compose.yml writable for the Postgres user?", error)
    }
  }

  class PersonTable(tag: Tag) extends Table[Person](tag, "persons") {
    def id = column[UUID]("id", O.PrimaryKey)

    def name = column[String]("name")

    def age = column[String]("age")

    def occupation = column[String]("occupation")

    // The default projection tells slick how to create a person from a tuple and vice versa
    def * = (id, name, age, occupation).mapTo[Person]
  }

  /** Gets all the people from the database */
  def getAllPeople: Future[Try[Seq[Person]]] = db.run(persons.result.asTry)

  /** Saves a couple of persons in the database */
  def save(personsToAdd: Seq[Person]): Future[Try[Option[Int]]] = {
    val saveAction = persons ++= personsToAdd
    db.run(saveAction.asTry)
  }
}
