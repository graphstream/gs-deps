package org.graphstream.scalags.graph

import org.graphstream.graph.{Node, Edge}

/**
 * A trait that adds some utility methods to the Graph interface.
 */
trait GraphHelpers extends org.graphstream.graph.Graph {
// Access
  
	def node(id:String):RichNode = new RichNode(getNode( id ))
	def edge(id:String):RichEdge = new RichEdge(getEdge( id ))
  
	//def edgeSet[T<:Edge]():Collection[T] = getEdgeSet[T]
	
	def / (id:String):RichNode = new RichNode(getNode(id))
	
	def - (id:String):RichEdge = new RichEdge(getEdge(id))
	
// Graph Construction
  
	/**
     * Add several nodes at once.
     * @param ids A variable set of one or more identifiers, one for each node.
     * @return A list of created nodes.
     */
	def addNodes( ids:String* ):List[RichNode] = {
		val list = new _root_.scala.collection.mutable.ListBuffer[RichNode]
		ids.foreach { id => list += new RichNode( addNode( id ) ) }
		list.toList
	}
 
	/**
     * Add several edges at once. The edges are created from two consecutive identifiers in the
     * given set. Edge idenfiers are computed using the simple concatenation of the two nodes
     * identifiers.  For example if there are three identifiers "A", "B", and "C", two edges will
     * be created. The first will be named "AB" between "A" and "B", the second will be "BC"
     * between "B" and "C".
     * @param nodeIds A variable set of two or more identifiers, each successive pair of identifiers
     * forms an edge.
     * @return A list of created edges.
     */
	def addEdges( nodeIds:String* ):List[RichEdge] = {
		val list = new _root_.scala.collection.mutable.ListBuffer[RichEdge]
        if( nodeIds.length > 1 ) {
        	var oldId = nodeIds( 0 )
        	var i     = 1
        	while( i < nodeIds.length ) {
        		var newId = nodeIds( i )
        		list += new RichEdge( addEdge( "%s--%s".format( oldId, newId ), oldId, newId ) )
        		i    += 1
        		oldId = newId
        	}
        }
		list.toList
	}
}