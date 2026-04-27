import java.awt.*;
import java.awt.event.*;

public class AWTTabs extends Frame implements ItemListener, ActionListener {
	CardLayout cardLayout;
	Panel contentPanel;

	EmployeesPanel ep = new EmployeesPanel();
	TracksPanel tp = new TracksPanel();
	ReportsPanel rp = new ReportsPanel();
	NotificationsPanel np = new NotificationsPanel();

	RecommendationPanel recp = new RecommendationPanel();

	public AWTTabs() {
		setTitle("COS 221 PA4 u24916031");
		setSize(1280, 720);
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

		contentPanel.add(ep.getRoot(), "Employees", 0);
		contentPanel.add(tp.getRoot(), "Tracks", 1);
		contentPanel.add(rp.getRoot(), "Report", 2);
		contentPanel.add(np.getRoot(), "Notifications", 3);
		contentPanel.add(recp.getRoot(), "Customer Recommendations", 4);

		add(tabMenu, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println(cmd);
		cardLayout.show(contentPanel, cmd);
	}

	public void itemStateChanged(ItemEvent e) {

		String cmd = e.getItem().toString();
		System.out.println(cmd);
	}
}