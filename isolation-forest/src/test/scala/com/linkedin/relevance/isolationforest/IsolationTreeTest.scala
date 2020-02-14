package com.linkedin.relevance.isolationforest

import com.linkedin.relevance.isolationforest.Nodes.{ExternalNode, InternalNode}
import com.linkedin.relevance.isolationforest.TestUtils._
import org.testng.Assert
import org.testng.annotations.Test
import org.apache.spark.ml.linalg.Vectors

class IsolationTreeTest {

  @Test(description = "generateIsolationTreeTest")
  def generateIsolationTreeTest(): Unit = {

    val data = readCsv("src/test/resources/shuttle.csv")

    val dataArray = data.map(x => Vectors.dense(x.slice(0, data.head.length - 1).map(_.toDouble).toArray))  // Drop labels column

    val heightLimit = 15
    val randomState = new scala.util.Random(1)
    val featureIndicies = (0 until dataArray.head.size).toArray
    val root = IsolationTree
      .generateIsolationTree(dataArray, heightLimit, randomState, featureIndicies)

    Assert.assertEquals(root.subtreeDepth, heightLimit)
  }

  @Test(description = "pathLengthTest")
  def pathLengthTest(): Unit = {

    val leftChild = ExternalNode(10)
    val rightChild = ExternalNode(20)
    val root = InternalNode(leftChild, rightChild, 0, 1.5)

    val data1 = Vectors.dense(Array(1.0))
    val data2 = Vectors.sparse(1, Array(0), Array(2.0))

    val pathLength1 = IsolationTree.pathLength(data1, root)
    val pathLength2 = IsolationTree.pathLength(data2, root)

    Assert.assertEquals(pathLength1, 4.7488804f)
    Assert.assertEquals(pathLength2, 6.143309f)
  }
}
