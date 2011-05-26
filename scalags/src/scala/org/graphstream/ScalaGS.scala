package org.graphstream

import scala.collection.JavaConversions._

import org.graphstream.graph._
import org.graphstream.scalags.graph._
import org.graphstream.stream._
import org.graphstream.ui.graphicGraph.stylesheet.Values
import org.graphstream.scalags.ui.graphicGraph.stylesheet.RichValues

/**
 * Some type definitions and implicit conversions to ease the use of GraphStream Java in Scala. 
 */
object ScalaGS {
	implicit def elementToRichElement( element:Element ):RichElement[Element] = new RichElement[Element]( element )
	implicit def nodeToRichNode( node:Node ):RichNode = new RichNode( node )
	implicit def valuesToRichValues( values:Values ):RichValues = new RichValues( values )
	implicit def graphToRichGraph( graph:Graph ):RichGraph = new RichGraph( graph )
	implicit def edgeToRichEdge( edge:Edge ):RichEdge = new RichEdge( edge )
}

//object Graph {
//	def apply( id:String ):RichGraph = new RichGraph( new org.graphstream.graph.implementations.MultiGraph( id ) )
//}