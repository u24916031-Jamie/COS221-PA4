import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ReportsPanel extends Component implements ActionListener {
	Panel data;
	Database db = Database.getInstance();
	Panel root;
	int numCols = 2;

	public Panel getRoot() {
		return root;
	}

	public ReportsPanel() {
		root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);

		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.setBackground(Color.RED);

		data = new Panel(new GridLayout(0, numCols, 1, 1));
		data.setBackground(Color.DARK_GRAY);

		updateData();

		scrollPane.add(data, 0);
		root.add(scrollPane, BorderLayout.CENTER);
	}

	private void updateData() {
		data.removeAll();
		data.setBackground(Color.DARK_GRAY);
		data.add(createCell("Genre", true));
		data.add(createCell("Total revenue", true));

		ArrayList<String[]> ret = db.getReport();
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

	public void actionPerformed(ActionEvent e) {

		updateData();
	}
}
