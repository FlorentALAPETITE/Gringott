package client.view;

import client.app.ClientApp;
import client.listeners.ConnectionListener;

import javax.swing.*;

class RegisterPanel extends JPanel {
	
	private static final long serialVersionUID = -4854758538004111313L;
	
	RegisterPanel(ClientApp client) {
		this.add(new JLabel("Pseudo :"));
		JTextField registerField = new JTextField();
		registerField.setColumns(15);
		this.add(registerField);
		JButton registerButton = new JButton("Connexion");
		this.add(registerButton );
		registerButton.addActionListener(new ConnectionListener(client, registerField));
	}
	
}
