package org.speed_reader.gui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputAdapter;

import com.sun.xml.internal.txw2.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileNotFoundException;

import static java.time.temporal.ChronoUnit.MINUTES;
import org.speed_reader.data.*;

public class MainGUI extends JFrame {
	
//Styling Global Vars
/*------------------------------------------------------------------------------------------------------------------------------------*/	
	int WINDOW_WIDTH = DPIScaling.scaleInt(1000); 
	int WINDOW_HEIGHT = DPIScaling.scaleInt(900);
	Color defaultBackgroundColor = new Color(24,145,227);  	//rgb - light blue color
	Color defaultTextColor = new Color(90,90,95);       	//rgb - gray
	int defaultFontSize = DPIScaling.scaleInt(12);
	int emphasisFontSize = DPIScaling.scaleInt(18);
/*------------------------------------------------------------------------------------------------------------------------------------*/	
//Provides for dynamic font sizing of reading
	
	public int readingFontSize = DPIScaling.scaleInt(22);													//User definable font size for the reading
	
	//Empirical Reading Panel tuning parameters based on window_height -- until I find a better way.
	public int numReadingRows = (int)(WINDOW_HEIGHT/18*(12/readingFontSize));			//Number of Rows in the Reading Pane (char tall)
	public int numReadingCols = (int)(WINDOW_WIDTH/13*(1.05*12/readingFontSize));			//Number of Cols in the Reading Pane (char wide)
/*------------------------------------------------------------------------------------------------------------------------------------*/	
//User & Document variables
	public String userName = "Maria";
	public LocalDateTime startTime = LocalDateTime.now(); // current time
	public int currWPM = 30;
	public int fastestWPMToday;
	
	ArrayList<String> documents = new ArrayList<String>();
/*------------------------------------------------------------------------------------------------------------------------------------*/	
//SWING GUI Global Objs
	private static final long serialVersionUID = 1L;
	JFrame mainFrame;
	DocumentListPanel docListPanel;
		//Globally accessible JList in docListPanel
		public JList<String> docList;
		public DefaultListModel<String> model;
	
		//STYLE Elements within docListPanel

		public JScrollPane listScroller; 			//scrollbar for the JList
	
	DocumentStatisticsPanel docStatsPanel;
	JTextPane textPane;								//document display pane
	TextHighlighter textHighlighter;					//document-highlighting object
/*------------------------------------------------------------------------------------------------------------------------------------*/	
				
	
	MainGUI(){
		//Instantiate new JFrame with Title
		super("Speed Reading Trainer");


		
		//DEBUG ONLY:
		for (int i=0;i < 50; i++) {
			documents.add("C:\\texts\\Document"+i+".txt");
		}
		documents.add("D:\\Document.txt");
		
		//Set Default Design Parameters
		setLayout(new BorderLayout());
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, DPIScaling.scaleInt(10), DPIScaling.scaleInt(10)));
		getRootPane().setWindowDecorationStyle(JRootPane.FRAME);


		
		modifyColors();
		
		//Setup the Initial Internals
		buildMainFrame();
		
		//Set JFrame to be moveable
		DragListener drag = new DragListener();
		addMouseListener( drag );
		addMouseMotionListener( drag );
		
		//Display
		setVisible(true);
		
	}
	private void modifyColors() {
	       getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBackgroundColor));  //set outside light blue border
	}
	
	private void buildMainFrame() {
		
		WestPanel westPanel = new WestPanel();
		add(westPanel, BorderLayout.WEST);
		
		SouthPanel southPanel = new SouthPanel();
		add(southPanel,BorderLayout.SOUTH);
		
		CenterPanel centerPanel = new CenterPanel();
		add(centerPanel,BorderLayout.CENTER);
		
		pack();
		setVisible(true);
		
	}
	
	public void updateDocList(){
		docListPanel.debugTestRepaint();
		docListPanel.revalidate();
		docListPanel.repaint();
	}
	
	public void updateStats(){
		docStatsPanel.revalidate();
		docStatsPanel.repaint();
	}
	
	public void startReading(){
		textHighlighter.startReading();
	}
	
	public void startReading(int wpm){
		textHighlighter.startReading(wpm);
	}
	
	public class DragListener extends MouseInputAdapter
	{
	    Point location;
	    MouseEvent pressed;
	 
	    public void mousePressed(MouseEvent me)
	    {
	        pressed = me;
	    }
	 
	    public void mouseDragged(MouseEvent me)
	    {
	        Component component = me.getComponent();
	        location = component.getLocation(location);
	        int x = location.x - pressed.getX() + me.getX();
	        int y = location.y - pressed.getY() + me.getY();
	        component.setLocation(x, y);
	     }
	}
	
	private class CenterPanel extends JPanel{

		private static final long serialVersionUID = 1L;
		
		

		CenterPanel(){
			
			int width=(int)(WINDOW_WIDTH*.75);
			int height=(int)(WINDOW_HEIGHT*.9);
			
			setLayout(new BorderLayout());
			this.setSize(width,height);
			this.setMaximumSize(new Dimension(width, (int)height));
			setBackground(defaultBackgroundColor);
			//setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, defaultBackgroundColor));
			
			JLabel wpmLabel = new JLabel("<html><p><font color=#5A5A5F size=5>Current Word Speed: " +"<font color=green>"+currWPM+"<font color=#5A5A5F> WPM</p></html>");
			wpmLabel.setForeground(defaultTextColor);
			wpmLabel.setBackground(Color.WHITE);
			wpmLabel.setOpaque(true);				//otherwise background goes unpainted
			wpmLabel.setMaximumSize(new Dimension(100,(int)(height*.1)));
			wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
			wpmLabel.setVerticalAlignment(SwingConstants.CENTER);
			wpmLabel.setBorder(BorderFactory.createMatteBorder(4, 400, 4, 400, defaultBackgroundColor));
			
			add(wpmLabel, BorderLayout.NORTH);
			
			try {
				Scanner testScanner = new Scanner(new File("src/org/speed_reader/gui/test2.txt"));
				String testDocStr = testScanner.useDelimiter("\\Z").next();
				testScanner.close();
				textHighlighter = new TextHighlighter(testDocStr, new Pointer());
//				textHighlighter.startReading(500);
			} catch(FileNotFoundException e){
				System.out.println("Error: file not found.");
			}
			textPane = textHighlighter.getTextPane();
			JScrollPane textScrollPane = new JScrollPane(textPane); 
			textPane.setEditable(false);
			textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			Border border = BorderFactory.createMatteBorder(0, 0, 0, 0, defaultBackgroundColor);
			Border margin = new EmptyBorder(0,10,0,10);
			textScrollPane.setBorder(new CompoundBorder(border, margin));
			
			
			//populate the textArea
			
/*			//DEBUG ONLY:
			String loremIpsumString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam elementum at lacus blandit ultricies. Pellentesque interdum, ex et facilisis laoreet, eros risus hendrerit neque, a interdum metus erat nec odio. Curabitur ultricies at diam ac tempus. Donec venenatis erat vel ante efficitur fringilla. Proin ante tortor, euismod eu felis non, vulputate laoreet neque. Quisque eget tincidunt nisi. Morbi iaculis enim nec turpis imperdiet ultricies." + 
					"Maecenas lobortis mollis finibus. Suspendisse fermentum elit at enim pretium mollis. Nunc non augue molestie, gravida turpis et, ultricies nibh. Duis erat ex, ornare et est a, dictum hendrerit sem. Duis fringilla diam ligula, at volutpat odio maximus eget. Aenean aliquam quam elementum ex vulputate, in tincidunt ante dapibus. Sed vestibulum ex vitae venenatis sodales. Cras fringilla eu orci quis sodales." + 
					"\r\n\r\nCras convallis magna nec cursus tempor. Etiam purus felis, imperdiet at fermentum vel, eleifend vitae velit. Vestibulum vitae euismod ipsum. Quisque fermentum tortor purus, et sollicitudin quam sollicitudin eu. Aenean tincidunt eros eget orci vulputate, vel posuere felis finibus. Donec facilisis, erat auctor semper molestie, tellus lorem molestie massa, quis porta justo orci et orci. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum in magna tempor, efficitur enim id, porttitor magna.\r\n" + 
					"Nam feugiat pellentesque nisi nec vulputate. Ut cursus urna at lectus mattis, vitae finibus ipsum fringilla. Nullam a pulvinar mauris. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum et dolor commodo nulla lobortis scelerisque. Curabitur hendrerit erat elit, ut fringilla justo elementum id. Etiam eu turpis quis odio dapibus blandit. Ut nunc arcu, dapibus eget consequat varius, suscipit porta leo. Duis at rhoncus nisl. Aenean placerat congue iaculis.\r\n" + 
					"Vivamus cursus varius convallis. Fusce nec nulla augue. Donec gravida ultricies consectetur. Nullam luctus luctus urna ut vulputate. Etiam cursus lacinia mi, vel euismod lectus. Fusce non mattis augue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut diam mauris, porttitor id fermentum non, ultricies sit amet lorem. Nulla odio purus, varius et tincidunt vel, gravida id nulla." + 
					"\r\n\r\nSuspendisse potenti. Duis eget suscipit metus. In quis elementum metus. Integer ac efficitur ligula. Morbi vel libero erat. Aliquam rhoncus ipsum leo, sollicitudin facilisis turpis viverra in. Phasellus ut placerat libero. Mauris vel turpis in nibh faucibus vehicula. Aenean laoreet ultricies luctus. Aliquam pretium metus id tortor tincidunt, nec venenatis metus dignissim. In facilisis dapibus leo eget dignissim.\r\n\r\n" + 
					"Nulla luctus in nibh ac ullamcorper. Sed at sollicitudin odio. Sed convallis felis id imperdiet tristique. Integer dui odio, rutrum eu mattis nec, malesuada sit amet nibh. Nulla pellentesque massa vulputate ante dapibus, non euismod risus pretium. Nam ut fermentum nibh. Aenean faucibus urna eu semper egestas. Nullam id ligula nulla. Duis dignissim hendrerit urna malesuada accumsan. Pellentesque vel tellus euismod, aliquam tellus sed, laoreet justo. Nunc tempus nisi sed diam tristique varius. Aenean velit turpis, tristique ut est nec, ultricies efficitur ipsum. Ut suscipit efficitur ligula. Quisque scelerisque sem massa." + 
					"Sed tempor metus metus, eget efficitur neque luctus sit amet. Donec iaculis dui tellus, ac mollis arcu aliquet in. Nam euismod sagittis ultrices. Suspendisse tempus sem velit. Donec placerat auctor nunc, rhoncus feugiat augue ornare eget. Mauris interdum ultrices nisl. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut laoreet dui eu risus ultricies accumsan. Aliquam at massa pellentesque, accumsan erat condimentum, sollicitudin nibh. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Pellentesque id magna suscipit, tempor dolor nec, bibendum urna. Duis nec malesuada metus, sit amet tincidunt dolor. Aenean dapibus, nulla et accumsan dapibus, enim lacus viverra nisl, nec consequat libero quam vel lectus. Aliquam ullamcorper sem ac lectus luctus, quis cursus lectus auctor." + 
					"Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Morbi cursus, velit ut faucibus dignissim, elit urna eleifend ipsum, eget imperdiet lorem arcu quis mi. Vestibulum non posuere urna. Pellentesque sagittis gravida dolor, nec tincidunt dui finibus sed. Duis sit amet est id tortor convallis pharetra in et mi. Donec blandit finibus diam quis feugiat. Ut accumsan mattis tortor in tristique. Integer rutrum sagittis arcu, condimentum congue arcu malesuada et. Vivamus vestibulum ullamcorper ipsum, at facilisis enim interdum quis. Praesent varius, turpis eget imperdiet scelerisque, nibh justo tristique ligula, id tempus tellus libero at dui. Duis dictum ante augue, id iaculis tellus molestie vitae. Morbi a velit nisi." + 
					"Maecenas non pellentesque ex. Suspendisse placerat facilisis lectus vitae accumsan. Cras vehicula ante eget mauris convallis, vel scelerisque ex ornare. Nunc mauris urna, porttitor at risus id, dapibus convallis tortor. Pellentesque id dui consequat, scelerisque ipsum non, scelerisque justo. Integer magna turpis, sollicitudin sit amet varius in, egestas in metus. Suspendisse faucibus interdum velit, sit amet pretium ante porttitor nec. Suspendisse potenti. "+
					"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam elementum at lacus blandit ultricies. Pellentesque interdum, ex et facilisis laoreet, eros risus hendrerit neque, a interdum metus erat nec odio. Curabitur ultricies at diam ac tempus. Donec venenatis erat vel ante efficitur fringilla. Proin ante tortor, euismod eu felis non, vulputate laoreet neque. Quisque eget tincidunt nisi. Morbi iaculis enim nec turpis imperdiet ultricies." + 
					"Maecenas lobortis mollis finibus. Suspendisse fermentum elit at enim pretium mollis. Nunc non augue molestie, gravida turpis et, ultricies nibh. Duis erat ex, ornare et est a, dictum hendrerit sem. Duis fringilla diam ligula, at volutpat odio maximus eget. Aenean aliquam quam elementum ex vulputate, in tincidunt ante dapibus. Sed vestibulum ex vitae venenatis sodales. Cras fringilla eu orci quis sodales." + 
					"\r\n\r\nCras convallis magna nec cursus tempor. Etiam purus felis, imperdiet at fermentum vel, eleifend vitae velit. Vestibulum vitae euismod ipsum. Quisque fermentum tortor purus, et sollicitudin quam sollicitudin eu. Aenean tincidunt eros eget orci vulputate, vel posuere felis finibus. Donec facilisis, erat auctor semper molestie, tellus lorem molestie massa, quis porta justo orci et orci. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum in magna tempor, efficitur enim id, porttitor magna.\r\n" + 
					"Nam feugiat pellentesque nisi nec vulputate. Ut cursus urna at lectus mattis, vitae finibus ipsum fringilla. Nullam a pulvinar mauris. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum et dolor commodo nulla lobortis scelerisque. Curabitur hendrerit erat elit, ut fringilla justo elementum id. Etiam eu turpis quis odio dapibus blandit. Ut nunc arcu, dapibus eget consequat varius, suscipit porta leo. Duis at rhoncus nisl. Aenean placerat congue iaculis.\r\n" + 
					"Vivamus cursus varius convallis. Fusce nec nulla augue. Donec gravida ultricies consectetur. Nullam luctus luctus urna ut vulputate. Etiam cursus lacinia mi, vel euismod lectus. Fusce non mattis augue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut diam mauris, porttitor id fermentum non, ultricies sit amet lorem. Nulla odio purus, varius et tincidunt vel, gravida id nulla." + 
					"\r\n\r\nSuspendisse potenti. Duis eget suscipit metus. In quis elementum metus. Integer ac efficitur ligula. Morbi vel libero erat. Aliquam rhoncus ipsum leo, sollicitudin facilisis turpis viverra in. Phasellus ut placerat libero. Mauris vel turpis in nibh faucibus vehicula. Aenean laoreet ultricies luctus. Aliquam pretium metus id tortor tincidunt, nec venenatis metus dignissim. In facilisis dapibus leo eget dignissim.\r\n" + 
					"Nulla luctus in nibh ac ullamcorper. Sed at sollicitudin odio. Sed convallis felis id imperdiet tristique. Integer dui odio, rutrum eu mattis nec, malesuada sit amet nibh. Nulla pellentesque massa vulputate ante dapibus, non euismod risus pretium. Nam ut fermentum nibh. Aenean faucibus urna eu semper egestas. Nullam id ligula nulla. Duis dignissim hendrerit urna malesuada accumsan. Pellentesque vel tellus euismod, aliquam tellus sed, laoreet justo. Nunc tempus nisi sed diam tristique varius. Aenean velit turpis, tristique ut est nec, ultricies efficitur ipsum. Ut suscipit efficitur ligula. Quisque scelerisque sem massa." + 
					"Sed tempor metus metus, eget efficitur neque luctus sit amet. Donec iaculis dui tellus, ac mollis arcu aliquet in. Nam euismod sagittis ultrices. Suspendisse tempus sem velit. Donec placerat auctor nunc, rhoncus feugiat augue ornare eget. Mauris interdum ultrices nisl. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut laoreet dui eu risus ultricies accumsan. Aliquam at massa pellentesque, accumsan erat condimentum, sollicitudin nibh. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Pellentesque id magna suscipit, tempor dolor nec, bibendum urna. Duis nec malesuada metus, sit amet tincidunt dolor. Aenean dapibus, nulla et accumsan dapibus, enim lacus viverra nisl, nec consequat libero quam vel lectus. Aliquam ullamcorper sem ac lectus luctus, quis cursus lectus auctor." + 
					"Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Morbi cursus, velit ut faucibus dignissim, elit urna eleifend ipsum, eget imperdiet lorem arcu quis mi. Vestibulum non posuere urna. Pellentesque sagittis gravida dolor, nec tincidunt dui finibus sed. Duis sit amet est id tortor convallis pharetra in et mi. Donec blandit finibus diam quis feugiat. Ut accumsan mattis tortor in tristique. Integer rutrum sagittis arcu, condimentum congue arcu malesuada et. Vivamus vestibulum ullamcorper ipsum, at facilisis enim interdum quis. Praesent varius, turpis eget imperdiet scelerisque, nibh justo tristique ligula, id tempus tellus libero at dui. Duis dictum ante augue, id iaculis tellus molestie vitae. Morbi a velit nisi." + 
					"Maecenas non pellentesque ex. Suspendisse placerat facilisis lectus vitae accumsan. Cras vehicula ante eget mauris convallis, vel scelerisque ex ornare. Nunc mauris urna, porttitor at risus id, dapibus convallis tortor. Pellentesque id dui consequat, scelerisque ipsum non, scelerisque justo. Integer magna turpis, sollicitudin sit amet varius in, egestas in metus. Suspendisse faucibus interdum velit, sit amet pretium ante porttitor nec. Suspendisse potenti. ";
			
			
			textArea.append(loremIpsumString);
			textArea.setFont(new Font("Times New Roman", Font.PLAIN, readingFontSize));*/
			
			add(textScrollPane,BorderLayout.WEST);
		}
		
	}
	private class SouthPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		SouthPanel(){
			setLayout(new FlowLayout());
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, defaultBackgroundColor));
			
			JLabel southLabel = new JLabel("<html><p><font size=4>To <i>Decrease</i> or <i>Increase</i> Reading Speed, "+
											"Press <font size=6>&lsaquo<font size=4> or <font size=6>&rsaquo<font size=4>.  "+
											"Press Spacebar to <i>Pause</i>.</p></html>", SwingConstants.CENTER);
			southLabel.setForeground(defaultTextColor);
			southLabel.setMaximumSize(new Dimension(WINDOW_WIDTH-DPIScaling.scaleInt(400),(int)(WINDOW_HEIGHT*.1)));
			add(southLabel);
		}
		
	}
	
	private class WestPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		WestPanel(){
			//West Panel Styling
			setLayout(new BorderLayout());
			setSize((int)(WINDOW_WIDTH*.25),(int)(WINDOW_HEIGHT*.9));
		
			//Setup Menu
			Menu menu = new Menu();
			add(menu, BorderLayout.NORTH);
			
			//Create DocumentListPanel
			docListPanel = new DocumentListPanel(userName +"'s Documents:");
			add(docListPanel, BorderLayout.WEST);
			
			//Create DocumentListStatisticsPanel
			docStatsPanel = new DocumentStatisticsPanel();
			add(docStatsPanel, BorderLayout.SOUTH);
		
		}
		
	}
	private class Menu extends JMenuBar{
		
		private static final long serialVersionUID = 1L;

		Menu() {
			super();
			setSize(WINDOW_WIDTH,(int)(WINDOW_HEIGHT*.1));
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, defaultBackgroundColor));
			
			//Set Default Location/Size Properties
			setBackground(defaultBackgroundColor);
			
			//File Menu
			JMenu fileMenu = new JMenu("File");
			fileMenu.setForeground(Color.WHITE);
			fileMenu.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			
			JMenuItem openNewDocument = new JMenuItem("Open New Document (*.txt)");
			openNewDocument.setForeground(defaultTextColor);
			openNewDocument.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			
			JMenuItem saveCurrentPlace = new JMenuItem("Save Current Place");
			saveCurrentPlace.setForeground(defaultTextColor);
			saveCurrentPlace.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			
			JMenuItem resetDocumentStatistics = new JMenuItem("Reset Document Statistics");
			resetDocumentStatistics.setForeground(defaultTextColor);
			resetDocumentStatistics.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			
			JMenuItem exit = new JMenuItem("Exit");
			exit.setForeground(defaultTextColor);
			exit.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			
			//Setup Listeners
			openNewDocument.addActionListener((ActionEvent event) -> {
				System.out.println("Open New Document (*.txt)");
				OpenNewFile f = new OpenNewFile(); //user input box for new document
			});
			saveCurrentPlace.addActionListener((ActionEvent event) -> {
				System.out.println("Save Current Place");
				//TBD
			});
			resetDocumentStatistics.addActionListener((ActionEvent event) -> {
				System.out.println("Reset Document Statistics");
				//TBD
			});
			exit.addActionListener((ActionEvent event) -> {
				System.exit(0);
			});
		
			fileMenu.add(openNewDocument);
			fileMenu.add(saveCurrentPlace);
			fileMenu.add(resetDocumentStatistics);
			fileMenu.add(exit);
			
			this.add(fileMenu);
		
			//User Menu
			JMenu userMenu = new JMenu("User");
			userMenu.setForeground(Color.WHITE);
			userMenu.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			JMenuItem selectExistingUser = new JMenuItem("Select Existing User Profile");
			selectExistingUser.setForeground(defaultTextColor);
			selectExistingUser.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			JMenuItem createNewUser = new JMenuItem("Create New User");
			createNewUser.setForeground(defaultTextColor);
			createNewUser.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			//User Event Listeners
			selectExistingUser.addActionListener((ActionEvent event) -> {
				System.out.println("Select Existing User Profile");
				//TBD
			});
			createNewUser.addActionListener((ActionEvent event) -> {
				System.out.println("Create New User");
				//TBD
			});
			
			userMenu.add(selectExistingUser);
			userMenu.add(createNewUser);
			this.add(userMenu);
	
			//Options Menu
			JMenu optionsMenu = new JMenu("Options");
			optionsMenu.setForeground(Color.WHITE);
			optionsMenu.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			JMenuItem setSpeedSetting = new JMenuItem("Set Speed Setting");
			setSpeedSetting.setForeground(defaultTextColor);
			setSpeedSetting.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			JMenuItem setDisplayFont = new JMenuItem("Set Display Font");
			setDisplayFont.setForeground(defaultTextColor);
			setDisplayFont.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			JMenuItem setPointerType = new JMenuItem("Set Pointer Type");
			setPointerType.setForeground(defaultTextColor);
			setPointerType.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			//Options Menu Listeners
			setSpeedSetting.addActionListener((ActionEvent event) -> {
				System.out.println("Set Speed Setting");
				//TBD
			});
			setDisplayFont.addActionListener((ActionEvent event) -> {
				System.out.println("Set Display Font");
			});
			setPointerType.addActionListener((ActionEvent event) -> {
				System.out.println("Set Pointer Type");
				//TBD
			});
			
			optionsMenu.add(setSpeedSetting);
			optionsMenu.add(setDisplayFont);
			optionsMenu.add(setPointerType);
			this.add(optionsMenu);
		}
	}
	private class DocumentListPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int width = (int)(WINDOW_WIDTH*.25);   //25% wide, 60% high
		int height = (int)(WINDOW_HEIGHT*.7);
		
		// DEBUG ONLY: Test repaint() functionality.
		private int debugTestIteration = 0;
		public void debugTestRepaint(){
			model.addElement("Test " + debugTestIteration++);
			docList.setModel(model);
		}
		
		DocumentListPanel(String title){
			super();

			setLayout(new BorderLayout());
			model = new DefaultListModel<String>();
			for (String s: documents) {
				model.addElement(s);
			}
			docList = new JList<String>(model);
			
			//Panel Styling Elements
			setSize(width,height);  
			setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, defaultBackgroundColor));
			setBackground(Color.white);			
			setForeground(defaultTextColor);
			
			//Set Label
			JLabel titleBox = new JLabel(title);
			titleBox.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));

			
			
			//Set Styling Elements for titlebox
			titleBox.setSize(width,DPIScaling.scaleInt(50));
			titleBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, defaultBackgroundColor));
			titleBox.setBackground(Color.white);
			titleBox.setForeground(defaultTextColor);
			this.add(titleBox,BorderLayout.NORTH);
			
			//Populate the docList
			String[] docArray = documents.toArray(new String[0]);
			docList = new JList<String>(docArray);
			
			//Set Styling Elements for docList
			docList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			docList.setLayoutOrientation(JList.VERTICAL);
			docList.setVisibleRowCount(-1);		//don't select an entry
			docList.setBackground(Color.white);
			docList.setForeground(defaultTextColor);

			
			//Add scrollbar
			listScroller = new JScrollPane(docList);
			
			//listScroller Styling
			listScroller.setPreferredSize(new Dimension(width, height-DPIScaling.scaleInt(50)));
			listScroller.setMaximumSize(new Dimension(width, height-DPIScaling.scaleInt(50)));
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			
			this.add(listScroller,BorderLayout.WEST);

			
		}
	}
	private class DocumentStatisticsPanel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		int width = (int)(WINDOW_WIDTH*.25);   //20% wide, 60% high
		int height = (int)(WINDOW_HEIGHT*.2);
		
		LocalDateTime currTime;
		int currTrainingTimeMin;
		int longestTrainingTimeMin;
		int fastestWPM;
		
		DocumentStatisticsPanel() {
			
			//Collect Current Statistics
			currTime = LocalDateTime.now();
			currTime =  LocalDateTime.now().plusMinutes(4); 					//DEBUG ONLY
			currTrainingTimeMin = (int)startTime.until(currTime, MINUTES);

			if (fastestWPMToday <= currWPM) fastestWPMToday = currWPM;
			System.out.println("DEBUG ONLY: TBD - Stats collection for Longest Training Time, Fastest WPM (These come from document obj)");
			System.out.println("                                   ^^^^^^^^^^^^^^^^^^^^^^^^^");
			System.out.println("                                   If you said goodbye to me tonight,");
			System.out.println("                                   There would still be music left to write.");
			System.out.println("                                   What else could I do?");
			System.out.println("                                   I'm so inspired by you.");
			System.out.println("                                   That hasn't happened for the longest time.");
			System.out.println("                                 --Billy Joel, \"The Longest Time\"");
			System.out.println("                                   (Sorry.)");
			
			//Panel Styling Elements
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(width, height-DPIScaling.scaleInt(50)));
			setMaximumSize(new Dimension(width, height-DPIScaling.scaleInt(50)));
			setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBackgroundColor));
			setBackground(Color.white);

			
			JLabel title = new JLabel("Document Statistics:");
			title.setForeground(defaultTextColor);
			title.setFont(new Font("Serif", Font.PLAIN, emphasisFontSize));
			title.setHorizontalTextPosition(JLabel.LEFT);
			
			/*String labelContents="<html><p>Today's Training Time:  "+currTrainingTimeMin+"m<br>"+
										"Longest Training Time:  "+longestTrainingTimeMin+"m<br>"+
										"Current WPM:         "+currentWPM+"WPM<br>"+
										"Fastest WPM Today:   "+fastestWPMToday+"WPM<br>"+
										"Fastest WPM:         "+fastestWPM+"WPM</p></html>";
			*/
			
			String labelContents="<html><p>Today's Training Time:  <br>"+
										"Longest Training Time:  <br>"+
										"Current WPM:         <br>"+
										"Fastest WPM Today:   <br>"+
										"Fastest WPM:         <br></p></html>";
			
			JLabel label = new JLabel(labelContents);
			label.setForeground(defaultTextColor);
			label.setHorizontalTextPosition(JLabel.LEFT);
			
			String valueContents="<html><p>"+currTrainingTimeMin+" m<br>"+
											longestTrainingTimeMin+" m<br>"+
											currWPM+" WPM<br>"+
											fastestWPMToday+" WPM<br>"+
											fastestWPM+" WPM</p></html>";
			
			JLabel values = new JLabel(valueContents);
			//Create a composite border in order to get blue edge + margin
			Border border = BorderFactory.createMatteBorder(0, 0, 0, 2, defaultBackgroundColor);
			Border margin = new EmptyBorder(0,0,0,8);
			setBorder(new CompoundBorder(border, margin));
			
			values.setForeground(defaultTextColor);
			values.setHorizontalTextPosition(JLabel.RIGHT);
			
			add(title, BorderLayout.NORTH);
			add(label, BorderLayout.WEST);
			add(values, BorderLayout.EAST);
		}
	}
	
	private class OpenNewFile extends JFrame {
		JLabel enterFileName;
		JTextField fileName;
		JPanel inputFrame;
		JButton submitButton;
		
		public OpenNewFile() {
			super("Open New Document");
			setSize(250, 200);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			enterFileName = new JLabel("New Document(*.txt): ");
			fileName = new JTextField();
			submitButton = new JButton("OK");	
			submitButton.addActionListener(new ButtonListener());
			
			inputFrame.add(enterFileName);
			inputFrame.add(fileName);
			inputFrame.add(submitButton);
			add(inputFrame);
			
			setVisible(true);
		}
		
		private class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				//TODO: add document to user and document list panel
			}
		}
		
		
	}
	
}
