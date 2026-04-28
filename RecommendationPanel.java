import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RecommendationPanel extends Component implements ItemListener {
	Panel data;
	Database db = Database.getInstance();
	Panel root;
	Choice customerChoice;
	Label totalSpent = new Label("Total spent: ");
	Label totalPurchases = new Label("Total purchases: ");
	Label lastPurchase = new Label("Last purchase: ");
	Label favGenre = new Label("Favourite genre: ");
	int numCols = 6;
	ArrayList<String> customers = new ArrayList<>();

	public Panel getRoot() {
		return root;
	}

	public RecommendationPanel() {
		root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);
		Panel filterBar = new Panel(new FlowLayout(FlowLayout.LEFT));

		customerChoice = new Choice();
		customerChoice.addItemListener(this);
		updateCustomers();
		filterBar.add(customerChoice);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, 0, 30);

		filterBar.add(totalSpent, gbc);
		filterBar.add(totalPurchases, gbc);
		filterBar.add(lastPurchase, gbc);
		filterBar.add(favGenre, gbc);

		updateCustomerData();

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
		data.add(createCell("Name", true));
		data.add(createCell("Album", true));
		data.add(createCell("Media Type", true));
		data.add(createCell("Genre", true));
		data.add(createCell("Composer", true));
		data.add(createCell("Length", true));
		int customerId = customers.indexOf(customerChoice.getSelectedItem()) + 1;
		ArrayList<String[]> ret = db.getRecommendations(customerId);
		for (int i = 0; i < ret.size(); i++) {
			for (int j = 0; j < ret.get(i).length; j++) {
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

	public void itemStateChanged(ItemEvent e) {
		updateCustomerData();
		updateData();
	}

	public void updateCustomerData() {
		int customerId = customers.indexOf(customerChoice.getSelectedItem()) + 1;
		String[] data = db.getRecommendationData(customerId);
		ArrayList<String[]> favGenres = db.getFavGenres(customerId);
		String favGenreName;
		if (favGenres.size() == 0) {
			favGenreName = "None";
		} else {

			favGenreName = favGenres.get(0)[1];
		}
		totalSpent.setText("Total spent: " + data[0]);
		totalPurchases.setText("Total purchases: " + data[1]);
		lastPurchase.setText("Last purchase: " + data[2]);
		favGenre.setText("Favourite genre: " + favGenreName);
		lastPurchase.getParent().revalidate();
		lastPurchase.getParent().repaint();
	}

	public void updateCustomers() {
		ArrayList<String[]> temp = db.getCustomers("", "", false, -1);
		customers.clear();
		customerChoice.removeAll();
		for (int i = 0; i < temp.size(); i++) {
			customers.add(temp.get(i)[1] + " " + temp.get(i)[2]);
			customerChoice.add(temp.get(i)[1] + " " + temp.get(i)[2]);
		}
		customerChoice.revalidate();
		customerChoice.repaint();
	}
}
