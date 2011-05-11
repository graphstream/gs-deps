package org.miv.pherd.ntree;

//import org.miv.pherd.*;

/**
 * The data stored in each cell that represent values specific to a given
 * simulation or algorithm.
 * 
 * <p>
 * In each cell, the cell data represents the data global to this cell that is
 * specific to a given particle simulation. This data represents the global
 * view represented by the cell.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public interface CellData
{
	/**
	 * Recompute the data, since the cell contents changed.
	 */
	void recompute();

	/**
	 * Set the parent cell of this data. This occurs only once, as soon
	 * as the cell knows its data. 
	 * @param cell The tied cell.
	 */
	void setCell( Cell cell );
	
	/**
	 * Factory method to create new cell data when a cell is subdivided.
	 * @return A new cell data element.
	 */
	CellData newCellData();
}