import java.awt.*;
import java.awt.event.*;

public class AWTTabs extends Frame implements ActionListener {
	CardLayout cardLayout;
	Panel contentPanel;

	public AWTTabs() {
		setTitle("AWT Custom Tabs");
		setSize(800, 300);
		setLayout(new BorderLayout());

		Panel tabMenu = new Panel(new FlowLayout(FlowLayout.LEFT));
		Button tab1 = new Button("Employees");
		Button tab2 = new Button("Tracks");
		Button tab3 = new Button("Report");
		Button tab4 = new Button("Notifications");
		Button tab5 = new Button("Customer Recommendations");

		tab1.addActionListener(this);
		tab2.addActionListener(this);
		tab3.addActionListener(this);
		tab4.addActionListener(this);
		tab5.addActionListener(this);

		tabMenu.add(tab1);
		tabMenu.add(tab2);
		tabMenu.add(tab3);
		tabMenu.add(tab4);
		tabMenu.add(tab5);

		cardLayout = new CardLayout();
		contentPanel = new Panel(cardLayout);

		contentPanel.add(employeesPanel(), "Employees");
		contentPanel.add(tracksPanel(), "Tracks");
		contentPanel.add(reportPanel(), "Report");
		contentPanel.add(notificationsPanel(), "Notifications");
		contentPanel.add(customerRecommendationsPanel(), "Customer Recommendations");

		add(tabMenu, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setVisible(true);
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

	private Panel employeesPanel() {
		Panel root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);
		Panel employeeFilter = new Panel(new FlowLayout(FlowLayout.LEFT));

		TextField filter = new TextField(30);
		Button search = new Button("Search");
		employeeFilter.add(filter);
		employeeFilter.add(search);
		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.setBackground(Color.RED);

		Panel employeeData = new Panel(new GridLayout(2, 8, 1, 1));
		employeeData.setBackground(Color.RED);

		employeeData.setPreferredSize(new Dimension(100 * 8, 50 * 2));
		employeeData.setBackground(Color.DARK_GRAY);
		employeeData.add(createCell("First Name", true));
		employeeData.add(createCell("Last Name", true));
		employeeData.add(createCell("Title", true));
		employeeData.add(createCell("City", true));
		employeeData.add(createCell("Country", true));
		employeeData.add(createCell("Phone", true));
		employeeData.add(createCell("Reports-to", true));
		employeeData.add(createCell("Active", true));

		// --- Row 1 ---
		employeeData.add(createCell("101", false));
		employeeData.add(createCell("Alice", false));
		employeeData.add(createCell("Admin", false));

		// --- Row 2 ---
		employeeData.add(createCell("102", false));
		employeeData.add(createCell("Bob", false));
		employeeData.add(createCell("Editor", false));
		employeeData.add(createCell("Bob", false));
		employeeData.add(createCell("Editor", false));

		scrollPane.add(employeeData);
		root.add(employeeFilter, BorderLayout.NORTH);
		root.add(scrollPane, BorderLayout.CENTER);

		return root;
	}

	private Panel tracksPanel() {

		Panel root = new Panel();
		root.setBackground(Color.GRAY);
		root.add(new Label("Tracks"));

		return root;
	}

	private Panel reportPanel() {

		Panel root = new Panel();
		root.setBackground(Color.GRAY);
		root.add(new Label("Report"));

		return root;
	}

	private Panel notificationsPanel() {
		Panel root = new Panel();
		root.setBackground(Color.GRAY);
		root.add(new Label("Notifications"));

		return root;
	}

	private Panel customerRecommendationsPanel() {
		Panel root = new Panel();
		root.setBackground(Color.GRAY);
		root.add(new Label("Customer Recommendations"));

		return root;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println(cmd);
		cardLayout.show(contentPanel, cmd);
	}

	public static void main(String[] args) {
		new AWTTabs();
	}
}