package org.graphstream.scalags.graph

import scala.collection.mutable.WrappedArray
/**
 * Wrapper for GraphStream elements that provide some utility methods and resolve some implicit
 * convertion problems.
 */
class RichElement[T <:org.graphstream.graph.Element]( protected val element:T ) extends org.graphstream.graph.Element {
// New methods
    
	def id:String = element.getId
	def attributeCount = element.getAttributeCount
	def attributeKeys:java.lang.Iterable[String] = element.getAttributeKeySet
	def label(key:String):CharSequence = element.getLabel(key)
	def number(key:String):Double = element.getNumber(key)
	def attribute[T](key:String):T = element.getAttribute[T](key)
    
// Wrapper
	def getId():String 												= element.getId
	
	def getAttributeCount():Int	                                   	= element.getAttributeCount
	def getAttribute[T]( key:String ):T                          	= element.getAttribute[T]( key )
	def getAttribute[T]( key:String, clazz:Class[T] ):T          = element.getAttribute[T]( key, clazz )
	def getAttributeKeyIterator():java.util.Iterator[String]       	= element.getAttributeKeyIterator 
	def getAttributeKeySet():java.lang.Iterable[String]            	= element.getAttributeKeySet
	def getFirstAttributeOf[T]( keys:String* ):T                 	= element.getFirstAttributeOf[T]( keys:_* )
	def getFirstAttributeOf[T]( clazz:Class[T], keys:String* ):T 	= element.getFirstAttributeOf[T]( clazz, keys:_* )
	
	def getLabel( key:String ):CharSequence                      	= element.getLabel( key )
	def getNumber( key:String ):Double                           	= element.getNumber( key )
	def getVector( key:String ):java.util.ArrayList[_ <: Number] 	= element.getVector( key )
	def getArray( key:String ):Array[Object]                     	= element.getArray( key )
	def getHash( key:String ):java.util.HashMap[_,_]             	= element.getHash( key )

	def hasAttribute( key:String ):Boolean                 			= element.hasAttribute( key )
	def hasAttribute( key:String, clazz:Class[_] ):Boolean 			= element.hasAttribute( key, clazz )
	def hasLabel( key:String ):Boolean                     			= element.hasLabel( key )
	def hasNumber( key:String ):Boolean                    			= element.hasNumber( key )
	def hasVector( key:String ):Boolean                   			= element.hasVector( key )
	def hasArray( key:String ):Boolean                     			= element.hasArray( key )
	def hasHash( key:String ):Boolean                      			= element.hasHash( key )
	
	def addAttributes( attributes:java.util.Map[String,Object] ) 	= element.addAttributes( attributes )
	def removeAttribute( attribute:String )                      	= element.removeAttribute( attribute )
	def clearAttributes()                                        	= element.clearAttributes

	def addAttribute(attribute:String, values:Object*) = setAttribute( attribute, values )
	def changeAttribute(attribute:String, values:Object*) = setAttribute( attribute, values )
	def setAttribute(attribute:String, values:Object*) {
		values match {
			case warray:WrappedArray[Object] => {
				val data = values.asInstanceOf[WrappedArray[Object]].array

				data.length match {
					case 0 => element.setAttribute( attribute, new java.lang.Boolean( true ) ); 
					case 1 => element.setAttribute( attribute, data( 0 ) )
					case _ => element.setAttribute( attribute, data )
				}
			}
			case _ => {
				element.setAttribute( attribute, values )
			}
		}
	}

	
// Additional access
 
 	def apply(key:String):Any = element.getAttribute( key )
 
// Additional command

  	def update( key:String, value:Any ) { element.setAttribute( key, value.asInstanceOf[Object] ) }
  
  	def update( key:String, values:Product ) {
  		// How to convert a product to an array ?
  		val a = new Array[Object]( values.productArity )
  		var i = 0
    
  		while( i < values.productArity ) {
  			a(i) = values.productElement( i ).asInstanceOf[Object]
            i += 1
        }
    
  		element.setAttribute( key, a:_* )
  	}
}