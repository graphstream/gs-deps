package org.miv.pherd.ntree;

public interface NTreeListener
{
	/**
	 * A new cell appeared.
	 * @param id The cell identifier.
	 * @param parentId The parent cell identifier.
	 * @param lo The cell space lowest point. 
	 * @param hi The cell space highest point.
	 * @param index The cell index in its parent cell.
	 */
	void cellAdded( Object id, Object parentId, Anchor lo, Anchor hi, int depth, int index );
	
	/**
	 * A cell disappeared.
	 * @param id The cell identifier.
	 */
	void cellRemoved( Object id );
	
	/**
	 * The cell data changed. This method is here to allow cell data to send
	 * arbitrary messages to the listeners.
	 * @param id The cell identifier.
	 * @param message The message.
	 * @param data The message data.
	 */
	void cellData( Object id, String message, Object data );
}