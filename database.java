import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;
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

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}

		return instance;
	}

	public ArrayList<String[]> getEmployees(String column, String filter) {
		try {
			System.out.println(column + " " + filter);
			ArrayList<String[]> arr = new ArrayList<>();
			String sql = "SELECT e1.firstname AS firstname,e1.lastname AS lastname, e1.title AS title, e1.city AS city, e1.country AS country, e1.phone AS phone, e2.firstname AS supervisor FROM employee AS e1 LEFT JOIN employee AS e2 on e1.reportsto = e2.employeeid";

			if (!filter.trim().equals("")) {
				sql += " WHERE e1." + column + " LIKE ?";

			}
			System.out.println(sql);
			PreparedStatement stmt = conn.prepareStatement(sql);
			if (!filter.trim().equals("")) {
				stmt.setString(1, "%" + filter.trim() + "%");

			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[8];
				temp[0] = rs.getString("firstname");
				temp[1] = rs.getString("lastname");
				temp[2] = rs.getString("title");
				temp[3] = rs.getString("city");
				temp[4] = rs.getString("country");
				temp[5] = rs.getString("phone");
				temp[6] = rs.getString("supervisor");
				temp[7] = "True";

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	public ArrayList<String[]> getTracks(String column, String filter, int id, int page) {
		try {
			ArrayList<String[]> arr = new ArrayList<>();
			String sql = "SELECT track.name AS name, album.title AS album, mt.name AS mediatype, genre.name AS genre, track.composer AS composer";
			sql += ", CONCAT(FLOOR(track.milliseconds / 60000),':',LPAD(FLOOR((track.milliseconds % 60000) / 1000), 2, '0'))";
			sql += " AS minutes_seconds FROM track JOIN album on track.albumid = album.albumid JOIN genre ON track.genreid = genre.genreid JOIN mediatype AS mt ON track.mediatypeid = mt.mediatypeid";
			if (id != -1) {
				sql += " WHERE track.trackid = ?";
			} else {

				if (!filter.equals("")) {
					sql += " WHERE " + column + " LIKE ?";

				}
			}
			// System.out.println(sql);
			sql += " ORDER BY track.trackid LIMIT 100 OFFSET " + page * 100;
			PreparedStatement stmt = conn.prepareStatement(sql);
			if (id != -1) {
				stmt.setInt(1, id);
			} else {

				if (!filter.equals("")) {
					stmt.setString(1, "%" + filter + "%");

				}
			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[6];
				temp[0] = rs.getString("name");
				temp[1] = rs.getString("album");
				temp[2] = rs.getString("mediatype");
				temp[3] = rs.getString("genre");
				temp[4] = rs.getString("composer");
				temp[5] = rs.getString("minutes_seconds");

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	// TrackId, Name, AlbumId, MediaType, GenreId, Composer, Milliseconds, Bytes,
	// UnitPrice
	public boolean addTrack(String[] data) {
		try {
			String sql = "INSERT INTO track (TrackId, Name, AlbumId, MediaType, GenreId, Composer, Milliseconds, Bytes, UnitPrice)";
			sql += " VALUES ((SELECT TrackId FROM track ORDER BY TrackId DESC LIMIT 1) + 1, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(0, data[0]);
			stmt.setInt(1, Integer.parseInt(data[1]));
			stmt.setInt(2, Integer.parseInt(data[2]));
			stmt.setInt(3, Integer.parseInt(data[3]));
			stmt.setString(4, data[4]);
			stmt.setInt(5, Integer.parseInt(data[5]));
			stmt.setInt(6, Integer.parseInt(data[6]));
			stmt.setDouble(7, Double.parseDouble(data[7]));

			return (stmt.executeUpdate() > 0);
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return false;
	}

	public ArrayList<String[]> getReport() {
		try {
			ArrayList<String[]> arr = new ArrayList<>();
			String sql = "SELECT genre.title AS genre, SUM(il.unitprice) AS total";
			sql += " FROM genre NATURAL JOIN track NATURAL JOIN invoiceline AS il GROUP BY genre.genreId ORDER BY total DESC";

			PreparedStatement stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[7];
				temp[0] = rs.getString("genre");
				temp[1] = rs.getString("total");

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	// TODO
	public ArrayList<String[]> getCustomers(String column, String filter, boolean inactive) {
		try {
			ArrayList<String[]> arr = new ArrayList<>();

			String sql = "SELECT cus.customerId, cus.firstname, cus.lastname, cus.email, cus.phone, cus.country FROM ";
			if (inactive == false) {
				sql += "customer AS cus";
			} else {

				sql += "( SELECT c.customerId, firstname, lastname, email, phone, country FROM customer AS c LEFT JOIN invoice AS i ON c.customerid = i.customerid GROUP BY c.customerid HAVING MAX(i.invoicedate) IS NULL OR MAX(i.invoicedate) < DATE_SUB(NOW(), INTERVAL 2 YEAR) ) AS cus";
			}

			if (!filter.equals("")) {
				sql += " WHERE " + column + " LIKE %?%";

			}
			// System.out.println(sql);
			PreparedStatement stmt = conn.prepareStatement(sql);
			if (filter.compareTo("") != 0) {
				stmt.setString(0, filter);

			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[6];
				temp[0] = rs.getString("customerId");
				temp[1] = rs.getString("firstname");
				temp[2] = rs.getString("lastname");
				temp[3] = rs.getString("email");
				temp[4] = rs.getString("phone");
				temp[5] = rs.getString("country");

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	public boolean updateCustomer(String[] data) {
		try {

			String sql = "UPDATE SET firstname = ?, lastname = ?, email = ?, phone = ?, country = ? FROM customer WHERE customerId = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setString(0, data[1]);
			stmt.setString(1, data[2]);
			stmt.setString(2, data[3]);
			stmt.setString(3, data[4]);
			stmt.setString(4, data[5]);
			stmt.setInt(5, Integer.parseInt(data[0]));

			return (stmt.executeUpdate() > 0);

		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return false;
	}

	public boolean addCustomer(String[] data) {
		try {

			String sql = "INSERT INTO customer (customerId, firstname, lastname, email, phone, country) ";
			sql += "VALUES ((SELECT customerId FROM customer ORDER BY customerId DESC LIMIT 1), ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setString(0, data[0]);
			stmt.setString(1, data[1]);
			stmt.setString(2, data[2]);
			stmt.setString(3, data[3]);
			stmt.setString(4, data[4]);

			return (stmt.executeUpdate() > 0);

		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return false;
	}

	public boolean removeCustomer(int customerId) {
		try {

			String sql = "DELETE FROM customer WHERE customerId = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setInt(0, customerId);

			return (stmt.executeUpdate() > 0);

		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return false;
	}

	public ArrayList<String[]> getSpendingSummary(int customerId) {
		try {
			ArrayList<String[]> arr = new ArrayList<>();
			// total spent, num purcahses, date of most recent purchase
			String sql = "SELECT SUM(unitprice) AS totalspent, COUNT(DISTINCT invoiceid) as numpurchases, MAX(invoicedate) AS mostrecentpurchase FROM customer NATURAL JOIN invoice NATURAL JOIN invoiceline WHERE customerid = ? ORDER BY customerid";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, customerId);
			stmt.setInt(2, customerId);
			stmt.setInt(3, customerId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[3];
				temp[0] = rs.getString("totalspent");
				temp[1] = rs.getString("numpurchases");
				temp[2] = rs.getString("mostrecentpurchase");

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	public ArrayList<String[]> getFavGenres(int customerId) {
		try {
			ArrayList<String[]> arr = new ArrayList<>();
			// total spent, num purcahses, date of most recent purchase
			String sql = "SELECT g.genreid AS genreid, g.name AS genrename, COUNT(g.genreid) AS genrecount FROM customer AS c NATURAL JOIN invoice AS i NATURAL JOIN invoiceline AS il NATURAL JOIN track AS t JOIN genre AS g ON t.GenreId = g.genreid WHERE customerid = ? GROUP BY g.genreid ORDER BY COUNT(g.genreid) DESC";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, customerId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String[] temp = new String[3];
				temp[0] = rs.getString("genreid");
				temp[1] = rs.getString("genrename");
				temp[2] = rs.getString("genrecount");

				arr.add(temp);
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

	public ArrayList<String[]> getRecommendations(int customerId) {
		try {
			ArrayList<String[]> arr = new ArrayList<>();
			// total spent, num purcahses, date of most recent purchase

			String mostFreqGenres = "WITH freqgenres AS (SELECT g.genreid AS genreid, COUNT(g.genreid) AS genrecount FROM customer AS c JOIN invoice AS i ON c.customerid = i.customerid JOIN invoiceline AS il ON i.invoiceid = il.invoiceid JOIN track AS t ON il.trackid = t.trackid JOIN genre AS g ON t.GenreId = g.genreid WHERE c.customerid = ? GROUP BY g.genreid ORDER BY COUNT(g.genreid) DESC)";
			String boughtTracks = "boughttracks AS (SELECT t.trackid AS boughtid, t.albumid AS boughtalbum FROM track AS t JOIN invoiceline AS il ON t.trackid = il.trackid JOIN invoice AS i ON il.invoiceid = i.invoiceid JOIN customer AS c ON c.customerid = i.customerid WHERE c.customerid = ?)";
			String sameGenreRec = "(SELECT t.trackid AS rectrack FROM track AS t JOIN freqgenres ON freqgenres.genreid = t.genreid WHERE t.trackid NOT IN (SELECT boughtid FROM boughttracks) LIMIT 10)";

			String sameAlbumTracks = "(SELECT t.trackid AS rectrack FROM album AS a JOIN track AS t ON a.albumid = t.albumid LEFT JOIN boughttracks AS bt on t.trackid = bt.boughtid WHERE t.trackid NOT IN (SELECT boughtid FROM boughttracks) AND a.albumid = bt.boughtalbum LIMIT 10)";

			String sql = mostFreqGenres + ", " + boughtTracks + " " + sameGenreRec + " UNION " + sameAlbumTracks;
			// System.out.println(sql);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, customerId);
			stmt.setInt(2, customerId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				int temp = rs.getInt("rectrack");

				arr.add(getTracks("", "", temp, 0).get(0));
			}

			return arr;
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return null;
	}

}
