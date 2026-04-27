import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NotificationsPanel extends Component implements ActionListener {
	Panel data;
	Database db = Database.getInstance();
	Panel root;
	Choice columnChoice;
	TextField filter;
	Checkbox inactive;
	int numCols = 6;
	ArrayList<String> customerIds = new ArrayList<>();

	public Panel getRoot() {
		return root;
	}

	public NotificationsPanel() {
		root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);
		Panel filterBar = new Panel(new FlowLayout(FlowLayout.LEFT));

		columnChoice = new Choice();
		columnChoice.add("First Name");
		columnChoice.add("Last Name");
		columnChoice.add("Email");
		columnChoice.add("Phone");
		columnChoice.add("Country");

		filter = new TextField(30);

		Button search = new Button("Search");
		search.addActionListener(this);

		inactive = new Checkbox("Inactive", false);
		inactive.addItemListener(e -> {
			updateData();
		});

		filterBar.add(columnChoice);
		filterBar.add(filter);
		filterBar.add(search);
		filterBar.add(inactive);

		Button addCustomer = new Button("Add Customer");
		addCustomer.addActionListener(e -> {
			Frame parentFrame = (Frame) javax.swing.SwingUtilities.getWindowAncestor(root);
			NewCustomerPopup popup = new NewCustomerPopup(parentFrame);
			popup.setVisible(true);
			updateData();
		});
		filterBar.add(addCustomer);

		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);

		data = new Panel(new GridLayout(0, numCols, 1, 1));

		updateData();

		scrollPane.add(data, 0);
		root.add(filterBar, BorderLayout.NORTH);
		root.add(scrollPane, BorderLayout.CENTER);
	}

	private void updateData() {
		data.removeAll();
		data.setBackground(Color.DARK_GRAY);
		data.add(createCell(" ", true));
		data.add(createCell("First Name", true));
		data.add(createCell("Last Name", true));
		data.add(createCell("Email", true));
		data.add(createCell("Phone", true));
		data.add(createCell("Country", true));

		String colString;
		switch (columnChoice.getSelectedItem()) {
			case ("First Name"): {
				colString = "cus.firstname";
				break;
			}
			case ("Last Name"): {
				colString = "cus..lastname";
				break;
			}
			case ("Email"): {
				colString = "cus.email";
				break;
			}
			case ("Phone"): {
				colString = "cus.phone";
				break;
			}
			case ("Country"): {
				colString = "cus.country";
				break;
			}
			default: {
				colString = "";
			}
		}

		ArrayList<String[]> ret = db.getCustomers(colString, filter.getText(), inactive.getState(), -1);
		for (int i = 0; i < ret.size(); i++) {
			for (int j = 0; j < ret.get(i).length; j++) {
				if (j == 0) {
					data.add(createEditButton(ret.get(i)[j]));
					continue;
				}
				data.add(createCell(ret.get(i)[j], false));
			}
		}

		int rowCount = (data.getComponentCount() / numCols);
		int fixedRowHeight = 30;

		data.setPreferredSize(new Dimension(1280, rowCount * fixedRowHeight));

		data.revalidate();
		data.repaint();
	}

	private Label createCell(String text, boolean isHeader) {
		Label label = new Label(text, Label.CENTER);
		if (isHeader) {
			label.setFont(new Font("Arial", Font.BOLD, 12));
		}
		label.setBackground(isHeader ? Color.GRAY : Color.LIGHT_GRAY);

		return label;
	}

	private Button createEditButton(String customerId) {
		Button editBtn = new Button("Edit");
		editBtn.addActionListener(e -> {
			Frame parentFrame = (Frame) javax.swing.SwingUtilities.getWindowAncestor(root);
			EditCustomerPopup popup = new EditCustomerPopup(parentFrame, Integer.parseInt(customerId));
			popup.setVisible(true);
			updateData();
		});

		return editBtn;
	}

	public void actionPerformed(ActionEvent e) {

		updateData();
	}
}
