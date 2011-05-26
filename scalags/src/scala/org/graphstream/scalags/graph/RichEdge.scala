package org.graphstream.scalags.graph

import org.graphstream.graph._

class RichEdge( edge:Edge ) extends RichElement[Edge]( edge ) with Edge {
	
	def isDirected():Boolean = element.isDirected
	def isLoop():Boolean = element.isLoop
	def getNode0[T<:Node]():T = element.getNode0[T]
	def getNode1[T<:Node]():T = element.getNode1[T]
	def getSourceNode[T<:Node]():T = element.getSourceNode[T]
	def getTargetNode[T<:Node]():T = element.getTargetNode[T]
	def source:Node = element.getNode0[Node]
	def target:Node = element.getNode1[Node]
	def getOpposite[T<:Node]( node:T ):T = element.getOpposite[T]( node )
	def opposite[T<:Node](node:T):T = element.getOpposite[T](node)
	
	def apply(i:Int):Node = {
		if(i==0) getNode0[Node]
		else if(i==1) getNode1[Node]
		else null
	}

	override def toString():String = element.toString
	
// Depreciated

	def setDirected( on:Boolean ) {}
	def switchDirection() {}
}