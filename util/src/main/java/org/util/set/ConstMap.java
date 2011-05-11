package org.util.set;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Utility map class that wraps a real map but disallow any modification.
 *
 * <p>
 * A constant map wraps a real map (like a hash map for example) and allows to
 * browse or iterate it, but not to modify it (elements cannot be added or
 * removed).
 * </p>
 *
 * @author Antoine Dutot
 * @author Yoann Pigné
 * @since 20070126
 * @param <K> The key type.
 * @param <T> The value type.
 */
public class ConstMap<K,T> implements Map<K,T>
{
// Attributes
	
	/**
	 * The wrapped map.
	 */
	protected Map<K,T> realMap;
	
// Constructors
	
	/**
	 * New constant map wrapping the given real map.
	 * @param map The map to wrap.
	 */
	public ConstMap( Map<K,T> map )
	{
		realMap = map;
	}
	
// Methods
	
	public void clear()
	{
		throw new RuntimeException( "cannot modifiy a ConstMap!" );
	}

	public boolean containsKey( Object arg0 )
	{
		return realMap.containsKey( arg0 );
	}

	public boolean containsValue( Object arg0 )
	{
		return realMap.containsValue( arg0 );
	}

	public Set<java.util.Map.Entry<K, T>> entrySet()
	{
		return realMap.entrySet();
	}

	public T get( Object arg0 )
	{
		return realMap.get( arg0 );
	}

	public boolean isEmpty()
	{
		return realMap.isEmpty();
	}

	public Set<K> keySet()
	{
		return realMap.keySet();
	}

	public T put( K arg0, T arg1 )
	{
		throw new RuntimeException( "cannot modifiy a ConstMap!" );
	}

	public void putAll( Map<? extends K, ? extends T> arg0 )
	{
		throw new RuntimeException( "cannot modifiy a ConstMap!" );
	}

	public T remove( Object arg0 )
	{
		throw new RuntimeException( "cannot modifiy a ConstMap!" );
	}

	public int size()
	{
		return realMap.size();
	}

	public Collection<T> values()
	{
		return realMap.values();
	}
}