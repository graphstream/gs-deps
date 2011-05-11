package org.miv.mbox.net;

import java.io.*;

/**
 * Descendant of ByteArrayInputStream that allows to change the offset and
 * length.
 *
 * The standard ByteArrayInputStream can only wrap a fixed part of its
 * underlying byte array (this byte array being managed by another source). As
 * the ByteArrayInputStream is used as source for other streams, it is not
 * possible to create a new ByteArrayInputStream if the part of the underlying
 * byte array used to read changes. Therefore this class allows to change the
 * offset and length of the part to use. Furthermore, it also allows to change
 * the underlying byte array.
 *
 * @author Antoine Dutot
 * @since 20041103
 */
public class PositionableByteArrayInputStream
	extends ByteArrayInputStream
{
    public PositionableByteArrayInputStream( byte buf[] )
	{
		super( buf );
    }

    public PositionableByteArrayInputStream( byte buf[], int offset, int length )
	{
		super( buf, offset, length );
    }

// Access

	public int getPos()
	{
		return pos;
	}

// Commands

	public void setPos( int pos, int count )
	{
		this.pos = pos;
		this.count = count;
	}

	public void changeBuffer( byte[] buf )
	{
		this.buf = buf;
	}
}