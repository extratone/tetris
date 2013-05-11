package games.tetris.engine.grid;

import games.util.grid.GridOutOfBoundsException;
import games.util.grid.Virtual2DGrid;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.lang.Validate;

/**
 * Class that logs the state of a grid to a file. The log will show '0' where grid locations are emtpy and '1' where grid locations are filled with an object.
 * 
 * Comment line for EGIT test
 * 
 * @author edwin
 *
 */
class GridStateLogger<T> {

	private final Virtual2DGrid<T> grid; //TODO Remove when no longer necessary
	private RandomAccessFile file;

	public GridStateLogger(Virtual2DGrid<T> grid) {
		Validate.notNull(grid, "Grid is null");
		this.grid = grid;
		try {
			this.file = new RandomAccessFile("/home/edwin/Documents/Projects/tetris.log", "rw");
			this.file.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logGridState() {
		try {
	//		System.out.print("Start Logging Grid State:" + System.getProperty("line.separator"));
			this.file.setLength(0);
			for (int i=0; i < this.grid.getGridDimensions().height; i++) {
				for (int j=0; j < this.grid.getGridDimensions().width; j++) {
					try {
						this.file.writeChars(" " + (this.grid.getObjectAtPosition(j, i) == (Boolean.TRUE) ? "1" : "0"));
					} catch (GridOutOfBoundsException e) {
	//					e.printStackTrace();
					}
				}
				this.file.writeChars(System.getProperty("line.separator"));
	//			System.out.print(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("Finished Logging Grid State" + System.getProperty("line.separator") + System.getProperty("line.separator") + System.getProperty("line.separator"));
	}
}