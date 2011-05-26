package org.graphstream.scalags.graph

object MultiGraph {
	def apply(id:String):MultiGraph = new MultiGraph(id)
	def apply(graph:org.graphstream.graph.implementations.MultiGraph):RichGraph = new RichGraph(graph)
}

class MultiGraph(id:String) extends org.graphstream.graph.implementations.MultiGraph(id) with GraphHelpers