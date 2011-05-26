package org.graphstream.scalags.graph

/**
 * A trait for attribute sink wrappers.
 */
trait RichAttributeSink[T <: org.graphstream.stream.AttributeSink] {
	protected val element:T
  
 	def graphAttributeAdded( sourceId:String, timeId:Long, attribute:String, value:Object )		= element.graphAttributeAdded( sourceId, timeId, attribute, value )
	def graphAttributeChanged( sourceId:String, timeId:Long, attribute:String,
                         oldValue:Object, newValue:Object )										= element.graphAttributeChanged( sourceId, timeId, attribute, oldValue, newValue )
	def graphAttributeRemoved( sourceId:String, timeId:Long, attribute:String )					= element.graphAttributeRemoved( sourceId, timeId, attribute )
	def nodeAttributeAdded( sourceId:String, timeId:Long, nodeId:String, attribute:String, 
                         value:Object )															= element.nodeAttributeAdded( sourceId, timeId, nodeId, attribute, value )
	def nodeAttributeChanged( sourceId:String, timeId:Long, nodeId:String, attribute:String,
                         oldValue:Object, newValue:Object )										= element.nodeAttributeChanged( sourceId, timeId, nodeId, attribute, oldValue, newValue )
	def nodeAttributeRemoved( sourceId:String, timeId:Long, nodeId:String, attribute:String )	= element.nodeAttributeRemoved( sourceId, timeId, nodeId, attribute )
	def edgeAttributeAdded( sourceId:String, timeId:Long, edgeId:String, attribute:String,
                         value:Object )															= element.edgeAttributeAdded( sourceId, timeId, edgeId, attribute, value )
	def edgeAttributeChanged( sourceId:String, timeId:Long, edgeId:String, attribute:String,
                         oldValue:Object, newValue:Object )										= element.edgeAttributeChanged( sourceId, timeId, edgeId, attribute, newValue, oldValue )
	def edgeAttributeRemoved( sourceId:String, timeId:Long, edgeId:String, attribute:String )	= element.edgeAttributeRemoved( sourceId, timeId, edgeId, attribute )
}