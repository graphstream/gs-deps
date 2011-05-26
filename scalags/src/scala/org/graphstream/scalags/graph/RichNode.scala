package org.graphstream.scalags.graph

import org.graphstream.graph._
import org.graphstream.stream._

class RichNode( node:Node ) extends RichElement[Node]( node ) with Node {
	
    def graph:Graph = element.getGraph
    
    def getGraph():Graph = element.getGraph
	def getDegree():Int  = element.getDegree
	def getOutDegree():Int = element.getOutDegree
	def getInDegree() = element.getInDegree
	def hasEdgeToward( id:String ):Boolean = element.hasEdgeToward( id )
	def hasEdgeFrom( id:String ):Boolean = element.hasEdgeFrom( id )
	def hasEdgeBetween( id:String ):Boolean = element.hasEdgeBetween( id )
	
	def degree:Int = element.getDegree
	def outDegree:Int = element.getOutDegree
	def inDegree:Int = element.getInDegree
	
	def getEdgeToward[T<:Edge]( id:String ):T = element.getEdgeToward[T]( id )
	def getEdgeFrom[T<:Edge]( id:String ):T = element.getEdgeFrom[T]( id )
	def getEdgeBetween[T<:Edge]( id:String ):T = element.getEdgeBetween[T]( id )
	def getEdge[T<:Edge]( i:Int ):T = element.getEdge[T]( i )
	
	def toward( id:String ):Option[Edge] = element.getEdgeToward( id ) match {
    	case e:Edge => Some[Edge]( e )
    	case _      => None
    }
    
    def from( id:String ):Option[Edge] = element.getEdgeFrom( id ) match {
    	case e:Edge => Some[Edge]( e )
    	case _      => None
    }
    
    def edge(i:Int):Edge = element.getEdge( i )

    def apply(i:Int):Edge = element.getEdge(i)
    
    def eachEdge[T<:Edge]:Iterable[T] = scala.collection.JavaConversions.asIterable(element.getEachEdge[T])
    def eachLeavingEdge[T<:Edge]:Iterable[T] = scala.collection.JavaConversions.asIterable(element.getEachLeavingEdge[T])
    def eachEnteringEdge[T<:Edge]:Iterable[T] = scala.collection.JavaConversions.asIterable(element.getEachEnteringEdge[T])
    def edges[T<:Edge]:java.util.Collection[T] = element.getEdgeSet[T]
    def leavingEdges[T<:Edge]:java.util.Collection[T] = element.getLeavingEdgeSet[T]
    def enteringEdges[T<:Edge]:java.util.Collection[T] = element.getEnteringEdgeSet[T]
    
	def getEdgeIterator[T<:Edge]():java.util.Iterator[T]         			= element.getEdgeIterator[T]
	def getEnteringEdgeIterator[T<:Edge]():java.util.Iterator[T] 			= element.getEnteringEdgeIterator[T]
	def getLeavingEdgeIterator[T<:Edge]():java.util.Iterator[T] 	 		= element.getLeavingEdgeIterator[T]
	def getNeighborNodeIterator[T<:Node]():java.util.Iterator[T]			= element.getNeighborNodeIterator[T]
	def getBreadthFirstIterator[T<:Node]():java.util.Iterator[T]			= element.getBreadthFirstIterator[T]
	def getBreadthFirstIterator[T<:Node]( directed:Boolean ):java.util.Iterator[T]	= element.getBreadthFirstIterator[T]( directed )
	def getDepthFirstIterator[T<:Node]():java.util.Iterator[T]			= element.getDepthFirstIterator[T]
	def getDepthFirstIterator[T<:Node]( directed:Boolean ):java.util.Iterator[T]	= element.getDepthFirstIterator[T]( directed )
	def iterator():java.util.Iterator[Edge]						= element.iterator
	def getEachEdge[T<:Edge]():java.lang.Iterable[T]					= element.getEachEdge[T]
	def getEachLeavingEdge[T<:Edge]():java.lang.Iterable[T]				= element.getEachLeavingEdge[T]
	def getEachEnteringEdge[T<:Edge]():java.lang.Iterable[T]				= element.getEachEnteringEdge[T]
	def getEdgeSet[T<:Edge]():java.util.Collection[T]					= element.getEdgeSet[T]
	def getLeavingEdgeSet[T<:Edge]():java.util.Collection[T]				= element.getLeavingEdgeSet[T]
	def getEnteringEdgeSet[T<:Edge]():java.util.Collection[T]				= element.getEnteringEdgeSet[T]
	
	def -- ( other:Node ):Node = { 
    	other.getGraph.addEdge( "%s--%s".format( element.getId, other.getId ), element.getId, other.getId, false )
    	other
    }
    
    def -> ( other:Node ):Node = {
    	other.getGraph.addEdge( "%s->%s".format( element.getId, other.getId ), element.getId, other.getId, true )
    	other
    }
    
    

	override def toString():String = element.toString
}