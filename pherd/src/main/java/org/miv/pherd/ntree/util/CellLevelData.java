package org.miv.pherd.ntree.util;

import org.miv.pherd.ntree.Cell;
import org.miv.pherd.ntree.CellData;

/**
 * Cell data item that put maintain the leveling information.
 *
 * <p>
 * A cell data register is peer cell in an object nammed NTreeLevels
 * so that groups of cells of the same depth are in a set nammed a level.
 * Each time something changes in the graph, the levels are maintained
 * automatically.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class CellLevelData implements CellData
{
	protected int depth;
	
	protected NTreeLevels levels;

	protected Cell myCell;
	
	/**
	 * New cell-level data that will register in the given levels.
	 * @param levels The levels.
	 */
	public CellLevelData( NTreeLevels levels )
	{
		this.levels = levels;
		this.depth  = -1;
	}
	
	/**
	 * The depth of this data.
	 * @return The depth.
	 */
	protected int getDepth()
	{
		return depth;
	}
	
	/**
	 * Change the depth of this data.
	 * @param depth The new depth.
	 */
	protected void setDepth( int depth )
	{
		this.depth = depth;
	}

	public CellData newCellData()
	{
		return new CellLevelData( levels );
	}

	public void recompute()
	{
		levels.check( myCell );
	}

	public void setCell( Cell cell )
	{
		myCell = cell;
	}
}