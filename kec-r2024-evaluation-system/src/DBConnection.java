import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * JDBC Connection Management
 * Supports Dual Modes:
 * 1. Embedded H2 Database (File-based, zero-configuration out-of-the-box)
 * 2. MySQL Server (Configured via db.properties)
 * Automatically initializes kec_schema.sql on first startup if tables are absent.
 */
public class DBConnection {

    private static Connection connection = null;
    private static String activeDbType = "h2";
    private static Properties config = new Properties();

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        File configFile = new File("db.properties");
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                config.load(input);
            } catch (Exception e) {
                System.err.println("[WARN] Could not load db.properties, using embedded defaults.");
            }
        }
    }

    /**
     * Obtains a valid, open JDBC Connection.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        String preferredType = config.getProperty("db.type", "h2").trim().toLowerCase();

        if ("mysql".equals(preferredType)) {
            try {
                connection = createMySQLConnection();
                activeDbType = "mysql";
                return connection;
            } catch (Exception e) {
                System.out.println("\n[!] MySQL Connection failed (" + e.getMessage() + ").");
                System.out.println("[*] Gracefully falling back to Embedded Zero-Config Database (H2)...");
            }
        }

        // Default or Fallback: Embedded H2
        connection = createH2Connection();
        activeDbType = "h2";
        return connection;
    }

    private static Connection createMySQLConnection() throws Exception {
        String driver = config.getProperty("mysql.driver", "com.mysql.cj.jdbc.Driver");
        String url = config.getProperty("mysql.url", "jdbc:mysql://localhost:3306/kec_eval_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata");
        String user = config.getProperty("mysql.user", "root");
        String pass = config.getProperty("mysql.password", "root");

        Class.forName(driver);
        return DriverManager.getConnection(url, user, pass);
    }

    private static Connection createH2Connection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Embedded H2 Driver not found in classpath. Ensure lib/h2-*.jar is included.", e);
        }

        String url = config.getProperty("h2.url", "jdbc:h2:./kec_eval_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
        String user = config.getProperty("h2.user", "sa");
        String pass = config.getProperty("h2.password", "");

        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * Initializes the database schema using kec_schema.sql if tables do not exist.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            boolean tablesExist = false;

            try (ResultSet rs = meta.getTables(null, null, "STUDENTS", null)) {
                if (rs.next()) tablesExist = true;
            }
            if (!tablesExist) {
                try (ResultSet rs = meta.getTables(null, null, "students", null)) {
                    if (rs.next()) tablesExist = true;
                }
            }

            if (!tablesExist) {
                System.out.println("[*] Initializing KEC R2024 database schema and default dataset...");
                executeSqlScript(conn, "kec_schema.sql");
                System.out.println("[+] Database schema successfully verified and initialized.");
            }
        } catch (Exception e) {
            System.err.println("[!] Schema initialization note: " + e.getMessage());
        }
    }

    /**
     * Executes SQL statements from an external script file.
     */
    public static void executeSqlScript(Connection conn, String scriptPath) {
        File file = new File(scriptPath);
        if (!file.exists()) {
            System.out.println("[!] Script file not found: " + scriptPath);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(scriptPath), StandardCharsets.UTF_8);
             Statement stmt = conn.createStatement()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Strip inline SQL comments (--)
                int commentIdx = line.indexOf("--");
                if (commentIdx >= 0) {
                    line = line.substring(0, commentIdx).trim();
                }
                if (line.isEmpty() || line.startsWith("//")) {
                    continue; // Skip blank lines and comments
                }
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sb.toString().trim();
                    if (sql.endsWith(";")) {
                        sql = sql.substring(0, sql.length() - 1).trim();
                    }
                    try {
                        stmt.execute(sql);
                    } catch (SQLException ex) {
                        // Suppress duplicate insert warnings on re-runs
                        if (!ex.getMessage().toLowerCase().contains("already exists") 
                                && !ex.getMessage().toLowerCase().contains("unique")
                                && !ex.getMessage().toLowerCase().contains("primary key")) {
                            System.err.println("[SQL Notice] " + ex.getMessage());
                        }
                    }
                    sb.setLength(0);
                }
            }
        } catch (Exception e) {
            System.err.println("[!] Error executing script: " + e.getMessage());
        }
    }

    public static String getActiveDatabaseType() {
        return activeDbType.toUpperCase();
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ignored) {
            } finally {
                connection = null;
            }
        }
    }
}
