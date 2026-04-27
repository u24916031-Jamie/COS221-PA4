import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditCustomerPopup extends Dialog {

	TextField fname = new TextField("", 30);
	TextField lname = new TextField("", 30);
	TextField email = new TextField("", 30);
	TextField phone = new TextField("", 30);
	TextField country = new TextField("", 30);
	Database db = Database.getInstance();
	int cusId = -1;

	public EditCustomerPopup(Frame parent, int customerId) {
		super(parent, "Edit Customer", true);

		ArrayList<String[]> data = db.getCustomers("", "", false, customerId);
		cusId = Integer.parseInt(data.get(0)[0]);
		fname.setText(data.get(0)[1]);
		lname.setText(data.get(0)[2]);
		email.setText(data.get(0)[3]);
		phone.setText(data.get(0)[4]);
		country.setText(data.get(0)[5]);

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

		Button changeButton = new Button("Apply Changes");
		changeButton.addActionListener(e -> {
			String[] updateData = { fname.getText(), lname.getText(), email.getText(), phone.getText(),
					country.getText(),
					String.valueOf(cusId) };
			db.updateCustomer(updateData);
			dispose();
		});
		add(changeButton, gbc);

		Button deleteButton = new Button("Delete Customer");
		deleteButton.addActionListener(e -> {
			db.removeCustomer(cusId);
			dispose();
		});
		add(deleteButton, gbc);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

}