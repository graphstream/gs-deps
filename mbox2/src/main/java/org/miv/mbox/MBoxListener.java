package org.miv.mbox;

/**
* Message box listener.
*
* <p>
* This is the interface to implement in order to receive message from a
* {@link org.miv.mbox.MBoxStandalone}.
* </p>
*
* @see org.miv.mbox.MBoxStandalone
* @author Antoine Dutot
* @since 20040624
*/
public interface MBoxListener
{
//Commands

	/**
	 * A message has been received.
	 * @param from The message sender.
	 * @param data The messages to process.
	 */
	void processMessage( String from, Object data[] );
}