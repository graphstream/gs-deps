package org.miv.pherd.ntree.util;

import java.util.*;
import org.miv.pherd.ntree.*;

/**
 * Levels in a NTree.
 * 
 * <p>
 * The levels represent sets of cells that are all at a given depth. They are
 * automatically updated when you use the {@link CellLevelData} class as data
 * for the cells of your tree.
 * </p>
 * 
 * <p>
 * When you use the {@link CellLevelData} as data of your cells, they automatically
 * maintain the cell level in an instance of this class. This instance can then
 * be queried to browse cells by levels.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class NTreeLevels
{
// Attributes
	
	/**
	 * The levels. Each level is a set of cells.
	 */
	protected ArrayList<HashSet<Cell>> levels;
	
// Constructors
	
	public NTreeLevels()
	{
	}
	
// Accessors
	
	/**
	 * The number of levels in the tree (equals to the depth of the tree).
	 * @return The number of levels.
	 */
	public int getLevelCount()
	{
		trim();
		return levels.size();
	}
	
	/**
	 * Set of cells of the i-th level.
	 * @param i The level.
	 * @return A set of cells (maybe empty).
	 */
	public Set<? extends Cell> getLevel( int i )
	{
		return levels.get( i );
	}
	
// Commands
	
	/**
	 * Remove empty levels at the top of the level stack.
	 */
	protected void trim()
	{
		int i = levels.size() - 1;
		
		while( levels.get(i).isEmpty() )
		{
			levels.remove( i );
			i --;
		}
	}
	
	/**
	 * Called by a CellLevelData instance to check it is correctly
	 * registered in its level.
	 * @param cell The cell to check.
	 */
	protected void check( Cell cell )
	{
		if( cell.getData() instanceof CellLevelData )
		{
			CellLevelData cdata = (CellLevelData) cell.getData();
			
			if( cdata.getDepth() != cell.getDepth() )
			{
				levels.get(cdata.getDepth()).remove( cell );
				cdata.setDepth( cell.getDepth() );
				
				while( levels.size() <= cdata.getDepth() )
				{
					levels.add( new HashSet<Cell>() );
				}
				
				levels.get(cdata.getDepth()).add( cell );
			}
		}
	}
}