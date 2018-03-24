import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 40;
	private static final int GRID_Y = 30;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 10;   //Last row has only one cell
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];

	// New instance variables 
	public static final int MINES = -1;
	public int [][] adjacentMine = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean [][] hGrid = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public int [][] mines = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public int totalFlags = 12;
	public boolean playerWon = false;
	public int totalMines = 12;
	

	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.WHITE;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.WHITE;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}

		//Start a New Game
		NewGame();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.GREEN);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS -1 ; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		//g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS -1 ; y++) {
				if ((x == 0) || (y != TOTAL_ROWS )) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}

		//Draw the number and paints the grids that have mines around
		Font gFont = new Font("Garamond", Font.BOLD,24);
		g.setFont(gFont);
		for(int x = 0; x < TOTAL_COLUMNS; x++){
			for (int y = 0; y < TOTAL_ROWS; y++) {
				Color numberColor = numberColor(adjacentMine[x][y]);
				g.setColor(numberColor);
				if(adjacentMine[x][y] > 0 && colorArray[x][y] == Color.LIGHT_GRAY && hGrid[x][y] == false && colorArray[x][y] != Color.RED) {
					g.drawString("  "+String.valueOf(adjacentMine[x][y]),GRID_X + ((INNER_CELL_SIZE+1)*x),y1 + (2*GRID_Y) + ((INNER_CELL_SIZE)*y));
				}
			}
		}
		//Draws the number that counts the number of flag put in the board
		Font fontFlagCounter = new Font("Garamond",Font.BOLD,12);
		g.setFont(fontFlagCounter);

		g.setColor(Color.red);
		g.drawString("Flags:" + String.valueOf(totalFlags), getWidth()/10 + 2, getHeight() - 350);

	}
	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate
	public void revealAdjacent(int x, int y){
		if((x<0) || (y<0) || (x>=9) || (y>=9)){return;}
		if (adjacentMine[x][y] == 0 && hGrid[x][y] && colorArray[x][y] != Color.RED){
			hGrid[x][y] = false;
			colorArray[x][y] = Color.LIGHT_GRAY;
			if(thePlayerWon()){
				playerWon = true;
				wonTheGame();
			}
			revealAdjacent(x-1, y);
			revealAdjacent(x+1, y);
			revealAdjacent(x, y-1);
			revealAdjacent(x, y+1);
			revealAdjacent(x + 1, y + 1);
			revealAdjacent(x + 1, y - 1);
			revealAdjacent(x - 1, y - 1);
			revealAdjacent(x - 1, y + 1);
		}
		else{
			if (adjacentMine[x][y] > 0 && hGrid[x][y] && mines[x][y] == 0){
				cellSelect(x, y);
			}
		}
	}

	public Color numberColor(int number){
		Color numberColor = null;

		switch(number){

		case 1	: 
			numberColor = Color.BLACK;
			break;
		case 2	: 
			numberColor = Color.RED;
			break;
		case 3	:
			numberColor = Color.YELLOW;
			break;
		case 4	:
			numberColor = Color.DARK_GRAY;
			break;
		case 5	: 
			numberColor = Color.CYAN;
			break;
		case 6	: 
			numberColor = Color.PINK;
			break;
		case 7	:
			numberColor = Color.ORANGE;
			break;
		}
		return numberColor;	
	}

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	//method Start a New game
	public void NewGame() {
		boardReset();
		randomMines();

		for(int x = 0; x <TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				adjacentMine[x][y] = minesAround(x,y);
			}
		}
	}
	//method reset board and a start a new game
	public void boardReset() {
		for(int x=0; x < TOTAL_ROWS; x++){
			for(int y=0; y < TOTAL_COLUMNS; y++){
				mines[x][y] = 0;
			}
		}
		for(int x=0;x<TOTAL_ROWS;x++){
			for(int y=0;y<TOTAL_COLUMNS;y++){
				adjacentMine[x][y]=0;
			}
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {  
			for (int y = 0; y < TOTAL_ROWS; y++) {
				hGrid[x][y]=true;
			}
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {  
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		totalMines = 12;
		playerWon = false;
		totalFlags = 12;
	}	
	//method set random mines inside the grid
	public void randomMines() {
		Random randomMines = new Random();
		while(totalMines != 0){
			int x = randomMines.nextInt(10);
			int y = randomMines.nextInt(10);
			if((mines[x][y] != MINES)) {
				mines[x][y] = MINES;
				totalMines--;
			}
		}
	}
//method flags
	public void flags(int x, int y) {
		if(colorArray[x][y] != Color.WHITE){
			if((colorArray[x][y] == Color.RED)){
				colorArray[x][y] = Color.DARK_GRAY;
				repaint();
				totalFlags++;
			}else if(colorArray[x][y] != Color.RED && hGrid[x][y] == true){
				//put the flag
				colorArray[x][y]=Color.RED;
				repaint();
				totalFlags -- ;
			}
		}
	}	
	//method locates and count the mines adjacent to the grid
	public int minesAround(int X, int Y){

		int totalMines = 0;
		int x_Right;
		int x_Left;
		int y_Top;
		int y_Bot;
		if(X>=9){
			x_Right = 9;
		}
		else{
			x_Right=X+1;
		}
		if(X== 0){
			x_Left = 0;
		}
		else{
			x_Left=X-1;
		}
		if(Y>=9){
			y_Bot = 9;
		}
		else{
			y_Bot = Y+1;
		}
		if(Y==0){
			y_Top = 0;
		}
		else{
			y_Top = Y-1;
		}
		for(int a= x_Left;a<=x_Right;a++ ){
			for(int b = y_Top;b<=y_Bot;b++){
				if((a==X && b == Y)|| mines[X][Y] == MINES){
					continue;
				}
				else{
					if(mines[a][b] == MINES){
						totalMines++;
					}
				}
			}
		}
		return totalMines;
	}
	// method to tell if there are 12 hidden grids in the board
	public boolean thePlayerWon() {
		int HiddenCell = 0;
		for(int x = 0; x < TOTAL_COLUMNS; x++) {
			for( int y = 0; y < TOTAL_ROWS; y++) {
				if(hGrid[x][y]) {
					HiddenCell++;
				}
			}
		}
		return(HiddenCell == 12);
	}
	//Frame for when the game ends
	public void wonTheGame(){

		mineShow();
		repaint();

		int button = JOptionPane.showConfirmDialog(null, "YOU WIN!! WANT TO PLAY AGAIN? ", null, JOptionPane.YES_NO_OPTION);
		if(button == JOptionPane.YES_OPTION) {
			boardReset();
			NewGame();
		}
		else {
			System.exit(0);
		}
	}
	public void lostTheGame(){
		mineShow();
		repaint();
		int theButtonPressed = JOptionPane.showConfirmDialog(null, "YOU LOST!! WANT TO PLAY AGAIN? ", null, JOptionPane.YES_NO_OPTION);
		if(theButtonPressed == JOptionPane.YES_OPTION) {
			boardReset();
			NewGame();
		}
		else {
			System.exit(0);
		}
	}
	//method to show the mines if the player lost
	public void mineShow() {
		for(int x=0;x<TOTAL_ROWS;x++){
			for(int y=0;y<TOTAL_COLUMNS;y++){
				if(mines[x][y]==MINES){
					colorArray[x][y] = Color.BLACK;
				}
			}
		}
	}
	//method is used when the player select a grid
	public void cellSelect(int x, int y) {
		if((hGrid[x][y] && colorArray[x][y] != Color.RED)){ 
			if(mines[x][y] == MINES){
				lostTheGame();
			}
			else{
				if(adjacentMine[x][y] > 0){
					colorArray[x][y] = Color.LIGHT_GRAY;
					hGrid[x][y] = false;
					if(thePlayerWon()){
						playerWon = true;
						wonTheGame();
					}
				}else if(adjacentMine[x][y] == 0){
					revealAdjacent(x, y);
				}
			}
		}
	}
}