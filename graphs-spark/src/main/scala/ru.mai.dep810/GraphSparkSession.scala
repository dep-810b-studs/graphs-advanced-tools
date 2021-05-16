package ru.mai.dep810

import org.apache.spark.sql.SparkSession

object GraphSpark {

  def InitSession(): SparkSession = {
    val session = SparkSession
      .builder()
      .master("local[*]")
      .appName("graphs")
      .getOrCreate()

    session.sparkContext.setLogLevel("WARN")
    session
  }
}
