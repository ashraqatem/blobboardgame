package blobboardgame;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] flattenedBoard = board.flatten();
		int size = flattenedBoard.length;
		int perimeterScore = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// Check if cell is on the perimeter
				if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
					if (flattenedBoard[i][j] != null && flattenedBoard[i][j].equals(targetGoal)) {
						// Cell is on perimeter and has target color
						if ((i == 0 || i == size - 1) && (j == 0 || j == size - 1)) {
							// Corner cell, count twice towards score
							perimeterScore += 2;
						} else {
							perimeterScore += 1;
						}
					}
				}
			}
		}

		return perimeterScore;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
