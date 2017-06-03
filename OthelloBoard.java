/**
	This is the Model - CheckerBoard.java

	The model (CheckerGame), the view (CheckersView), and the controller
	(Checkers) are in separate files.  When the model changes, the view is
	notified and updated automatically.
*/

/*
	The checker board is an 8 x 8 grid with the top left corner being
	a white square.  All pieces (White & Green) reside on black squares.
	Red squares are never occupied.
*/
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.lang.Math;

public class OthelloBoard extends Observable // NEED TO EXTEND A CLASS TO MAKE MVC WORK
{
	public int board[][];
	private int BOARD_SIZE;
	private int EMPTY = 0;
	private static int PLAYER_ONE = 1;
	private static int PLAYER_TWO = 2;
	private int moves;

	/**
	 *	Creates an checkerboard with all the pieces in their starting places.
	 */
	public OthelloBoard()
	{
		BOARD_SIZE = Othello.boardsize;
		board = new int[BOARD_SIZE][BOARD_SIZE];
		clearBoard();
		setPlayers();
		moves = 0;
	}

	public void zeroMoves()
	{
		moves = 0;
	}
	/**
		Makes it possible to change the boardsize, so for instance it would be easy to make a 4 x 4 board, if you wanted to play another game.
		This was useful because I was considering making a 4 x 4 board as another possibility, but in the end I decided not to.
	*/
	public void changeBoardSize(int newboardsize)
	{
		BOARD_SIZE = newboardsize;
		board = new int[BOARD_SIZE][BOARD_SIZE];
		setChanged();
		notifyObservers();
	}

	// checks if theres no moves left - this is one way that the  game can end.
	public boolean noMoves()
	{
		for(int k = 0; k< BOARD_SIZE; k++)
    	{
    		for(int l = 0; l < BOARD_SIZE; l++)
    		{
    			if(validMove(k,l))
    				return false;
    		}
    	}
    	return true;
	}

	// clears the board to start the game by making every entry of the board array empty
	public void clearBoard()
	{
		for (int r = 0; r < BOARD_SIZE; r++)
			for (int c = 0; c < BOARD_SIZE; c++)
				board[r][c] = EMPTY;
		System.out.println("Cleared");
		setChanged();
		notifyObservers();
	}


	/**
	 *	Places the board pieces in their starting locations(The middle 4).
	 */
	public void setPlayers()
	{
		System.out.println(BOARD_SIZE);
		int middle = BOARD_SIZE/2;
		board[middle-1][middle-1] = PLAYER_ONE;
		board[middle][middle] = PLAYER_ONE;
		board[middle][middle-1] = PLAYER_TWO;
		board[middle-1][middle] = PLAYER_TWO;

	}

	// Determines which player to move, by seeing if the number of moves is odd or even.
	public int playerToMove(int moves)
	{
		if(moves%2 == 1)
			return PLAYER_ONE;
		else
			return PLAYER_TWO;
	}
	/**
		Returns true if r & c are within the board
		@param r row location of checkerboard
		@param c column location of checkerboard
		@return returns true if r & c are valid locations in the checkerboard
	*/
	public boolean validLocation(int r, int c)
	{
		if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE)
			return false;
		return true;
	}

	/**
		Black squares can be found in
			Even Row & Odd Column
			Odd Row & Even Column
		// checks if a certain square is a black square.
	*/
	public boolean blacksquare(int r, int c)
	{
		if (!validLocation(r, c))
			return false;
		if (r % 2 == 0)
			return (c % 2 != 0);
		return (c % 2 == 0);
	}

	/**
	 	Checks if a square is occupied.
		@return returns true is there is a checker piece on the grid location (r,c)
		        otherwise returns false
	*/
	public boolean squareOccupied(int r, int c)
	{
		if (!validLocation(r,c))
			return false;
		return board[r][c] != EMPTY;
	}

	/**
		 Returns true if positions occupy different players
	*/
	public boolean isOpponent(int myRow, int myCol, int oppRow, int oppCol)
	{
		if (!squareOccupied(myRow, myCol) || !squareOccupied(oppRow, oppCol))
			return false;
		return true;
	}

	// checks if the color of a certain piece matches the color of the player to move.
	boolean checkPiecePlaced(int toRow, int toCol)
	{
		if(toRow >= BOARD_SIZE|| toCol >= BOARD_SIZE || toRow < 0|| toCol < 0)
			return false;
		if(playerToMove(moves) == board[toRow][toCol])
			return true;
		return false;
	}

	/**
	 *	Checking to see if (fromRow,fromCol) to (toRwo,toCol)
	 *	is a valid move.
		@return returns true if it is a valid move
	*/
	public boolean validMove(int toRow, int toCol)
	{
		// Must move within the board
		if (!validLocation(toRow, toCol))
			return false;

		// Check to see if the position moving to is occupied
		if (squareOccupied(toRow, toCol))
			return false;
		boolean neighborsOccupied = false;
		for(int incRow = -1; incRow <= 1; incRow++)
		{
			for(int incCol = -1; incCol <=1; incCol++)
			{
				if(squareOccupied(toRow+incRow,toCol+incCol)&& !(incRow==0&&incCol ==0))
				{
					neighborsOccupied = true;
				}
			}
		}
		if(neighborsOccupied == false)
		{
			return false;
		}

		// if the neighbors are occupied, you need to check that your moves make a capture.
		if(neighborsOccupied == true)
		{
			// North
			if(squareOccupied(toRow-1,toCol) && !checkPiecePlaced(toRow-1,toCol))
			{
				boolean empty = false;
				for(int k=toRow -1; k>=0;k--)
				{
					if(!squareOccupied(k,toCol))
					{
						empty = true;
					}
					if(checkPiecePlaced(k,toCol)&& empty == false)
					{
						return true;
					}
				}
			}

			//South
			if(squareOccupied(toRow+1,toCol) && !checkPiecePlaced(toRow+1,toCol))
			{
				boolean empty = false;
				for(int k=toRow + 1; k < BOARD_SIZE;k++)
				{
					if(!squareOccupied(k,toCol))
					{
						empty = true;
					}
					if(checkPiecePlaced(k,toCol)&& empty == false)
					{
						return true;
					}
				}
			}

			//West
			if(squareOccupied(toRow,toCol-1) && !checkPiecePlaced(toRow,toCol-1))
			{
				boolean empty = false;
				for(int k=toCol -1; k>=0;k--)
				{
					if(!squareOccupied(toRow,k))
					{
						empty = true;
					}
					if(checkPiecePlaced(toRow,k)  && empty == false)
					{
						return true;
					}
				}
			}

			//East
			if(squareOccupied(toRow,toCol+1) && !checkPiecePlaced(toRow,toCol+1))
			{
				boolean empty = false;
				for(int k=toCol+1; k < BOARD_SIZE;k++)
				{
					if(!squareOccupied(toRow,k))
					{
						empty = true;
					}
					if(checkPiecePlaced(toRow,k)&& empty == false)
					{
						return true;
					}
				}
			}

			// Southeast
			if(squareOccupied(toRow+1,toCol+1) && !checkPiecePlaced(toRow+1,toCol+1))
			{
				boolean empty = false;
				int ctr = -1;
				if(toCol > toRow)
					ctr = BOARD_SIZE - toRow;
				else
					ctr = BOARD_SIZE - toCol;
				for(int k= 1; k<= ctr; k++)
				{
					if(!squareOccupied(toRow+k,toCol+k))
					{
						empty = true;
					}
					if(checkPiecePlaced(toRow+k,toCol+k)&& empty == false)
					{
						return true;
					}
				}
			}

			//NorthWest
			if(squareOccupied(toRow-1,toCol-1) && !checkPiecePlaced(toRow-1,toCol-1))
			{
				boolean empty = false;
				int ctr = -1;
				if(toCol > toRow)
					ctr = toRow;
				else
					ctr = toCol;
				for(int k= 1; k<= ctr; k++)
				{
					if(!squareOccupied(toRow-k,toCol-k))
					{
						empty = true;
					}
					if(checkPiecePlaced(toRow-k,toCol-k)&& empty == false)
					{
						return true;
					}
				}
			}

			//NorthEast
			if(squareOccupied(toRow-1,toCol+1) && !checkPiecePlaced(toRow-1,toCol+1))
			{
				boolean empty = false;
				int ctr = -1;
				if((BOARD_SIZE - toCol) > toRow)
					ctr = toRow;
				else
					ctr = BOARD_SIZE-toCol;
				for(int k= 1; k<= ctr; k++)
				{
					if(!squareOccupied(toRow-k,toCol+k))
					{
						empty = true;
					}
					if(checkPiecePlaced(toRow-k,toCol+k)&& empty == false)
					{
						return true;
					}
				}
			}

			//SouthWest
			if(squareOccupied(toRow+1,toCol-1) && !checkPiecePlaced(toRow+1,toCol-1))
			{
				boolean empty = false;
				int ctr = -1;
				if((BOARD_SIZE - toRow) > toCol)
					ctr = toCol;
				else
					ctr = BOARD_SIZE - toRow;
				for(int k= 1; k<= ctr; k++)
				{
					if(!squareOccupied(toRow+k,toCol-k))
					{
						 empty = true;
					}
					if(checkPiecePlaced(toRow+k,toCol-k)&& empty == false)
					{
						return true;
					}
				}
			}


		}
		return false;
	}

	// A useful helper method to check if 2 entries or the same color.
	public boolean sameColor(int row1,int col1, int row2, int col2)
	{
		if(board[row1][col1] == board[row2][col2])
			return true;
		return false;
	}

	/**
		Attempt to place a piece into a row and a col.
		Only lets you do it if this is a valid move.
	*/
	public void placePiece(int toRow, int toCol)
	{
		if (validMove(toRow, toCol))
		{
			System.out.println(moves);
			board[toRow][toCol] = playerToMove(moves);
			// Move the player to it's new position
			if(moves%2 == 1)
			{
				board[toRow][toCol] = PLAYER_ONE;

			}
			else
			{
				board[toRow][toCol]= PLAYER_TWO;
			}

			//first do it in the North Direction.
			boolean canRemove = true;
			int desiredRow = -1;
			for(int k=toRow -1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow>=0)
			{
				for(int k = desiredRow; k<toRow;k++)
				{
					board[k][toCol]= board[desiredRow][toCol];
				}
			}


			//South Direction
			desiredRow = -1;
			canRemove = true;
			for(int k= toRow+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow >=0)
			{
				for(int k = toRow; k<desiredRow;k++)
				{
					board[k][toCol]= board[desiredRow][toCol];
				}
			}

			//West Direction
			int desiredCol= -1;
			canRemove = true;
			for(int k=toCol-1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(toRow,k))
				{
					canRemove= false;
				}
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol>=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow][k]= board[toRow][desiredCol];
				}
			}

			//East Direction
			desiredCol= -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(toRow,k))
					canRemove= false;
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow][k]= board[toRow][desiredCol];
				}
			}

			//SouthEast
			desiredCol= -1;
			int newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow + (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow < BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow+(k-toCol)][k]= board[newRow][desiredCol];
				}
			}

			//NorthEast
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow - (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0&& sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow-(k-toCol)][k]= board[newRow][desiredCol];
				}
			}

			//NorthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>=0&&canRemove==true;k--)
			{
				newRow = toRow - (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0 && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow-(toCol-k)][k]= board[newRow][desiredCol];
				}
			}

			//SouthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>=0&&canRemove==true;k--)
			{
				newRow = toRow + (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow< BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow+(toCol-k)][k]= board[newRow][desiredCol];
				}
			}
			// increment the moves.
			moves++;

			// These 2 statements connect what happens on the board to the view and the controller.
			setChanged();
			notifyObservers();
		}


	}

	// get the number of moves - useful in figuring out who's move it is.
	public int getMoves()
	{
		return moves;
	}

	/**
	 *	Returns the Othello
	 *	@return returns the Othello
	 */
	public int[][] getBoard()
	{
		return board;
	}

	/**
	 *	Determines who is at location (r,c)
		@param r is the row in the grid to check
		@param c is column in the gird to check
		@return returns true if "player one" is at location [r][c],
		        otherwise returns false
	*/
	public boolean isPlayerOne()
	{
		if (moves%2 == 1)
			return false;
		return true;
	}

	/**
	 *	Determines if there is a winner.
		Count the number of pieces left for each player.  If there are not any
		pieces left for a player, they lose, and the other player is the winner.
		@return returns true if there is a winner - all of one color's pieces are gone
	*/
	public boolean gameOver()
	{
		if(noMoves())
			return true;
		for(int k = 0; k< BOARD_SIZE; k++)
		{
			for(int l = 0; l< BOARD_SIZE; l++)
			{
				if(board[k][l] == EMPTY)
					return false;
			}
		}
		return true;
	}

	// A helper accessor method with the BOARD SIZE.
	public int getBoardSize()
	{
		return BOARD_SIZE;
	}

	// Count the number of blackPieces, to determine the winner
	public int blackPieces()
	{
		int count = 0;
		for(int k = 0; k< BOARD_SIZE; k++)
		{
			for(int l = 0; l<BOARD_SIZE; l++)
			{
				if(board[k][l] == PLAYER_TWO)
					count++;
			}
		}
		return count;
	}

	// Count the number of white pieces to determine the winner.
	public int whitePieces()
	{
		int count = 0;
		for(int k = 0; k< BOARD_SIZE; k++)
		{
			for(int l = 0; l<BOARD_SIZE; l++)
			{
				if(board[k][l] == PLAYER_ONE)
					count++;
			}
		}
		return count;
	}

	// checks who's the winner, based on the counts of white and black(blue) pieces, leavng the possibility open for a tie.
	public String winner()
	{
		if(whitePieces() > blackPieces())
		{
			return "WHITE WINS!";
		}
		else if(whitePieces() < blackPieces())
		{
			return "BLACK WINS!";
		}
		else
		{
			return "TIE!";
		}
	}

	// This is the means to make an easy move, by choosing a random move.
	public void makeMove()
    {
    	System.out.println("Making Easy Move!");
    	int size = getBoardSize();
    	// creates an arraylist to store each mossible move(with the rows and the columns).
    	List<Integer> rows = new ArrayList<Integer>();
    	List<Integer> cols = new ArrayList<Integer>();

    	// adds each legal move to an arraylist
    	int ctr = 0;
    	for(int k = 0; k< size; k++)
    	{
    		for(int l = 0; l < size; l++)
    		{
    			if(validMove(k,l))
    			{
    				rows.add(k);
    				cols.add(l);
    				ctr++;
    			}
    		}
    	}

    	// Uses a random number generator to choose between these moves.
    	int move = (int)Math.random() * ctr;

    	// places piece at the row and col for this moves.
    	placePiece(rows.get(move), cols.get(move));
    }

	// Part of the strategic  algorithm: figuring out how many poieces a move captures(part of its merit!)
    public int piecesCaptured(int toRow, int toCol)
    {
    	int captured = 0;
    	if (validMove(toRow, toCol))
		{
			//first do it in the North Direction.
			boolean canRemove = true;
			int desiredRow = -1;
			for(int k=toRow -1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(checkPiecePlaced(k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow>=0)
			{
				for(int k = desiredRow; k<toRow;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}


			//South Direction
			desiredRow = -1;
			canRemove = true;
			for(int k= toRow+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(checkPiecePlaced(k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow >=0)
			{
				for(int k = toRow; k<desiredRow;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//West Direction
			int desiredCol= -1;
			canRemove = true;
			for(int k=toCol-1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(k,toCol))
				{
					canRemove= false;
				}
				if(checkPiecePlaced(k,toCol))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol>=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//East Direction
			desiredCol= -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(toRow,k))
					canRemove= false;
				if(checkPiecePlaced(toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//SouthEast
			desiredCol= -1;
			int newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow + (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow < BOARD_SIZE && checkPiecePlaced(newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//NorthEast
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow - (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0&& checkPiecePlaced(newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//NorthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>-0&&canRemove==true;k--)
			{
				newRow = toRow - (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0 && checkPiecePlaced(newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
			//SouthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>0&&canRemove==true;k--)
			{
				newRow = toRow + (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow< BOARD_SIZE && checkPiecePlaced(newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					captured++;
					System.out.println("Captured!");
				}
			}
		}
		return captured;
    }

    // Now it's the meat of the algorithm!
    public void strategicMove()
    {
    	// Just a check for me!
    	System.out.println("Making Hard Move!");
    	// Gets board size
    	int size = getBoardSize();

    	// Creates an Arraylist for the rows and cols. Note I chose an arraylist so we can modify the memory as we go.
    	List<Integer> rows = new ArrayList<Integer>();
    	List<Integer> cols = new ArrayList<Integer>();

    	// Adds all the moves to the arraylist.
    	int ctr = 0;
    	for(int k = 0; k< size; k++)
    	{
    		for(int l = 0; l < size; l++)
    		{
    			if(validMove(k,l))
    			{
    				rows.add(k);
    				cols.add(l);
    				ctr++;
    			}
    		}
    	}

    	// my priority algorithm.
    	int priority = 0;
    	int bestrow = -1;
    	int bestcol = -1;

    	// Statement to make sure something gets executed.
    	if(ctr > 0)
    	{
    		bestrow = rows.get(0);
    		bestcol = cols.get(0);
    	}
    	for(int m = 0; m < ctr; m++)
    	{
    		int row = rows.get(m);
    		int col = cols.get(m);
    		int temppriority = 0;
    		// takes into account pieces captured.
    		temppriority += piecesCaptured(row,col);
    		System.out.println("Pieces: " + piecesCaptured(row,col));

    		// strongly against doing things in the corner.
			if(((row == 6 && col == 6) && !squareOccupied(7,7)) || ((row == 1 && col == 1) && !squareOccupied(0,0)) || ((row == 1 && col == 6) && !squareOccupied(0,7)) ||((row == 6 && col == 1) && !squareOccupied(7,0)))
			{
				temppriority = -50;
			}
			// also against edges.
			if((((row == 7 && col == 6) || row == 6 && col == 7) && !squareOccupied(7,7)) ||(row + col == 1 && !squareOccupied(0,0)) || (((row == 0 && col == 6)||(row == 1 && col == 7))&&!squareOccupied(0,7)) || (((row == 6 && col == 0)||(row == 7 && col == 1))&&!squareOccupied(7,0)))
			{
				temppriority  -=10;
			}

			// adds the priority if you're  at a corner yourself.
			if((row == 7 && col == 7) || (row == 0 && col == 0) || (row == 0 && col == 7) || (row == 7 && col == 0))
			{
				temppriority += 50;
			}

			/**
			tempplacePiece(row,col);
    		List<Integer> opprows = new ArrayList<Integer>();
    		List<Integer> oppcols = new ArrayList<Integer>();
	    	int oppctr = 0;

	    	for(int k = 0; k< size; k++)
	    	{
	    		for(int l = 0; l < size; l++)
	    		{
	    			if(validMove(k,l))
	    			{
	    				opprows.add(k);
	    				oppcols.add(l);
	    				oppctr++;
	    			}
	    		}
    		}
			int bestopponentmove = 0;
			for(int n = 0; n < oppctr; n++)
			{
				int oppscore = 0;
	    		int opprow = opprows.get(n);
	    		int oppcol = oppcols.get(n);
	    		oppscore += piecesCaptured(opprow,oppcol);
	    		oppscore /=2;
	    		if(((opprow == 6 && oppcol == 6) && !squareOccupied(7,7)) || ((opprow == 1 && oppcol == 1) && !squareOccupied(0,0)) || ((opprow == 1 && oppcol == 6) && !squareOccupied(0,7)) ||((opprow == 6 && oppcol == 1) && !squareOccupied(7,0)))
				{
					oppscore -= 10;
				}
				if((opprow + oppcol == 13 && !squareOccupied(7,7)) ||(opprow + oppcol == 1 && !squareOccupied(0,0)) || (((opprow == 0 && oppcol == 6) || (opprow == 1 && oppcol == 7)) && !squareOccupied(0,7))||(((opprow == 6 && oppcol == 0) || (opprow == 7 && oppcol == 1)) && !squareOccupied(7,0)))
				{
					oppscore -= 5;
				}
				if((opprow == 7 && oppcol == 7) || (opprow == 0 && oppcol == 0) || (opprow == 0 && oppcol == 7) || (opprow == 7 && oppcol == 0))
				{
					oppscore +=10;
				}
				if(oppscore > bestopponentmove)
				{
					bestopponentmove = oppscore;
				}
			}
			temppriority -= bestopponentmove;
			*/
			// This was an original modification I had to my game, where it would calculate based on the opponent's moves what to do next!
			// But possibly because the heuristics in the future are so hard to check out, I found that this was actually doing worse than my original algorithm!

			// uses the modification of temppriority to determine the move that you want to make.
    		if(temppriority > priority)
    		{
    			bestrow = row;
    			bestcol = col;
    			System.out.println("Row is " + row);
    			System.out.println("Col is " + col);
    			System.out.println("Changed!");
    		}

    		/*
    		removeplacedPiece(row,col);
    		*/
    	}

    	System.out.println("The best row is " + bestrow);
    	System.out.println("The best col is " + bestcol);
    	placePiece(bestrow,bestcol);
    }

	/**
	These were original helper methods where I would place a piece, then remove it to be able to think ahead into the future.
	But, because my computer wasn't performing as well in this scenario, I decided to scrap it. I think it's because my algorithms didn't generalize that well ahead.
	*/
	/*

	// This method temporarily places a piece, so that you can calls.
    public void tempplacePiece(int toRow, int toCol)
    {
			System.out.println(moves);
			board[toRow][toCol] = playerToMove(moves);
			// Move the player to it's new position
			if(moves%2 == 0)
			{
				board[toRow][toCol] = PLAYER_ONE;
			}
			else
			{
				board[toRow][toCol]= PLAYER_TWO;
			}


			//first do it in the North Direction.
			boolean canRemove = true;
			int desiredRow = -1;
			for(int k=toRow -1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow>=0)
			{
				for(int k = desiredRow; k<toRow;k++)
				{
					board[k][toCol]= board[desiredRow][toCol];
				}
			}


			//South Direction
			desiredRow = -1;
			canRemove = true;
			for(int k= toRow+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow >=0)
			{
				for(int k = toRow; k<desiredRow;k++)
				{
					board[k][toCol]= board[desiredRow][toCol];
				}
			}
			//West Direction
			int desiredCol= -1;
			canRemove = true;
			for(int k=toCol-1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(toRow,k))
				{
					canRemove= false;
				}
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol>=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow][k]= board[toRow][desiredCol];
				}
			}
			//East Direction
			desiredCol= -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(toRow,k))
					canRemove= false;
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow][k]= board[toRow][desiredCol];
				}
			}
			//SouthEast
			desiredCol= -1;
			int newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE &&canRemove==true;k++)
			{
				newRow = toRow + (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow < BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow+(k-toCol)][k]= board[newRow][desiredCol];
				}
			}
			//NorthEast
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow - (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0&& sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow-(k-toCol)][k]= board[newRow][desiredCol];
				}
			}
			//NorthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>=0&&canRemove==true;k--)
			{
				newRow = toRow - (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0 && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow-(toCol-k)][k]= board[newRow][desiredCol];
				}
			}
			//SouthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>0&&canRemove==true;k--)
			{
				newRow = toRow + (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow< BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow+(toCol-k)][k]= board[newRow][desiredCol];
				}
			}
			boolean checked = false;
			for(int k = 0; k< BOARD_SIZE; k++)
			{
				for(int l = 0; l< BOARD_SIZE; l++)
				{
					if(board[k][l] == PLAYER_ONE)
					{
						board[k][l] = PLAYER_TWO;
						checked = true;
					}
					if(board[k][l] == PLAYER_TWO && checked == false)
						board[k][l] = PLAYER_ONE;
				}
			}
			moves++;
    }
    // a helper file to help with removing the placed piece.
    public int oppositeColor(int toRow, int toCol)
    {
    	if(board[toRow][toCol] == PLAYER_ONE)
    		return PLAYER_TWO;
    	return PLAYER_ONE;
    }
	// once you've modified the board with a certain piece you need to remove it.
    public void removeplacedPiece(int toRow, int toCol)
    {
			// Move the player to it's new position
			// Change color for the pieces in between! NEED TO WORK ON THIS

			boolean canRemove = true;
			int desiredRow = -1;
			for(int k=toRow -1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow>=0)
			{
				for(int k = desiredRow; k<toRow;k++)
				{
					board[k][toCol]= oppositeColor(toRow,toCol);
				}
			}


			//South Direction
			desiredRow = -1;
			canRemove = true;
			for(int k= toRow+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(k,toCol))
					canRemove= false;
				if(sameColor(toRow,toCol,k,toCol))
				{
					desiredRow = k;
					canRemove = false;
				}
			}
			if(desiredRow >=0)
			{
				for(int k = toRow; k<desiredRow;k++)
				{
					board[k][toCol] = oppositeColor(toRow,toCol);
				}
			}

			//West Direction
			int desiredCol= -1;
			canRemove = true;
			for(int k=toCol-1; k>=0&&canRemove==true;k--)
			{
				if(!squareOccupied(toRow,k))
				{
					canRemove= false;
				}
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol>=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow][k]= oppositeColor(toRow,toCol);
				}
			}

			//East Direction
			desiredCol= -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				if(!squareOccupied(toRow,k))
					canRemove= false;
				if(sameColor(toRow,toCol,toRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow][k]= oppositeColor (toRow,toCol);
				}
			}

			//SouthEast
			desiredCol= -1;
			int newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow + (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow < BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow+(k-toCol)][k]= oppositeColor(toRow,toCol);
				}
			}

			//NorthEast
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol+1; k<BOARD_SIZE&&canRemove==true;k++)
			{
				newRow = toRow - (k-toCol);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0&& sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = toCol; k<desiredCol;k++)
				{
					board[toRow-(k-toCol)][k]= oppositeColor(toRow,toCol);
				}
			}

			//NorthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>-0&&canRemove==true;k--)
			{
				newRow = toRow - (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow>=0 && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow-(toCol-k)][k]= oppositeColor(toRow,toCol);
				}
			}

			//SouthWest
			desiredCol= -1;
			newRow = -1;
			canRemove = true;
			for(int k = toCol-1; k>0&&canRemove==true;k--)
			{
				newRow = toRow + (toCol-k);
				if(!squareOccupied(newRow,k))
					canRemove= false;
				if(newRow< BOARD_SIZE && sameColor(toRow,toCol,newRow,k))
				{
					desiredCol = k;
					canRemove = false;
				}
			}
			if(desiredCol >=0)
			{
				for(int k = desiredCol; k<toCol;k++)
				{
					board[toRow+(toCol-k)][k]= oppositeColor(toRow,toCol);
				}
			}
			moves--;
			board[toRow][toCol] = EMPTY;
    }
    */
	// DEBUG method
	public String toString()
	{
		String b = "";
		for (int r = 0; r < BOARD_SIZE; r++)
		{
			for (int c = 0; c < BOARD_SIZE-1; c++)
				b += board[r][c] + "\t";
			b += board [r][BOARD_SIZE-1] + "\n";
		}
		return b;
	}
}
