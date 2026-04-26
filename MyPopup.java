import java.awt.*;
import java.awt.event.*;

public class MyPopup extends Dialog implements ActionListener {

	public MyPopup(Frame parent, String message) {
		super(parent, "Notification", true);

		setLayout(new FlowLayout());
		setSize(300, 150);
		setLocationRelativeTo(parent);

		add(new Label(message));

		Button closeButton = new Button("OK");
		closeButton.addActionListener(this);

		add(closeButton);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}
}