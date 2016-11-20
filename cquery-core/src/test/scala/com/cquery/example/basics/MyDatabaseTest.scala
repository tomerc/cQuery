package com.cquery.example.basics

import org.scalatest.{BeforeAndAfterAll, FlatSpec}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by Deepa on 20/11/2016.
  */
class MyDatabaseTest extends FlatSpec with BeforeAndAfterAll  with Defaults.connector.Connector {

  override def beforeAll(): Unit = {
  super.beforeAll()
    Await.result(MyDatabase.autocreate().future(), 5.seconds)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Await.result(MyDatabase.autotruncate().future(), 5.seconds)
  }
}

