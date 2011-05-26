package org.graphstream.ui.sgeom

class Point3(x:Double, y:Double, z:Double) extends org.graphstream.ui.geom.Point3(x, y, z) {
	def this(other:Point3) =this(other.x, other.y, other.z)
	def this() = this(0, 0, 0)
}

object Point3 {
	def apply(x:Double, y:Double, z:Double):Point3 = new Point3(x, y, z)
	def apply(other:Point3):Point3 = new Point3(other)
	implicit def toScalaPoint3(p:org.graphstream.ui.geom.Point3):Point3 = new Point3(p.x, p.y, p.z)
}