package org.graphstream.scalags.stream

trait SinkAdapter {
	
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
	}

	def nodeAttributeChanged( sourceId:String,  timeId:Long, nodeId:String,  attribute:String,  oldValue:Object,  newValue:Object) {
	}

	def nodeAttributeRemoved( sourceId:String,  timeId:Long, nodeId:String,  attribute:String) {
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