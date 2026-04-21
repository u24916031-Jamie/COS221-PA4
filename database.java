import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {
	private static Database instance;

	public static Connection conn;

	private Database() {

	}

	public static synchronized Database getInstance() {
		if (instance == null) {

			instance = new Database();
			try {
				conn = DriverManager
						.getConnection("jdbc:mariadb://localhost:3306/u24916031_chinook?user=root&password=root");

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM employee");
				while (rs.next()) {
					int id = rs.getInt("EmployeeId");
					String name = rs.getString("FirstName");
					System.out.println(id + " | " + name);
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}

		return instance;
	}

	public String[] getEmployees(String filter) {
		String[] ret = new String[5];

		return ret;
	}
}
