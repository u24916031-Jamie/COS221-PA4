import java.awt.*;
import java.awt.event.*;

public class NewCustomerPopup extends Dialog {

	TextField fname = new TextField("", 30);
	TextField lname = new TextField("", 30);
	TextField email = new TextField("", 30);
	TextField phone = new TextField("", 30);
	TextField country = new TextField("", 30);
	Database db = Database.getInstance();

	public NewCustomerPopup(Frame parent) {
		super(parent, "Add Customer", true);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 10, 10, 10);
		setSize(700, 700);
		setLocationRelativeTo(parent);

		add(new Label("Edit first name:"), gbc);
		add(fname, gbc);

		add(new Label("Edit last name:"), gbc);
		add(lname, gbc);

		add(new Label("Edit email:"), gbc);
		add(email, gbc);

		add(new Label("Edit phone:"), gbc);
		add(phone, gbc);

		add(new Label("Edit country:"), gbc);
		add(country, gbc);

		Button addButton = new Button("Add Customer");
		addButton.addActionListener(e -> {
			String[] updateData = { fname.getText(), lname.getText(), email.getText(), phone.getText(),
					country.getText() };
			db.addCustomer(updateData);
			dispose();
		});
		add(addButton, gbc);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

}