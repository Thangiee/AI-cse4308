package assignments

import java.io.PrintWriter

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext, mllib}

object SparkML {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Main").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val trainData: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "pen-train.txt")
    val testData: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "pen-test.txt")

    val model = DecisionTree.trainClassifier(trainData, 10, Map[Int, Int](), "gini", 10, 32)

    val labelAndPreds = testData.map { point =>
      val pred = model.predict(point.features)
      (point.label, pred)
    }

    val testErr = labelAndPreds.filter(r => r._1 != r._2).count().toDouble / testData.count()
    println("Test Error = " + testErr)
    println("Learned classification tree model:\n" + model.toDebugString)

    model.save(sc, "myDecisionTreeClassificationModel")
  }

}

object Format {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Main").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val data = sc.textFile("pendigits_test.txt")
      .map(_.split("\\s+").filter(_ != ""))
      .map(cols => cols.last +: cols.reverse.tail.reverse.zipWithIndex.map { case (v, i) => s"${i+1}:$v" } )
      .map(_.mkString(" "))

    new PrintWriter("pen-test.txt") { write(data.collect.mkString("\n")); close() }
  }
}
