// Credit to my 11th grade teach Greg Volger, who's view for a checkers game I based a large part of this file off.
import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class OthelloView extends JPanel implements Observer
{
	private int board[][];
	private final int SIZE = 150;
	private final int OFFSET = 0;
	private final int CIRCLE = 150;
	private final int CIRCLE_SPACE = 20;
	private static int PLAYER_ONE = 1;	// Color is white
	private static int PLAYER_TWO = 2;	// Color is green
	private static int TOP_BORDER_PIXELS = 143; // used to distinguish top, found through trial and error.

    public OthelloView()
    {
    	setBackground(Color.white);
    }

	// This paints the component(influences what you see)
    public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
			{
				if ((r % 2 == 0 && c % 2 == 0) || (r % 2 != 0 && c % 2 != 0))
					g.setColor(Color.red);
				else
					g.setColor(Color.black);
				g.fillRect(c*SIZE+OFFSET, OFFSET + r * SIZE, SIZE, SIZE);
			}
		}

		// painting the board, row by row and circle by circle.
    	for(int row = 0; row < board.length; row++)
    	{
    		for (int col = 0; col < board[row].length; col++)
    		{
    			int x = OFFSET + (col-1) * CIRCLE+CIRCLE_SPACE + SIZE;
    			int y = CIRCLE+CIRCLE_SPACE+OFFSET + (row-1)*SIZE;
    			int width = CIRCLE-CIRCLE_SPACE*2;
    			if (board[row][col] == PLAYER_ONE)
    			{
    				g.setColor(Color.white);
    				// Normal piece
    				g.fillOval(x, y, width, width);
    			}
     			else if (board[row][col] == PLAYER_TWO)
    			{
    				g.setColor(Color.BLUE);
    				// Normal piece
    				g.fillOval(x, y, width, width);
    			}
    		}
    	}
	}
	public int getRow(int rowPixels)
    {
    	rowPixels -= OFFSET;
    	return rowPixels / SIZE;
    }

    /**
     *	Returns the grid column location knowing the pixel location within the board
     *	@param colPixels number of pixels across in the window
     *	@returns the checkerboard square column
     */
    public int getCol(int colPixels)
    {
    	colPixels -= TOP_BORDER_PIXELS;
    	colPixels -= OFFSET;
    	return colPixels / SIZE;
    }

    // this updates the view to the controller.
	public void update(Observable o, Object obj)
	{
		OthelloBoard x = (OthelloBoard)(o);
		board = x.getBoard();
		repaint();
	}
}