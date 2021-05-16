package ru.mai.dep810

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}

object Helpers {
  private val DATASET_PATH = "./import/dataset/"

  implicit class SparkSessionReader(session: SparkSession){
    def loadDatasetFromCSV(datasetName: String) : RDD[Row]=
      session
        .read
        .format("com.databricks.spark.csv")
        .option("sep",",")
        .option("header","true")
        .option("inferSchema", "true")
        .load(f"${DATASET_PATH}/${datasetName}.csv")
        .rdd
  }
}
