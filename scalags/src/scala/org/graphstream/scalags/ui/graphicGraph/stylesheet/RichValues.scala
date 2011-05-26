package org.graphstream.scalags.ui.graphicGraph.stylesheet

import org.graphstream.ui.graphicGraph.stylesheet.Values
//import _root_.scala.collection.jcl.MutableIterator
import scala.collection.JavaConversions._

class RichValues( val values:Values ) {
//  	implicit def javaIterableToScalaIterator[A]( it:java.lang.Iterable[A] ) = new MutableIterator.Wrapper( it.iterator )

	def apply( i:Int ):Double = values.get( i )
	def contains( value:Double ):Boolean = {
  		var found = false
		values.foreach { v => if( v == value ) found = true }
  		found
	}
}
