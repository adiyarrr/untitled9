import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CinemaTicketSystem {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "220222";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            addMovie(connection, "Avengers: Endgame", "Anthony Russo, Joe Russo", "Action", "The Avengers must save the universe from Thanos.");
            addMovie(connection, "The Shawshank Redemption", "Frank Darabont", "Drama", "Two imprisoned men bond over a number of years.");
            viewMovies(connection);
            updateMovie(connection, 1, "Avengers: Endgame", "Anthony Russo, Joe Russo", "Action", "The Avengers must save the universe from Thanos.");
            deleteMovie(connection, 2);
            viewMovies(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addMovie(Connection connection, String title, String director, String genre, String description) throws SQLException {
        String sql = "INSERT INTO movies(title, director, genre, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, director);
            statement.setString(3, genre);
            statement.setString(4, description);
            statement.executeUpdate();
            System.out.println("Added movie: " + title + ", Director: " + director + ", Genre: " + genre + ", Description: " + description);
        }
    }

    private static void viewMovies(Connection connection) throws SQLException {
        String sql = "SELECT * FROM movies";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int movieId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                String genre = resultSet.getString("genre");
                String description = resultSet.getString("description");
                System.out.println("Movie ID: " + movieId + ", Title: " + title + ", Director: " + director + ", Genre: " + genre + ", Description: " + description);
            }
        }
    }

    private static void updateMovie(Connection connection, int movieId, String title, String director, String genre, String description) throws SQLException {
        String sql = "UPDATE movies SET title = ?, director = ?, genre = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, director);
            statement.setString(3, genre);
            statement.setString(4, description);
            statement.setInt(5, movieId);
            statement.executeUpdate();
            System.out.println("Updated movie with ID " + movieId + " to: " + title + ", Director: " + director + ", Genre: " + genre + ", Description: " + description);
        }
    }

    private static void deleteMovie(Connection connection, int movieId) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, movieId);
            statement.executeUpdate();
            System.out.println("Deleted movie with ID " + movieId);
        }
    }
}
