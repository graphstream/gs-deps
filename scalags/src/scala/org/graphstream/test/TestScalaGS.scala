package org.graphstream.test

import org.graphstream.graph._
import org.graphstream.ScalaGS._
import org.graphstream.scalags.graph.MultiGraph

object TestScalaGS {
	def main(args:Array[String]):Unit = {
		val test = new Test
		test.testNodeImplicit
	}
}
 
class Test {
	def testNodeImplicit():Unit = {
		val graph = MultiGraph("g1")
  
		val A:Node = graph.addNode("A")
		graph.addNodes( "B", "C" )
  
		A("xyz") = (0, 1, 0)
		graph.node("B")("xyz") = ( 1, 0, 0 )
		graph.node("C")("xyz") = (-1, 0, 0 )
  
		graph.addEdges( "A", "B", "C", "A" )
  
		graph.display(false)
	}
}