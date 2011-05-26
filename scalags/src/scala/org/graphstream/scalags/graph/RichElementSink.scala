package org.graphstream.scalags.graph

/**
 * A trait for element sink wrappers.
 */
trait RichElementSink[T <: org.graphstream.stream.ElementSink] {
	protected val element:T
 
 	def nodeAdded( sourceId:String, timeId:Long, nodeId:String )	= element.nodeAdded( sourceId, timeId, nodeId )
	def nodeRemoved( sourceId:String, timeId:Long, nodeId:String )	= element.nodeRemoved( sourceId, timeId, nodeId )
	def edgeAdded( sourceId:String, timeId:Long, edgeId:String,
             fromNodeId:String, toNodeId:String, directed:Boolean ) = element.edgeAdded( sourceId, timeId, edgeId, fromNodeId, toNodeId, directed )
	def edgeRemoved( sourceId:String, timeId:Long, edgeId:String )	= element.edgeRemoved( sourceId, timeId, edgeId )
	def graphCleared( sourceId:String, timeId:Long )				= element.graphCleared( sourceId, timeId )
	def stepBegins( sourceId:String, timeId:Long, step:Double )		= element.stepBegins( sourceId, timeId, step )
}