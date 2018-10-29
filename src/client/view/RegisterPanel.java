package client.view;

import client.app.ClientApp;
import client.listeners.ConnectionListener;

import javax.swing.*;

public class RegisterPanel extends JPanel {
	
	private static final long serialVersionUID = -4854758538004111313L;
	private ClientApp client;
	private JTextField registerField;
	private JButton registerButton;
	
	public RegisterPanel(ClientApp client) {
		this.client = client;
		this.add(new JLabel("Pseudo :"));
		this.registerField = new JTextField();
		this.registerField.setColumns(15);
		this.add(registerField);
		this.registerButton = new JButton("Connexion");
		this.add(registerButton );
		this.registerButton.addActionListener(new ConnectionListener(client));
	}
	
	public String getFieldContent(){
		return this.registerField.getText();
	}
	
}
