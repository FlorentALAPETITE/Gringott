package client.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
	
	private static final long serialVersionUID = -4854758538004111313L;
	private ActionListener controller;
	private JTextField registerField;
	private JButton registerButton;
	
	public RegisterPanel(ActionListener controller) {
		this.controller = controller;
		this.add(new JLabel("Pseudo :"));
		this.registerField = new JTextField();
		this.registerField.setColumns(15);
		this.add(registerField);
		this.registerButton = new JButton("Connexion");
		this.add(registerButton );
		this.registerButton.addActionListener(this.controller);
	}
	
	public String getFieldContent(){
		return this.registerField.getText();
	}
	
}
