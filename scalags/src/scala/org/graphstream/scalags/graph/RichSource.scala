package org.graphstream.scalags.graph

import org.graphstream.graph.{Element, Graph, Node, Edge, NodeFactory, EdgeFactory}
import org.graphstream.stream.{Sink, Source, ElementSink, AttributeSink}

/**
 * A trait for wrappers around sources.
 */
trait RichSource[T <: Source] {
	protected val element:T
 
	def addSink( listener:Sink )						= element.addSink( listener )
	def removeSink( listener:Sink )						= element.removeSink( listener )
	def addAttributeSink( listener:AttributeSink )		= element.addAttributeSink( listener )
	def addElementSink( listener:ElementSink )			= element.addElementSink( listener )
	def removeAttributeSink( listener:AttributeSink )	= element.removeAttributeSink( listener )
	def removeElementSink( listener:ElementSink )		= element.removeElementSink( listener )
	def clearElementSinks()								= element.clearElementSinks
	def clearAttributeSinks()							= element.clearAttributeSinks
	def clearSinks()									= element.clearSinks
}