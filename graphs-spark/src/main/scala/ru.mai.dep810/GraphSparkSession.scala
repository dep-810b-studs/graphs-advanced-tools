package ru.mai.dep810

import org.apache.spark.sql.SparkSession

object GraphSpark {

  case class VerticeInfo(countPosts: Int, engagementGrade: Double)

  def InitSession() = SparkSession
    .builder()
    .master("local[*]")
    .appName("graphTest")
    .getOrCreate()

}
