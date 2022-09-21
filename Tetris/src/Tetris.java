import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
public abstract class Tetris extends JFrame implements ActionListener, KeyListener{
	int TILE_SIZE = 32; 
	int rows, cols;
	Color [] colors = {Color.lightGray, Color.green, Color.cyan, Color.orange,Color.yellow,Color.magenta,Color.red,Color.blue};
	int [][][] tetris_shapes = {
			
			{{1, 1, 1},
				
				{0, 1, 0}},
			
			{{0, 2, 2},
					
				{2, 2, 0}},
			
			{{3, 3, 0},
					
				{0, 3, 3}},
			
			{{4, 0, 0},
					
				{4, 4, 4}},
			
			{{0, 0, 5},
					
				{5, 5, 5}},
			
			{{6, 6, 6, 6}},
			
			{{7, 7},
				
				{7, 7}}};
	boolean gameover,pause;
	int [][] gridCell;
	JLabel[][] board;
	JPanel panel,help;
	
	int [][]stone;
	int stone_r,stone_c;
	
	Timer timer;
	
	public Tetris(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		gameover = false;
		pause = false;
		timer = new Timer(300, this);
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1,2,1,1));
		
		init_game();
		new_stone();
		setSize(TILE_SIZE*cols*2,TILE_SIZE*rows);
		setVisible(true);
		this.addKeyListener(this);
	}
	public boolean check_collision(int off_r, int off_c) {
		for(int r=0; r<stone.length; r++)
			for(int c=0; c<stone[0].length; c++) {
				if(0 <= off_r+r && off_r+r<rows &&0<= off_c+c && off_c+c<cols) {
					if(stone[r][c] != 0 && gridCell[off_r+r][off_c+c] != 0)
						return true;
				}else
					return true;
			}
		return true;
	}
	public void new_stone() {
		int choice = (int)(Math.random()*tetris_shapes.length);
		stone = new int[tetris_shapes[choice].length][tetris_shapes[choice][0].length];
		for (int r=0; r<stone.length; r++)
			for(int c=0; c<stone[0].length; c++) 
				stone[r][c] = tetris_shapes[choice][r][c];
		stone_r = 0;
		stone_c = cols/2 -stone[0].length/2;
		if(check_collision(stone_r,stone_c))
			gameover = true;
		
	}
	public void init_game() {
		panel = new JPanel();
		panel.setBackground(Color.black);
		panel.setLayout(new GridLayout(rows,cols,1,1));
		add(panel);
		
		gridCell = new int [rows][cols];
		board = new JLabel[rows][cols];
		for (int r=0; r<rows; r++)
			for (int c=0; c<cols; c++) {
				board[r][c] = new JLabel("");
				board[r][c].setOpaque(true);
				board[r][c].setBackground(Color.lightGray);
				panel.add(board[r][c]);
			}
		help = new JPanel();
		help.setBackground(Color.black);
		help.setLayout(new GridLayout(10,1,1,1));
		add(help);
		Font font = new Font("arial",Font.BOLD,24);
		JLabel JLtetris = new JLabel("TETRIS",JLabel.CENTER);
		JLtetris.setFont(font);
		JLtetris.setForeground(Color.orange);
		help.add(JLtetris);
	
		Font font2 = new Font("arial",Font.BOLD,16);
		JLabel JLup = new JLabel("UP:rotate",JLabel.CENTER);
		JLup.setFont(font2);
		JLup.setForeground(Color.white);
		help.add(JLup);
		
		JLabel JLLeft = new JLabel("Left:move left",JLabel.CENTER);
		JLLeft.setFont(font2);
		JLLeft.setForeground(Color.white);
		help.add(JLLeft);
		
		JLabel JLright = new JLabel("Right:move right",JLabel.CENTER);
		JLright.setFont(font2);
		JLright.setForeground(Color.white);
		help.add(JLright);
		
		JLabel JLdown = new JLabel("Down:move down",JLabel.CENTER);
		JLdown.setFont(font2);
		JLdown.setForeground(Color.white);
		help.add(JLdown);
		
		JLabel JLp = new JLabel("p:pause",JLabel.CENTER);
		JLp.setFont(font2);
		JLp.setForeground(Color.white);
		help.add(JLp);
		
		JLabel JLspace = new JLabel("space:start",JLabel.CENTER);
		JLspace.setFont(font2);
		JLspace.setForeground(Color.white);
		help.add(JLspace);
	}
	public void paintGrid() {
		for (int r=0; r<rows; r++)
			for (int c=0; c<cols; c++) 
				if(gridCell[r][c] == 0)
					board[r][c].setBackground(Color.lightGray);
				else
					board[r][c].setBackground(colors[gridCell[r][c]]);
		
		for(int r=0; r<stone.length; r++)
			for (int c=0; c<stone[0].length;  c++)
				if(stone[r][c] != 0)
					board[r+stone_r][c+stone_c].setBackground(colors[stone[r][c]]);
	}
	public void merge_stone() {
		for(int r=0; r<stone.length; r++)
			for (int c=0; c<stone[0].length;  c++)
				if(stone[r][c] != 0)
					gridCell[r+stone_r][c+stone_c] = stone[r][c];
	}
	public void check_remove() {
		int r = rows -1;
		while (r>=0) {
			boolean flag = true;
			for (int c=0; c<cols;c++)
				if(gridCell[r][c] == 0) {
					flag = false;
					break;
				}
			if(flag) {
				for(int y=r-1; y>=0; y--)
					for (int c=0; c<cols;c++)
						gridCell[y+1][c] = gridCell[y][c];
					for(int c=0; c<cols; c++)
						gridCell[0][c]=0;
					paintGrid();
			}else
				r --;
		}
	}
	public void move_down() {
		if(check_collision(stone_r+1,stone_c)) {
			merge_stone();
			new_stone();
			check_remove();
		}else {
			stone_r ++;
			paintGrid();
		}
	}
	public void move_rotate() {
		int[][]temp = stone;
		int[][]rstone = new int[stone[0].length][stone.length];
		for(int r=0; r<stone.length; r++)
			for(int c=stone[0].length-1; c>= 0; c--)
				rstone[stone[0].length-1-c][r] = stone[r][c];
			stone = rstone;
			if(check_collision(stone_r,stone_c))
				stone = temp;
			else
				paintGrid();
	}
	public void move_left() {
		if(!check_collision(stone_r,stone_c-1)) {
			stone_c --;
			paintGrid();
		}
	}
	public void move_right() {
		if(!check_collision(stone_r,stone_c+1)) {
			stone_c ++;
			paintGrid();
	}
	}

	public static void main(String[] args) {
		
		new Tetris(16,8);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == timer)
			if(!gameover && !pause) {
				paintGrid();
				move_down();
			}
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 80)
			pause = !pause;
		if(gameover || pause)
			return;
		if(e.getKeyCode() == 32)
			timer.restart();
		if(e.getKeyCode() == 37)
			move_left();
		if(e.getKeyCode() == 38)
			move_rotate();
		if(e.getKeyCode() == 39)
			move_right();
		if(e.getKeyCode() == 40)
			move_down();
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	
	}

