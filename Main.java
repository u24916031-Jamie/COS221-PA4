import java.util.ArrayList;

public class Main {
	private static void print(ArrayList<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).length; j++) {
				System.out.print(list.get(i)[j] + " ");
			}
			System.out.print("\n");
		}
	}

	public static void main(String[] args) {
		Database db = Database.getInstance();
		print(db.getCustomers("", "", true));
		new AWTTabs();

	}
}
