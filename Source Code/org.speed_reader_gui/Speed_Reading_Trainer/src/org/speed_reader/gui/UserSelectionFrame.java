package org.speed_reader.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserSelectionFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public final ActionType action;
	
	// TEMPORARY testing method
	public static void main(String[] args){
		UserSelectionFrame login = new UserSelectionFrame();
	}
	
	public UserSelectionFrame(){
		this(ActionType.CREATE_NEW);
	}
	
	public UserSelectionFrame(ActionType action){
		super();
		this.action = action;
		setTitle(this.action.titleText);
		UsernamePanel usernamePanel = new UsernamePanel(action);
		PasswordPanel passwordPanel = new PasswordPanel(action);
		ButtonPanel buttonPanel = new ButtonPanel(action);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(usernamePanel);
		add(passwordPanel);
		add(buttonPanel);
		setSize(500, 250);
		setVisible(true);
	}
	
	public enum ActionType {
		CREATE_NEW("New User", "Register"),
		SIGN_IN("Sign In", "Log In");
		public final String titleText;
		public final String buttonText;
		ActionType(String titleText, String buttonText){
			this.titleText = titleText;
			this.buttonText = buttonText;
		}
	}
	
	private class UsernamePanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public UsernamePanel(ActionType action){
			super();
			JLabel			usernameLabel = new JLabel("Username");
			JTextField		usernameField = new JTextField(20);
			add(usernameLabel);
			add(usernameField);
		}
	}
	
	private class PasswordPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public PasswordPanel(ActionType action){
			super();
			JLabel			passwordLabel = new JLabel("Password");
			JPasswordField	passwordField = new JPasswordField(20);
			add(passwordLabel);
			add(passwordField);
		}
	}
	
	private class ButtonPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public ButtonPanel(ActionType action){
			super();
			JButton submitButton = new JButton(action.buttonText);
			JButton cancelButton = new JButton("Cancel");
		}
	}
	
}
