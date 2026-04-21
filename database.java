import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {
	private static Database instance;

	public static Connection conn;

	private Database() {

	}

	public static synchronized Database getInstance() {
		if (instance == null) {

			instance = new Database();
			try {
				Dotenv dotenv = Dotenv.load();
				String proto = dotenv.get("CHINOOK_DB_PROTO");
				String host = dotenv.get("CHINOOK_DB_HOST");
				String port = dotenv.get("CHINOOK_DB_PORT");
				String DBname = dotenv.get("CHINOOK_DB_NAME");
				String username = dotenv.get("CHINOOK_DB_USERNAME");
				String password = dotenv.get("CHINOOK_DB_PASSWORD");

				conn = DriverManager
						.getConnection(proto + "://" + host + ":" + port + "/" + DBname + "?user=" + username
								+ "&password=" + password);

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
