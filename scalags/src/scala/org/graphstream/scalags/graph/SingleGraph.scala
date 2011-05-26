package org.graphstream.scalags.graph

object SingleGraph {
	def apply(id:String):MultiGraph = new MultiGraph(id)
	def apply(graph:org.graphstream.graph.implementations.SingleGraph):RichGraph = new RichGraph(graph)
}

class SingleGraph(id:String) extends org.graphstream.graph.implementations.SingleGraph(id) with GraphHelpers