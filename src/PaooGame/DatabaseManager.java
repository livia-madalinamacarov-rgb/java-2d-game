package PaooGame;

import PaooGame.Exceptions.DatabaseException;

import java.sql.*;

/*! \class DatabaseManager
    \brief Gestionează baza de date SQLite a jocului.

    Clasa se ocupă de conectarea la baza de date, crearea tabelelor,
    salvarea și încărcarea progresului, gestionarea setărilor și afișarea leaderboard-ului.
 */
public class DatabaseManager {

    /*! \brief URL-ul bazei de date SQLite. */
    private static final String DB_URL = "jdbc:sqlite:mitzi_game.db";

    /*! \fn public static Connection connect()
        \brief Creează conexiunea la baza de date SQLite.

        Încarcă driverul SQLite și returnează o conexiune către fișierul bazei de date.

        \return Obiect Connection pentru baza de date.
        \throws SQLException Dacă apare o eroare la conectare.
     */
    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(DB_URL);
    }

    /*! \fn public static void createTables()
        \brief Creează tabelele necesare jocului.

        Creează tabela saves pentru progresul jucătorilor și tabela settings pentru setări.
        De asemenea, încearcă să adauge coloane suplimentare dacă acestea nu există deja.

        \throws DatabaseException Dacă apare o eroare la baza de date.
     */
    public static void createTables() throws DatabaseException {
        String savesSql =
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "player_name TEXT PRIMARY KEY, " +
                        "level INTEGER, " +
                        "score INTEGER, " +
                        "lives INTEGER, " +
                        "collected_fish TEXT, " +
                        "checkpoint_x INTEGER DEFAULT -1, " +
                        "checkpoint_y INTEGER DEFAULT -1" +
                        ");";

        String settingsSql =
                "CREATE TABLE IF NOT EXISTS settings (" +
                        "setting_name TEXT PRIMARY KEY, " +
                        "setting_value TEXT" +
                        ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(savesSql);
            stmt.execute(settingsSql);

            try {
                stmt.execute("ALTER TABLE saves ADD COLUMN collected_fish TEXT DEFAULT '';");
            } catch (SQLException ignored) {}

            try {
                stmt.execute("ALTER TABLE saves ADD COLUMN checkpoint_x INTEGER DEFAULT -1;");
            } catch (SQLException ignored) {}

            try {
                stmt.execute("ALTER TABLE saves ADD COLUMN checkpoint_y INTEGER DEFAULT -1;");
            } catch (SQLException ignored) {}

            System.out.println("Tabelele saves si settings sunt gata.");

        } catch (SQLException e) {
            throw new DatabaseException("Eroare la baza de date.", e);
        }
    }

    /*! \fn public static void saveOrUpdateGame(String playerName, int level, int score, int lives, String collectedFish, int checkpointX, int checkpointY)
        \brief Salvează sau actualizează progresul unui jucător.

        Folosește INSERT OR REPLACE pentru a crea o salvare nouă sau pentru a actualiza una existentă.

        \param playerName Numele jucătorului.
        \param level Nivelul curent.
        \param score Scorul jucătorului.
        \param lives Numărul de vieți rămase.
        \param collectedFish Lista peștilor colectați.
        \param checkpointX Coordonata X a checkpoint-ului.
        \param checkpointY Coordonata Y a checkpoint-ului.
     */
    public static void saveOrUpdateGame(String playerName, int level, int score, int lives,
                                        String collectedFish, int checkpointX, int checkpointY) {
        String sql =
                "INSERT OR REPLACE INTO saves(player_name, level, score, lives, collected_fish, checkpoint_x, checkpoint_y) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, level);
            pstmt.setInt(3, score);
            pstmt.setInt(4, lives);
            pstmt.setString(5, collectedFish);
            pstmt.setInt(6, checkpointX);
            pstmt.setInt(7, checkpointY);

            pstmt.executeUpdate();
            System.out.println("Progres salvat pentru " + playerName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*! \fn public static SaveData loadGame(String playerName)
        \brief Încarcă progresul salvat al unui jucător.

        Caută în tabela saves progresul asociat numelui jucătorului.

        \param playerName Numele jucătorului.
        \return Obiect SaveData cu progresul salvat sau null dacă nu există salvare.
     */
    public static SaveData loadGame(String playerName) {
        String sql =
                "SELECT level, score, lives, collected_fish, checkpoint_x, checkpoint_y " +
                        "FROM saves WHERE player_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new SaveData(
                        rs.getInt("level"),
                        rs.getInt("score"),
                        rs.getInt("lives"),
                        rs.getString("collected_fish"),
                        rs.getInt("checkpoint_x"),
                        rs.getInt("checkpoint_y")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*! \fn public static String getLeaderboardText()
        \brief Generează textul pentru leaderboard.

        Selectează primele 10 salvări, ordonate după scor, nivel, vieți și numele jucătorului.

        \return Textul leaderboard-ului.
     */
    public static String getLeaderboardText() {
        String sql =
                "SELECT player_name, level, score, lives FROM saves " +
                        "ORDER BY score DESC, level DESC, lives DESC, player_name ASC LIMIT 10";

        StringBuilder text = new StringBuilder();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            int place = 1;

            while (rs.next()) {
                text.append(place)
                        .append(". ")
                        .append(rs.getString("player_name"))
                        .append("  |  Score: ")
                        .append(rs.getInt("score"))
                        .append("  |  Level: ")
                        .append(rs.getInt("level"))
                        .append("  |  Lives: ")
                        .append(rs.getInt("lives"))
                        .append("\n");

                place++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (text.length() == 0) {
            text.append("Nu exista scoruri salvate.");
        }

        return text.toString();
    }

    /*! \fn public static void saveSetting(String name, String value)
        \brief Salvează sau actualizează o setare a jocului.

        \param name Numele setării.
        \param value Valoarea setării.
     */
    public static void saveSetting(String name, String value) {
        String sql = "INSERT OR REPLACE INTO settings(setting_name, setting_value) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, value);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*! \fn public static String loadSetting(String name)
        \brief Încarcă valoarea unei setări.

        Caută în tabela settings valoarea asociată numelui primit.

        \param name Numele setării.
        \return Valoarea setării sau null dacă nu există.
     */
    public static String loadSetting(String name) {
        String sql = "SELECT setting_value FROM settings WHERE setting_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("setting_value");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}