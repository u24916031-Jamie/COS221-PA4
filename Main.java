import java.util.ArrayList;

public class Main {
	private static void printarr(ArrayList<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).length; j++) {

				System.out.print(list.get(i)[j] + " ");
			}
			System.out.print("\n");
		}
	}

	private static void print(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {

			System.out.println(list.get(i));

		}
	}

	public static void main(String[] args) {
		Database db = Database.getInstance();
		// print(db.getCustomers("", "", true));
		// printarr(db.getSpendingSummary(1));
		// printarr(db.getSpendingSummary(2));
		// printarr(db.getRecommendations(2));
		new AWTTabs();

	}
}
