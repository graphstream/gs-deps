package org.miv.mbox;

/**
 * Container for incoming messages.
 *
 * <p>
 * The message box acts as a buffer for incoming messages. Its major property is
 * to be usable from any thread. This allows any external source to post any
 * message at any time without having to wonder about synchronisation. 
 * </p>
 * 
 * <p>
 * This interface contains only one command: {@link #post(String, Object[])}
 * that allow to post messages in the message box. The way messages are
 * retrieved inside the box depends on particular implementations (after all,
 * only a postman can open mail boxes).
 * </p>
 *
 * @author Antoine Dutot
 * @since 20040624
 */
public interface MBox
{
// Commands

	/**
	 * Post a message in the message box. This method can be called from another
	 * thread.
	 * @param from Identifier of the sending object.
	 * @param data Array of posted objects.
	 */
	void
	post( String from, Object ... data ) throws CannotPostException;
}