package org.graphstream.ui.sgeom

class Vector2( x:Double, y:Double ) extends org.graphstream.ui.geom.Vector2( x, y ) {

	def this( other:Vector2 ) {
		this( other.x, other.y )
	}
	
	def this( from:org.graphstream.ui.geom.Point2, to:org.graphstream.ui.geom.Point2 ) {
		this( to.x - from.x, to.y - from.y )
	}

	def this() { this( 0, 0 ) }
 
	def apply( component:Int ):Double = data( component )

	def update( component:Int, value:Double ) { data(component) = value }
 
	def x:Double = data(0)
	
	def y:Double = data(1)
}

object Vector2 {
	def apply( x:Double, y:Double ):Vector2 = new Vector2( x, y )

	def apply( p0:org.graphstream.ui.geom.Point2, p1:org.graphstream.ui.geom.Point2 ):Vector2 = new Vector2( p0, p1 )
 
	def apply( p0:org.graphstream.ui.geom.Point2, p1:org.graphstream.ui.geom.Point2, p2:org.graphstream.ui.geom.Point2 ):Vector2 = new Vector2(
			( p1.x - p0.x ) + ( p2.x - p1.x ),
			( p1.y - p0.y ) + ( p2.y - p1.y )
		)

	def apply( p0:org.graphstream.ui.geom.Point2, p1:org.graphstream.ui.geom.Point2, p2:org.graphstream.ui.geom.Point2, p3:org.graphstream.ui.geom.Point2 ):Vector2 = new Vector2(
			( p1.x - p0.x ) + ( p2.x - p1.x ) + ( p3.x - p2.x ),
			( p1.y - p0.y ) + ( p2.y - p1.y ) + ( p3.y - p2.y )
		)
}
