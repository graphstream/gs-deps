package org.graphstream.scalags.ui

import org.graphstream.ui.swingViewer.ViewerListener

trait ViewerListenerTrait extends ViewerListener {
	def viewClosed( id:String ) {}
 	def buttonPushed( id:String ) {}
 	def buttonReleased( id:String ) {} 
}