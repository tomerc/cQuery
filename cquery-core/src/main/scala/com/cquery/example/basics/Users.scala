package com.cquery.example.basics

import scala.concurrent.Future
import com.websudos.phantom.dsl._

case class User(
                 id: UUID,
                 email: String,
                 name: String,
                 registrationDate: DateTime
               )

class Users extends CassandraTable[ConcreteUsers, User] {

  object id extends UUIDColumn(this) with PartitionKey[UUID]
  object email extends StringColumn(this)
  object name extends StringColumn(this)
  object registrationDate extends DateTimeColumn(this)

  def fromRow(row: Row): User = {
    User(
      id(row),
      email(row),
      name(row),
      registrationDate(row)
    )
  }
}

// The root connector comes from import com.websudos.phantom.dsl._
abstract class ConcreteUsers extends Users with RootConnector {

  def store(user: User): Future[ResultSet] = {
    insert.value(_.id, user.id).value(_.email, user.email)
      .value(_.name, user.name)
      .value(_.registrationDate, user.registrationDate)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getById(id: UUID): Future[Option[User]] = {
    select.where(_.id eqs id).one()
  }
}