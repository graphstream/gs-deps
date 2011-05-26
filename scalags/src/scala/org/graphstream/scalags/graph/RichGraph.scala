package org.graphstream.scalags.graph


import org.graphstream.graph.{Element, Graph, Node, Edge, NodeFactory, EdgeFactory}
import org.graphstream.stream.{Sink, Source, ElementSink, AttributeSink}

/**
 * A wrapper for Java graphs that gives some more usage patterns.
 */
class RichGraph( graph:Graph )
	extends RichElement[Graph]( graph )
	with Graph
	with RichSource[Graph]
	with RichElementSink[Graph]
	with RichAttributeSink[Graph]
	with GraphHelpers
{
// Access
	
	def getNode[T<:Node]( id:String ):T			= element.getNode[T]( id )
	def getEdge[T<:Edge]( id:String ):T			= element.getEdge[T]( id )
	def getNodeCount():Int					= element.getNodeCount
	def getEdgeCount():Int					= element.getEdgeCount
	def getNodeIterator[T<:Node]():java.util.Iterator[T]	= element.getNodeIterator[T]
	def getEdgeIterator[T<:Edge]():java.util.Iterator[T]	= element.getEdgeIterator[T]
	def getEachNode[T<:Node]():java.lang.Iterable[_<:T]	= element.getEachNode[T]
	def getEachEdge[T<:Edge]():java.lang.Iterable[_<:T]	= element.getEachEdge[T]
	def getNodeSet[T<:Node]():java.util.Collection[T]	= element.getNodeSet[T]
	def getEdgeSet[T<:Edge]():java.util.Collection[T]	= element.getEdgeSet[T]
	def nodeFactory():NodeFactory[_<:Node]			= element.nodeFactory
	def edgeFactory():EdgeFactory[_<:Edge]			= element.edgeFactory
	def isStrict():Boolean					= element.isStrict
	def isAutoCreationEnabled():Boolean			= element.isAutoCreationEnabled
	def nullAttributesAreErrors():Boolean	= element.nullAttributesAreErrors
	def getStep():Double					= element.getStep
	def iterator():java.util.Iterator[Node]			= element.iterator
 
// Command
 
	def setNodeFactory( nf:NodeFactory[_<:Node] )	= element.setNodeFactory( nf )
	def setEdgeFactory( ef:EdgeFactory[_<:Edge] )	= element.setEdgeFactory( ef )
	def setStrict( on:Boolean )			= element.setStrict( on )
	def setAutoCreate( on:Boolean )			= element.setAutoCreate( on )
	def setNullAttributesAreErrors(on:Boolean) = element.setNullAttributesAreErrors(on)

// Graph Construction
	
	def clear()									= element.clear
	def addNode[T<:Node]( id:String ):T						= element.addNode[T]( id )
	def removeNode[T<:Node]( id:String ):T						= element.removeNode[T]( id )
	def addEdge[T<:Edge]( id:String, node1:String, node2:String ):T			= element.addEdge[T]( id, node1, node2 )
	def addEdge[T<:Edge]( id:String, from:String, to:String, directed:Boolean ):T	= element.addEdge[T]( id, from, to, directed )
	def removeEdge[T<:Edge]( from:String, to:String ):T				= element.removeEdge[T]( from, to )
	def removeEdge[T<:Edge]( id:String ):T						= element.removeEdge[T]( id )
	def stepBegins( time:Double )							= element.stepBegins( time )
 
// Source
	
	def attributeSinks():java.lang.Iterable[AttributeSink]	= element.attributeSinks
	def elementSinks():java.lang.Iterable[ElementSink]	= element.elementSinks
	
// Utility shortcuts
	
	def read( filename:String )														= element.read( filename )
	def read( input:org.graphstream.stream.file.FileSource, filename:String )		= element.read( input, filename )
	def write( filename:String )													= element.write( filename )
	def write( output:org.graphstream.stream.file.FileSink, filename:String )		= element.write( output, filename )
 	def display():org.graphstream.ui.swingViewer.Viewer								= element.display
	def display( autoLayout:Boolean ):org.graphstream.ui.swingViewer.Viewer			= element.display( autoLayout )
}