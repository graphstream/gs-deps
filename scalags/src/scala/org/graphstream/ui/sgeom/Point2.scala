package org.graphstream.ui.sgeom

class Point2(x:Double, y:Double) extends org.graphstream.ui.geom.Point2( x, y ) {
	def this(other:Point2) =this(other.x, other.y)
	def this() = this(0, 0)
}

object Point2 {
	def apply(x:Double, y:Double):Point2 = new Point2(x, y)
	def apply(other:Point2):Point2 = new Point2(other)
	implicit def toScalaPoint2(p:org.graphstream.ui.geom.Point2):Point2 = new Point2(p.x, p.y)
}