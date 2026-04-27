import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddTrackPopup extends Dialog implements ActionListener {

	TextField title = new TextField("", 30);
	TextField composer = new TextField("", 30);
	TextField milliseconds = new TextField("", 30);
	TextField bytes = new TextField("", 30);
	TextField unitprice = new TextField("", 30);
	Label errorLabel;
	Database db = Database.getInstance();
	ArrayList<String> genres;
	Choice selectedGenre;
	ArrayList<String> albums;
	Choice selectedAlbum;
	ArrayList<String> mediaTypes;
	Choice selectedMediaType;

	public AddTrackPopup(Frame parent, String message) {
		super(parent, message, true);

		genres = db.getGenres();
		albums = db.getAlbums();
		mediaTypes = db.getMediaTypes();

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 10, 10, 10);
		setSize(700, 700);
		setLocationRelativeTo(parent);

		add(new Label("Enter track name:"), gbc);
		add(title, gbc);
		selectedAlbum = new Choice();
		for (String album : albums) {
			selectedAlbum.add(album);
		}
		add(new Label("Select album:"), gbc);
		add(selectedAlbum, gbc);

		selectedGenre = new Choice();
		for (String genre : genres) {
			selectedGenre.add(genre);
		}
		add(new Label("Select genre:"), gbc);
		add(selectedGenre, gbc);

		selectedMediaType = new Choice();
		for (String mediaType : mediaTypes) {
			selectedMediaType.add(mediaType);
		}
		add(new Label("Select media type:"), gbc);
		add(selectedMediaType, gbc);

		add(new Label("Enter composer name:"), gbc);
		add(composer, gbc);

		add(new Label("Enter length in milliseconds:"), gbc);
		add(milliseconds, gbc);

		add(new Label("Enter size in bytes:"), gbc);
		add(bytes, gbc);

		add(new Label("Enter unit price:"), gbc);
		add(unitprice, gbc);

		Button closeButton = new Button("OK");
		closeButton.addActionListener(this);

		add(closeButton, gbc);

		errorLabel = new Label("Unit price must be a Decimal Number!");
		errorLabel.setForeground(Color.RED);
		errorLabel.setVisible(false);
		add(errorLabel, gbc);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	public boolean isValidDouble(String str) {
		try {
			Double.parseDouble(str);

			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isValidInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (!isValidInt(milliseconds.getText())) {
			errorLabel.setText("Milliseconds must be an integer!");
			errorLabel.setVisible(true);
			this.validate();
			return;
		}
		if (!isValidInt(bytes.getText())) {
			errorLabel.setText("Size in bytes must be an integer!");
			errorLabel.setVisible(true);
			this.validate();
			return;
		}
		if (!isValidDouble(unitprice.getText())) {
			errorLabel.setText("Unit price must be a Decimal Number!");
			errorLabel.setVisible(true);
			this.validate();
			return;
		}
		addTrackToDB();
		this.setVisible(false);
		this.dispose();
	}

	public void addTrackToDB() {
		String[] data = new String[7];
		data[0] = title.getText();
		data[1] = String.valueOf(albums.indexOf(selectedAlbum.getSelectedItem()) + 1);
		data[2] = String.valueOf(mediaTypes.indexOf(selectedMediaType.getSelectedItem()) + 1);
		data[3] = String.valueOf(genres.indexOf(selectedGenre.getSelectedItem()) + 1);
		data[4] = composer.getText();
		data[5] = milliseconds.getText();
		data[6] = unitprice.getText();

		db.addTrack(data);
	}
}