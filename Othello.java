// the code for the Othello Controller!

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Othello extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	// a list of the instance variables.
	private OthelloBoard board;		// The model - which is the checker board
	private OthelloView view;		// The view - which is the view of the checker board
	private boolean playersOneTurn;	// Whose turn it is
	private boolean mouseInWindow;
	private JTextField message;
	private int computermode; // different modes to signify what mode.
	public static int boardsize; // the size of the board, which is 8. I wanted to make it static..

	public Othello()
	{
		// this creates the board and the view.
		super("Othello - Black moves first");
		boardsize = 8;
		computermode = 0;
		message = new JTextField();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new OthelloBoard();
		view = new OthelloView();
		board.addObserver(view);
		view.update(board, null);
		Container c = getContentPane();
		JMenuItem m;

		// eightboard. This creates a JMenu which allows you to click on an 8 x 8 board.
		JMenu eightboard = new JMenu("New Game: 8 x 8");
		eightboard.setFont(new Font("sans-serif", Font.PLAIN, 25));
		setResizable(true);
		addMouseListener(this);				// Have this program listen for mouse events
		addMouseMotionListener(this);

		// The first menu item: a 2 player ggame.
		m = new JMenuItem("New 8 Game: 2-player");
		m.setFont(new Font("sans-serif", Font.PLAIN, 25));
		m.addActionListener(this);
		eightboard.add(m);

		// Easy Computer Mode, which plays random moves.
		m = new JMenuItem("New 8 Game: Computer Easy Mode");
		m.setFont(new Font("sans-serif", Font.PLAIN, 25));
		m.addActionListener(this);
		eightboard.add(m);

		// Hard Computer Mode, based on my algorithm.
		m = new JMenuItem("New 8 Game: Computer Hard Mode");
		m.setFont(new Font("sans-serif", Font.PLAIN, 25));
		m.addActionListener(this);
		eightboard.add(m);

		// An option to resign
		m = new JMenuItem("Resign");
		m.setFont(new Font("sans-serif", Font.PLAIN, 25));
		m.addActionListener(this);
		eightboard.add(m);


		// A JMenuBar at the top of the screen.
		JMenuBar mBar = new JMenuBar();
		mBar.add(Box.createRigidArea(new Dimension(0,50)));
        mBar.add(eightboard);


        setJMenuBar(mBar);

		c.add(view);

		// starts off with player One's Turn.
		playersOneTurn = true;
		mouseInWindow = false;
	}


	public void mouseClicked(MouseEvent e)
	{
		int row = view.getRow(e.getX());
		int col = view.getCol(e.getY());
		// Move checker piece on board - move method tells viewer to redisplay
		System.out.println("Putting Piece on + " + ( row + ", " + col ));	// DEBUG
			board.placePiece(col,row);

			//what to do if the game is over.(Just a preemptive step!)
			if (board.gameOver())
			{
				String s = "Game Over: " + board.winner() + "  Black: " + board.blackPieces() + " and White: " + board.whitePieces();
				setTitle(s);
			}
			// steps to do if the game is not over.
			else
			{
				boolean gameover = false;

				if(gameover == false)
				{
					// gets moves
					if (board.getMoves()%2 == 0)
						setTitle("Black's move");
					else
					{
						// decides what mode to activate.
						if(computermode == 1)
						{
							board.makeMove();
							setTitle("Black's move");
						}
						else if(computermode == 2)
						{
							board.strategicMove();
							setTitle("Black's move");
						}
						else if(computermode == 3)
						{
							board.minimaxMove();
							setTitle("Black's move");
						}

						else
							setTitle("White's Move");
					}

					// checks if the game is over
					if (board.gameOver())
					{
						String s = "Game Over: " + board.winner() + "  Black: " + board.blackPieces() + " and White: " + board.whitePieces();
						setTitle(s);
					}
				}
				System.out.println("Moves: " + board.getMoves());
				// Check for no possible moves for next player
			}
	}

	// A method I used to create a delay in the system, so that the computer doesn't make its moves instantly.

	public void actionPerformed(ActionEvent e)
	{
		// gets what button was clicked.
		String cmd = e.getActionCommand();
		if(cmd == "New 8 Game: 2-player")
		{
			setTitle("New Game! Black's move. May the best player win.");
			boardsize = 8;
			System.out.println("New");
			board.clearBoard();
			board.setPlayers();
			board.zeroMoves();
			System.out.println("2!");
		}
		if(cmd == "New 8 Game: Computer Easy Mode")
		{
			setTitle("New Game! Black's move.");
			board.clearBoard();
			board.setPlayers();
			board.zeroMoves();
			computermode = 1;
			System.out.println("Easy!");
		}

		if(cmd == "New 8 Game: Computer Hard Mode")
		{
			setTitle("New Game! Black's move. Ready for a challenge?");
			// These are the steps to start a new game.
			board.clearBoard();
			board.setPlayers();
			board.zeroMoves();
			computermode = 2;
		}
		if(cmd == "New 8 Game: Computer Minimax Mode")
		{
			setTitle("New Game! Black's move. Ready for a challenge?");
			// These are the steps to start a new game.
			board.clearBoard();
			board.setPlayers();
			board.zeroMoves();
			computermode = 3;
		}

		if(cmd == "Resign")
		{
			if(playersOneTurn)
			{
				board.clearBoard();
				setTitle("White Wins!");
			}
			if(!playersOneTurn)
			{
				board.clearBoard();
				setTitle("Black Wins!");
			}
		}
	}

	// these are various other possibile commands, but I didn't see the need to do anything about them.
	public void mouseDragged(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}

	// This is where the game is initialized!
    public static void main(String[] args)
	{
		Othello game = new Othello();
		game.setSize(1207,1315);
		game. setResizable(false);
		game.setVisible(true);
	}


}