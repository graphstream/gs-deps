package org.graphstream.scalags.stream

import java.io.PrintStream

class SinkPrinter( val out:PrintStream ) extends org.graphstream.stream.Sink {
	
	def this() { this( System.out ) }
	
	def edgeAttributeAdded( sourceId:String,  timeId:Long,  edgeId:String, attribute:String,  value:Object) {
	}

	def edgeAttributeChanged( sourceId:String,  timeId:Long, edgeId:String,  attribute:String,  oldValue:Object,  newValue:Object) {
	}

	def edgeAttributeRemoved( sourceId:String,  timeId:Long, edgeId:String,  attribute:String) {
	}

	def graphAttributeAdded( sourceId:String,  timeId:Long, attribute:String,  value:Object) {
	}

	def graphAttributeChanged( sourceId:String,  timeId:Long, attribute:String,  oldValue:Object,  newValue:Object) {
	}

	def graphAttributeRemoved( sourceId:String,  timeId:Long, attribute:String) {
	}

	def nodeAttributeAdded( sourceId:String,  timeId:Long,  nodeId:String, attribute:String,  value:Object) {
		out.print( "nodeAttributeAdded(%s, %d, %s, %s, %s)%n".format( sourceId, timeId, nodeId, attribute, value ) )
	}

	def nodeAttributeChanged( sourceId:String,  timeId:Long, nodeId:String,  attribute:String,  oldValue:Object,  newValue:Object) {
		out.print( "nodeAttributeChanged(%s, %d, %s, %s, %s, %s)%n".format( sourceId, timeId, nodeId, attribute, oldValue, newValue ) )
	}

	def nodeAttributeRemoved( sourceId:String,  timeId:Long, nodeId:String,  attribute:String) {
		out.print( "nodeAttributeRemoved(%s, %d, %s, %s)%n".format( sourceId, timeId, nodeId, attribute ) )
	}

	def edgeAdded( sourceId:String,  timeId:Long,  edgeId:String, fromNodeId:String,  toNodeId:String, directed:Boolean) {
	}

	def edgeRemoved( sourceId:String,  timeId:Long,  edgeId:String) {
	}

	def graphCleared( sourceId:String,  timeId:Long) {
	}

	def nodeAdded( sourceId:String,  timeId:Long,  nodeId:String) {
	}

	def nodeRemoved( sourceId:String,  timeId:Long,  nodeId:String) {
	}

	def stepBegins( sourceId:String,  timeId:Long,  step:Double) {
	}
}
