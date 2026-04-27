import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TracksPanel extends Component implements ActionListener {
	Panel data;
	Database db = Database.getInstance();
	Panel root;
	Choice columnChoice;
	TextField filter;
	Label pageNum;
	int page = 0;

	public Panel getRoot() {
		return root;
	}

	public TracksPanel() {
		root = new Panel(new BorderLayout());
		root.setBackground(Color.LIGHT_GRAY);
		Panel employeeFilter = new Panel(new FlowLayout(FlowLayout.LEFT));

		columnChoice = new Choice();
		columnChoice.add("Name");
		columnChoice.add("Album");
		employeeFilter.add(columnChoice);

		filter = new TextField(30);
		employeeFilter.add(filter);

		Button search = new Button("Search");
		search.addActionListener(this);
		employeeFilter.add(search);

		Button nextPage = new Button("Next Page");
		nextPage.addActionListener(this);
		employeeFilter.add(nextPage);

		Button prevPage = new Button("Prev Page");
		prevPage.addActionListener(this);
		employeeFilter.add(prevPage);

		pageNum = new Label("Showing page " + page + " of results");
		employeeFilter.add(pageNum);

		Button addTrack = new Button("Add Track");
		addTrack.addActionListener(this);
		employeeFilter.add(addTrack);

		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.setBackground(Color.RED);

		data = new Panel(new GridLayout(0, 6, 1, 1));
		data.setBackground(Color.DARK_GRAY);

		updateData();

		scrollPane.add(data, 0);
		root.add(employeeFilter, BorderLayout.NORTH);
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

		String colString;
		switch (columnChoice.getSelectedItem()) {
			case ("Name"): {
				colString = "track.name";
				break;
			}
			case ("Album"): {
				colString = "album.title";
				break;
			}
			default: {
				colString = "";
			}
		}
		ArrayList<String[]> ret = db.getTracks(colString, filter.getText(), -1, page);
		for (int i = 0; i < ret.size(); i++) {
			for (int j = 0; j < ret.get(i).length; j++) {
				data.add(createCell(ret.get(i)[j], false));
			}
		}
		int rowCount = (data.getComponentCount() / 6);
		int fixedRowHeight = 30; // Set your desired row height here

		// Force the panel to be tall enough to maintain the row height
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
		switch (e.getActionCommand()) {
			case "Next Page":
				page++;
				break;

			case "Prev Page":
				page--;
				break;
			case "Add Track":

				Frame parentFrame = (Frame) javax.swing.SwingUtilities.getWindowAncestor(root);
				AddTrackPopup popup = new AddTrackPopup(parentFrame, "Add Track");
				popup.setVisible(true);
				break;

			default:
				break;
		}
		pageNum.setText("Showing page " + page + 1 + " of results");
		page = Math.max(page, 0);
		updateData();
	}
}
