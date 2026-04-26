import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EmployeesPanel extends Component implements ActionListener {
	Panel data;
	Database db = Database.getInstance();
	Panel root;
	Choice columnChoice;
	TextField filter;

	public Panel getRoot() {
		return root;
	}

	public EmployeesPanel() {
		root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);
		Panel employeeFilter = new Panel(new FlowLayout(FlowLayout.LEFT));

		columnChoice = new Choice();
		columnChoice.add("firstname");
		columnChoice.add("country");

		filter = new TextField(30);

		Button search = new Button("Search");
		search.addActionListener(this);

		employeeFilter.add(columnChoice);
		employeeFilter.add(filter);
		employeeFilter.add(search);
		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.setBackground(Color.RED);

		data = new Panel(new GridLayout(0, 8, 1, 1));
		data.setPreferredSize(new Dimension(100 * 8, 50 * 2));
		data.setBackground(Color.DARK_GRAY);

		updateData();

		scrollPane.add(data, 0);
		root.add(employeeFilter, BorderLayout.NORTH);
		root.add(scrollPane, BorderLayout.CENTER);
	}

	private void updateData() {
		data.removeAll();
		data.setPreferredSize(new Dimension(100 * 8, 50 * 2));
		data.setBackground(Color.DARK_GRAY);
		data.add(createCell("First Name", true));
		data.add(createCell("Last Name", true));
		data.add(createCell("Title", true));
		data.add(createCell("City", true));
		data.add(createCell("Country", true));
		data.add(createCell("Phone", true));
		data.add(createCell("Reports-to", true));
		data.add(createCell("Active", true));

		ArrayList<String[]> ret = db.getEmployees(columnChoice.getSelectedItem(), filter.getText());
		System.out.println("in update data");
		for (int i = 0; i < ret.size(); i++) {
			for (int j = 0; j < ret.get(i).length; j++) {
				System.out.print(ret.get(i)[j] + " ");
				data.add(createCell(ret.get(i)[j], false));
			}
			System.out.println("");
		}
		data.revalidate();
		data.repaint();
	}

	private Panel createCell(String text, boolean isHeader) {
		Panel p = new Panel(new FlowLayout(FlowLayout.CENTER));
		p.setBackground(isHeader ? Color.GRAY : Color.LIGHT_GRAY);
		p.setSize(new Dimension(50, 20));
		Label label = new Label(text);
		if (isHeader) {
			label.setFont(new Font("Arial", Font.BOLD, 12));
		}

		p.add(label);
		return p;
	}

	public void actionPerformed(ActionEvent e) {

		updateData();
	}
}
