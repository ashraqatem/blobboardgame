package blobboardgame;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		// Get the flattened representation of the board
		Color[][] unitCells = board.flatten();

		// Initialize visited array to keep track of visited cells
		boolean[][] visited = new boolean[unitCells.length][unitCells[0].length];

		// Initialize maxBlobSize to keep track of the largest blob size found
		int maxBlobSize = 0;

		// Iterate through each cell in the board
		for (int i = 0; i < unitCells.length; i++) {
			for (int j = 0; j < unitCells[0].length; j++) {
				// If the cell is not visited and has the target color
				if (!visited[i][j] && unitCells[i][j].equals(targetGoal)) {
					// Calculate the size of the undiscovered blob starting from this cell
					int blobSize = undiscoveredBlobSize(i, j, unitCells, visited);
					// Update maxBlobSize if the current blob is larger
					maxBlobSize = Math.max(maxBlobSize, blobSize);
				}
			}
		}

		// Return the size of the largest connected blob of the target color
		return maxBlobSize;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		// Check if the current cell is within bounds and if it is not visited
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[0].length || visited[i][j]) {
			return 0;
		}

		// Mark the current cell as visited
		visited[i][j] = true;

		// Check if the current cell is of the target color
		if (unitCells[i][j].equals(targetGoal)) {
			// Initialize blob size to 1 (for the current cell)
			int blobSize = 1;

			// Recursively check and count the sizes of neighboring cells
			blobSize += undiscoveredBlobSize(i + 1, j, unitCells, visited); // Check right cell
			blobSize += undiscoveredBlobSize(i - 1, j, unitCells, visited); // Check left cell
			blobSize += undiscoveredBlobSize(i, j + 1, unitCells, visited); // Check bottom cell
			blobSize += undiscoveredBlobSize(i, j - 1, unitCells, visited); // Check top cell

			return blobSize;
		}

		// Return 0 if the current cell is not of the target color
		return 0;

	}

}
