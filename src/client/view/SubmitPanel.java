package client.view;

import client.app.ClientApp;
import client.listeners.SubmissionListener;
import shared.Item;
import shared.SellableItem;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class SubmitPanel extends JPanel {

	private static final long serialVersionUID = -7555887340687619434L;
	private ClientApp client;
	private JTextField txtItemName;
	private JTextField txtItemDescription;
	private JTextField txtItemPrice;
	private JTextField txtItemTime;

	SubmitPanel(ClientApp client) {
		super();
		this.client = client;
		this.txtItemName = new JTextField();
		this.txtItemDescription = new JTextField();
		this.txtItemPrice = new JTextField();
		this.txtItemTime = new JTextField();
		JButton btnItemSubmission = new JButton("Soumettre");

		this.setLayout(new GridBagLayout());

		JLabel labelName = new JLabel("Nom : ");
		JLabel labelDescription = new JLabel("Description : ");
		JLabel labelPrice = new JLabel("Prix de base : ");
		JLabel labelTime = new JLabel("Durée de la vente (minutes) : ");

		labelName.setPreferredSize(new Dimension(250, 40));
		txtItemName.setPreferredSize(new Dimension(300, 40));
		labelDescription.setPreferredSize(new Dimension(250, 150));
		txtItemDescription.setPreferredSize(new Dimension(300, 150));
		labelPrice.setPreferredSize(new Dimension(250, 40));
		txtItemPrice.setPreferredSize(new Dimension(300, 40));
		labelTime.setPreferredSize(new Dimension(250, 40));
		txtItemTime.setPreferredSize(new Dimension(300, 40));

		GridBagConstraints gbSubmission = new GridBagConstraints();

		gbSubmission.gridx = 0;
		gbSubmission.gridy = 0;
		gbSubmission.gridwidth = 1;
		gbSubmission.gridheight = 4;
		gbSubmission.insets = new Insets(5, 5, 5, 50);
		gbSubmission.insets = new Insets(0, 0, 0, 0);

		// Name
		gbSubmission.gridx = 1;
		gbSubmission.gridheight = 1;
		this.add(labelName, gbSubmission);

		gbSubmission.gridx = 2;
		this.add(txtItemName, gbSubmission);

		// Description
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 2;
		gbSubmission.gridwidth = 1;
		gbSubmission.gridheight = 1;
		this.add(labelDescription, gbSubmission);

		gbSubmission.gridx = 2;
		this.add(txtItemDescription, gbSubmission);

		// Price
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 3;
		this.add(labelPrice, gbSubmission);

		gbSubmission.gridx = 2;
		gbSubmission.gridy = 3;
		this.add(txtItemPrice, gbSubmission);

		// Time
		gbSubmission.gridx = 1;
		gbSubmission.gridy = 4;
		this.add(labelTime, gbSubmission);

		gbSubmission.gridx = 2;
		gbSubmission.gridy = 4;
		this.add(txtItemTime, gbSubmission);

	
		// Button for submission
		gbSubmission.gridx = 2;
		gbSubmission.gridy = 6;
		btnItemSubmission.addActionListener(new SubmissionListener(client, this));
		this.add(btnItemSubmission, gbSubmission);
	}

	public Item getFieldsContent() throws NumberFormatException, RemoteException{
		return new SellableItem(txtItemName.getText(), txtItemDescription.getText(), Double.parseDouble(txtItemPrice.getText()), client.getPseudo(), Long.parseLong(txtItemTime.getText()));
	}

	public void clear() {
		txtItemName.setText("");
		txtItemDescription.setText("");
		txtItemPrice.setText("");
		txtItemTime.setText("");
	}

}
