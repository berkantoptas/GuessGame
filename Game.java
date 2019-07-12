import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;


public class Game extends JFrame
{	
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem exit;
	
	private JButton userNumberButton, userGuessButton, computerGuessButton, computerGuessResultSubmitButton, gameStartButton;
	
	private JTextPane userPlusTextPane, userMinusTextPane, computerPlusTextPane, computerMinusTextPane, userGuessLogTextPane;
	
	private Font font;
	private float fontSize;
	
	private String userNumber;
	private String userGuess = "";
	
	private int userGuessCount = 0;
	private int computerGuessCount = 0;
	
	private ArrayList<String> computerGuessList = new ArrayList<String>();
	
	private int[] computerNumberArr = new int[4];
	private int[] userNumberArr = new int[4];
	private int[] userGuessArr = new int[4];
	
	private int userPlusCount = 0;
	private int userMinusCount = 0;
	
	public void InitComputerGuessList()		//Initialize computer guess list with different digit of 4-digit numbers 
	{
		int num1, num2, num3, num4;
		
		for(int i = 1000 ; i < 10000 ; i++)
		{
			num1=Integer.toString(i).charAt(0);
			num2=Integer.toString(i).charAt(1);
			num3=Integer.toString(i).charAt(2);
			num4=Integer.toString(i).charAt(3);
			if(num1!=num2 && num1!=num3 && num1!=num4 && num2!=num3 && num2!=num4 && num3!=num4) //Different digits control
			{
				computerGuessList.add(Integer.toString(i));
			}
		}
	}
	
	public void UpdateComputerGuessList(String computerGuessNumber, int computerPlus, int computerMinus) //Update computer guess list considering computer(+/-) counts
	{	
		int hintSum = computerPlus + computerMinus*(-1) ;
		computerGuessList.remove(computerGuessNumber); //Computers last guess
		
		int num1, num2, num3, num4;
		int numg1, numg2, numg3, numg4;
		
		numg1=computerGuessNumber.charAt(0);
		numg2=computerGuessNumber.charAt(1);
		numg3=computerGuessNumber.charAt(2);
		numg4=computerGuessNumber.charAt(3);
		
		for(int i = 0 ; i < computerGuessList.size() ; i++)
		{
			num1=computerGuessList.get(i).charAt(0);
			num2=computerGuessList.get(i).charAt(1);
			num3=computerGuessList.get(i).charAt(2);
			num4=computerGuessList.get(i).charAt(3);
			
			boolean bool1 = (num1==numg1 || num1==numg2 || num1==numg3 || num1==numg4);		//Next guess digits & last guess digits comparing
			boolean bool2 = (num2==numg1 || num2==numg2 || num2==numg3 || num2==numg4);		
			boolean bool3 = (num3==numg1 || num3==numg2 || num3==numg3 || num3==numg4);
			boolean bool4 = (num4==numg1 || num4==numg2 || num4==numg3 || num4==numg4);
			
			if(hintSum==0)																	
			{
				if(bool1 || bool2 || bool3 || bool4)
				{															
					computerGuessList.remove(computerGuessList.get(i));		//if hintsum=0 after guess result any digits in guess wont be in next guesses
					i--;																	//Index reduction after remove item from arraylist
				}
			}
			if(hintSum==1)
			{
				if((bool1 && !bool2 && !bool3 && !bool4) || (!bool1 && bool2 && !bool3 && !bool4) || 
				   (!bool1 && !bool2 && bool3 && !bool4) || (!bool1 && !bool2 && !bool3 && bool4)   )
					continue;
				else
				{
					computerGuessList.remove(computerGuessList.get(i));		//if hintsum=1 after guess result there will be 1 digit in next guesses
					i--;																	//Index reduction after remove item from arraylist
				}
			}
			if(hintSum==2)
			{
				if((bool1 && bool2 && !bool3 && !bool4) || (bool1 && !bool2 && bool3 && !bool4) || 
				   (bool1 && !bool2 && !bool3 && bool4) || (!bool1 && bool2 && bool3 && !bool4) || 
				   (!bool1 && bool2 && !bool3 && bool4) || (!bool1 && !bool2 && bool3 && bool4)   )
					continue;
				else
				{
					computerGuessList.remove(computerGuessList.get(i));		//if hintsum=2 after guess result there will be 2 digit in next guesses
					i--;																	//Index reduction after remove item from arraylist
				}
			}
			if(hintSum==3)
			{
				if((bool1 && bool2 && bool3 && !bool4) || (bool1 && bool2 && !bool3 && bool4) || 
				   (bool1 && !bool2 && bool3 && bool4) || (!bool1 && bool2 && bool3 && bool4)   )
					continue;
				else
				{
					computerGuessList.remove(computerGuessList.get(i));		//if hintsum=3 after guess result there will be 3 digit in next guesses
					i--;																	//Index reduction after remove item from arraylist
				}
			}
			if(hintSum==4)
			{
				if(bool1 && bool2 && bool3 && bool4)
					continue;
				else
				{
					computerGuessList.remove(computerGuessList.get(i));		//if hintsum=4 after guess result computer will win!
					i--;																	//Index reduction after remove item from arraylist
				}
			}
		}
	}
	
	
	public Game()		//USER INTERFACE
	{		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Menu");
		menuBar.add(menu);
		
		exit = new JMenuItem("Exit");
		menu.add(exit);
				
		Container pane = this.getContentPane();
		
		//USER PANEL
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// User number
		userNumberButton = new JButton("My Number");
		userNumberButton.setPreferredSize(new Dimension(150,60));
		userNumberButton.setEnabled(false);
		panel1.add(userNumberButton);
		
		// User + box
		userPlusTextPane = new JTextPane();
		userPlusTextPane.setPreferredSize(new Dimension(50, 50));
		font = userPlusTextPane.getFont();
		fontSize = font.getSize() + 20.0f;
		userPlusTextPane.setFont( font.deriveFont(fontSize) );
		userPlusTextPane.setEnabled(false);
		panel1.add(userPlusTextPane);
		
		// User - box
		userMinusTextPane = new JTextPane();
		userMinusTextPane.setPreferredSize(new Dimension(50, 50));
		font = userMinusTextPane.getFont();
		fontSize = font.getSize() + 20.0f;
		userMinusTextPane.setFont( font.deriveFont(fontSize) );
		userMinusTextPane.setEnabled(false);
		panel1.add(userMinusTextPane);
		
		// User guess
		userGuessButton = new JButton("Guess");
		userGuessButton.setPreferredSize(new Dimension(100,40));
		userGuessButton.setEnabled(false);
		panel1.add(userGuessButton);
		
		Border userBorder = BorderFactory.createTitledBorder("USER");
		panel1.setBorder(userBorder);
		
		pane.add(panel1);
		
		// User Guess Log
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		userGuessLogTextPane = new JTextPane();
		userGuessLogTextPane.setPreferredSize(new Dimension(400, 400));
		font = userGuessLogTextPane.getFont();
		fontSize = font.getSize() + 10.0f;
		userGuessLogTextPane.setFont( font.deriveFont(fontSize) );
		JScrollPane scroll = new JScrollPane(userGuessLogTextPane);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		userGuessLogTextPane.setEnabled(false);
		panel2.add(scroll);
		
		pane.add(panel2);
	
		//COMPUTER PANEL
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Computer + box
		computerPlusTextPane = new JTextPane();
		computerPlusTextPane.setPreferredSize(new Dimension(50, 50));
		font = computerPlusTextPane.getFont();
		fontSize = font.getSize() + 20.0f;
		computerPlusTextPane.setFont( font.deriveFont(fontSize) );
		computerPlusTextPane.setEnabled(false);
		panel3.add(computerPlusTextPane);
		
		// Computer - box
		computerMinusTextPane = new JTextPane();
		computerMinusTextPane.setPreferredSize(new Dimension(50, 50));
		font = computerMinusTextPane.getFont();
		fontSize = font.getSize() + 20.0f;
		computerMinusTextPane.setFont( font.deriveFont(fontSize) );
		computerMinusTextPane.setEnabled(false);
		panel3.add(computerMinusTextPane);
		
		// Computer guess
		computerGuessButton = new JButton("Guess");
		computerGuessButton.setPreferredSize(new Dimension(100,40));
		computerGuessButton.setEnabled(false);
		panel3.add(computerGuessButton);
		
		// Computer roll
		computerGuessResultSubmitButton = new JButton("Roll");
		computerGuessResultSubmitButton.setPreferredSize(new Dimension(100,40));
		computerGuessResultSubmitButton.setEnabled(false);
		panel3.add(computerGuessResultSubmitButton);
		
		Border computerBorder = BorderFactory.createTitledBorder("COMPUTER");
		panel3.setBorder(computerBorder);
		
		pane.add(panel3);
		
		//START PANEL
		JPanel panel4 = new JPanel();
		panel4.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		gameStartButton = new JButton("START");
		gameStartButton.setPreferredSize(new Dimension(200,80));
		panel4.add(gameStartButton);
		
		pane.add(panel4);
		
		//EVENTS
		ExitMenuEvent eme = new ExitMenuEvent();
		exit.addActionListener(eme);
		
		UserNumberEvent une = new UserNumberEvent();	//USER NUMBER BUTTON
		userNumberButton.addActionListener(une);
		
		UserGuessEvent uge = new UserGuessEvent();	//USER GUESS BUTTON
		userGuessButton.addActionListener(uge);
		
		ComputerGuessEvent cge = new ComputerGuessEvent();	//COMPUTER GUESS BUTTON
		computerGuessButton.addActionListener(cge);
		
		ComputerGuessResultSubmitEvent cgrse = new ComputerGuessResultSubmitEvent();	//COMPUTER GUESS RESULT SUBMIT BUTTON
		computerGuessResultSubmitButton.addActionListener(cgrse);
		
		GameStartEvent gse = new GameStartEvent();	//GAME START BUTTON
		gameStartButton.addActionListener(gse);
		
	}
	
	public class ExitMenuEvent implements ActionListener		//EXIT MENU ITEM
	{
		public void actionPerformed(ActionEvent eme)
		{
			System.exit(0);
		}
	}	
	
	public class UserNumberEvent implements ActionListener		//USER NUMBER BUTTON
	{	
		public void actionPerformed(ActionEvent une)
		{
			userNumber = JOptionPane.showInputDialog(Game.this , "Enter your number:", "Number", JOptionPane.OK_OPTION|JOptionPane.QUESTION_MESSAGE);
			
			int num1, num2, num3, num4;
			
			if(userNumber.length()==4 && userNumber.charAt(0)!='0')	//4-digit control , First digit cant be 0
			{
				num1=userNumber.charAt(0);
				num2=userNumber.charAt(1);
				num3=userNumber.charAt(2);
				num4=userNumber.charAt(3);
				
				if(num1!=num2 && num1!=num3 && num1!=num4 && num2!=num3 && num2!=num4 && num3!=num4)	//Different digits control
				{
					userGuessButton.setEnabled(true);
					userNumberButton.setEnabled(false);
					Decompose(Integer.parseInt(userNumber), userNumberArr);
					System.out.print("[Hint] User Number: ");
					PrintArray(userNumberArr);
					System.out.println();
				}
				else
				{
					JOptionPane.showMessageDialog(Game.this, "Wrong Input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(Game.this, "Wrong Input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public class UserGuessEvent implements ActionListener		//USER GUESS BUTTON
	{	
		public void actionPerformed(ActionEvent uge)
		{
			userGuess = JOptionPane.showInputDialog(Game.this , "Enter your guess:", "Guess", JOptionPane.OK_OPTION|JOptionPane.QUESTION_MESSAGE);
			
			int num1, num2, num3, num4;
			
			if(userGuess.length()==4 && userGuess.charAt(0)!='0')	//4-digit control , First digit cant be 0
			{
				num1=userGuess.charAt(0);
				num2=userGuess.charAt(1);
				num3=userGuess.charAt(2);
				num4=userGuess.charAt(3);
				
				if(num1!=num2 && num1!=num3 && num1!=num4 && num2!=num3 && num2!=num4 && num3!=num4)	//Different digits control
				{
					userGuessCount++;
					userGuessButton.setEnabled(false);
					computerGuessButton.setEnabled(true);
					Decompose(Integer.parseInt(userGuess), userGuessArr);
					
					for (int i = 0 ; i < 4 ; i++) 
					{
						for (int j = 0 ; j < 4 ; j++) 
						{
							if (computerNumberArr[i] == userGuessArr[j] && i == j)
							{
								userPlusCount++;
							}
							if (computerNumberArr[i] == userGuessArr[j] && i != j)
							{
								userMinusCount--;
							}
						}
					}
					
					userPlusTextPane.setText(Integer.toString(userPlusCount));
					userMinusTextPane.setText(Integer.toString(userMinusCount));
					
					if(userPlusCount==4)						//User win
					{
						JOptionPane.showMessageDialog(Game.this, "Right Guess!!(User)", "Info", JOptionPane.INFORMATION_MESSAGE);
						userNumberButton.setEnabled(false);
						userGuessButton.setEnabled(false);
						computerGuessButton.setEnabled(false);
						computerGuessResultSubmitButton.setEnabled(false);
						gameStartButton.setEnabled(true);
						userPlusTextPane.setEnabled(false);
						userMinusTextPane.setEnabled(false);
						computerPlusTextPane.setEnabled(false);
						computerMinusTextPane.setEnabled(false);
						userPlusTextPane.setText("");
						userMinusTextPane.setText("");
						computerPlusTextPane.setText("");
						computerMinusTextPane.setText("");
						userGuessLogTextPane.setText("User Won!! - (" + userGuessCount + " attempts)\n");
						userGuessCount = 0;
						computerGuessCount = 0;
					}
					else
					{
						StyledDocument doc = userGuessLogTextPane.getStyledDocument();
						try {
							doc.insertString(doc.getLength(), "User Last Guess: " + userGuess + " " + userPlusCount + " " + userMinusCount + "\n", null);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
					userPlusCount=0;
					userMinusCount=0;
				}
				else
				{
					JOptionPane.showMessageDialog(Game.this, "Wrong Input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(Game.this, "Wrong Input", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public class ComputerGuessEvent implements ActionListener		//COMPUTER GUESS BUTTON
	{	
		public void actionPerformed(ActionEvent cge)
		{
			JOptionPane.showMessageDialog(Game.this, computerGuessList.get(computerGuessList.size()/2), "Computer Guess", JOptionPane.PLAIN_MESSAGE);
			computerGuessButton.setEnabled(false);
			computerPlusTextPane.setEnabled(true);
			computerMinusTextPane.setEnabled(true);
			computerGuessResultSubmitButton.setEnabled(true);
			computerGuessCount++;
		}
	}
	
	public class ComputerGuessResultSubmitEvent implements ActionListener		//COMPUTER GUESS RESULT SUBMIT BUTTON
	{	
		public void actionPerformed(ActionEvent cgrse)
		{
			computerGuessResultSubmitButton.setEnabled(false);
			userGuessButton.setEnabled(true);
			computerPlusTextPane.setEnabled(false);
			computerMinusTextPane.setEnabled(false);
			
			if(Integer.parseInt(computerPlusTextPane.getText()) == 4)		//Computer win
			{
				JOptionPane.showMessageDialog(Game.this, "Right Guess!!(Computer)", "Info", JOptionPane.INFORMATION_MESSAGE);
				userNumberButton.setEnabled(false);
				userGuessButton.setEnabled(false);
				computerGuessButton.setEnabled(false);
				computerGuessResultSubmitButton.setEnabled(false);
				gameStartButton.setEnabled(true);
				userPlusTextPane.setEnabled(false);
				userMinusTextPane.setEnabled(false);
				computerPlusTextPane.setEnabled(false);
				computerMinusTextPane.setEnabled(false);
				userPlusTextPane.setText("");
				userMinusTextPane.setText("");
				computerPlusTextPane.setText("");
				computerMinusTextPane.setText("");
				userGuessLogTextPane.setText("Computer Won!! - (" + computerGuessCount + " attempts)\n");
				userGuessCount = 0;
				computerGuessCount = 0;
			}
			else
			{
				UpdateComputerGuessList(computerGuessList.get(computerGuessList.size()/2), Integer.parseInt(computerPlusTextPane.getText()), Integer.parseInt(computerMinusTextPane.getText()));
			}
								
			computerPlusTextPane.setText("");
			computerMinusTextPane.setText("");
		}
	}
	
	public class GameStartEvent implements ActionListener		//GAME START BUTTON
	{	
		public void actionPerformed(ActionEvent gse)
		{
			InitComputerGuessList();
			
			userNumberButton.setEnabled(true);
			userGuessButton.setEnabled(false);
			computerGuessButton.setEnabled(false);
			computerGuessResultSubmitButton.setEnabled(false);
			gameStartButton.setEnabled(false);
			userPlusTextPane.setEnabled(false);
			userMinusTextPane.setEnabled(false);
			computerPlusTextPane.setEnabled(false);
			computerMinusTextPane.setEnabled(false);
			userGuessLogTextPane.setEnabled(false);
			userGuessLogTextPane.setText("");
			
			computerNumberArr = RandomNumber();
			System.out.print("[Hint] Computer Number: ");
			PrintArray(computerNumberArr);
			System.out.println();
		}
	}
	
	public static int[] RandomNumber()		//Creating Different digit of 4-digit numbers
	{
		Random r = new Random();
		
		int num1, num2, num3, num4;
		int randomArray[] = new int[4];
		num1 = r.nextInt(9) + 1 ; 		//First digit cant be 0
		boolean isValid = false;
		
		while(isValid!=true)
		{
			num2 = r.nextInt(10);
			num3 = r.nextInt(10);
			num4 = r.nextInt(10);
		
			if(num1!=num2 && num1!=num3 && num1!=num4 && num2!=num3 && num2!=num4 && num3!=num4)	//Different digits control
			{
				randomArray[0] = num1;
				randomArray[1] = num2;
				randomArray[2] = num3;
				randomArray[3] = num4;
				isValid = true;
			}
		}
		return randomArray;
	}
	
	public static int[] Decompose(int number, int[] array)		//Decomposing numbers digits
	{
		while (number > 0) 
		{
			for (int i = 0; i < 4; i++) 
			{
				int arrayNum = 0;
				arrayNum = number % 10;
				array[4 - i - 1] = arrayNum;
				number = number / 10;
			}
		}
		return array;											//int to int[]
	}
	
	public static void PrintArray(int[] array)					//Print array as Integer
	{
		for (int i = 0 ; i < 4 ; i++)
		{
			System.out.print(array[i]);
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{	
		Game newGame = new Game();
		
		newGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newGame.setSize(450,800);
		newGame.setVisible(true);
		newGame.setResizable(true);
		newGame.setTitle("Guess Game");
	}
	
}
