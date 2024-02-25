package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Player;

public class DaoImpl implements Dao{

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/UNO";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456789";
	
    private Connection connection;
    
    public DaoImpl () throws SQLException {
    	this.connect();
    }
    
    @Override
    public void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con la base de datos.");
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public int getLastIdCard(int playerId) throws SQLException {
        int lastId = 0;

        try (PreparedStatement statement = connection.prepareStatement("SELECT IFNULL(MAX(id), 0) + 1 FROM card WHERE id_player = ?")) {
            statement.setInt(1, playerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        }

        return lastId;
    }


    @Override
    public Card getLastCard() throws SQLException {
        Card lastCard = null;

        String query = "SELECT card.id, card.numero, card.color, card.id_player " +
                       "FROM card JOIN game ON card.id = game.id_card " +
                       "ORDER BY game.id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String numberStr = resultSet.getString("numero");
                String colorStr = resultSet.getString("color");
                int playerId = resultSet.getInt("id_player");

                // Creamos un objeto Card utilizando el constructor actual
                lastCard = new Card(id, numberStr, colorStr, playerId);
            }
        }

        return lastCard;
    }



    @Override
    public Player getPlayer(String user, String pass) throws SQLException {
        Player player = null;

        String query = "SELECT * FROM player WHERE userPlayer = ? AND passwordPlayer = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);
            statement.setString(2, pass);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String playerName = resultSet.getString("namePlayer");
                    int games = resultSet.getInt("games");
                    int victories = resultSet.getInt("victories");

                    // Creamos un objeto Player utilizando el constructor actual
                    player = new Player(id, playerName, games, victories);
                }
            }
        }

        return player;
    }


    @Override
    public ArrayList<Card> getCards(int playerId) throws SQLException {
        ArrayList<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM card WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int cardId = resultSet.getInt("id");
                    String number = resultSet.getString("numero");
                    String color = resultSet.getString("color");

                    Card card = new Card(cardId, number, color, playerId);
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }



    @Override
    public Card getCard(int cardId) throws SQLException {
        Card card = null;
        String query = "SELECT * FROM card WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cardId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int playerId = resultSet.getInt("id_player");
                    String number = resultSet.getString("numero");
                    String color = resultSet.getString("color");

                    card = new Card(cardId, number, color, playerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }


    @Override
    public void saveGame(Card card) throws SQLException {
        String query = "INSERT INTO game (id_card) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


    @Override
    public void saveCard(Card card) throws SQLException {
        String query = "INSERT INTO card (id, id_player, numero, color) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.setInt(2, card.getPlayerId());
            preparedStatement.setString(3, card.getNumber());
            preparedStatement.setString(4, card.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


    @Override
    public void deleteCard(Card card) throws SQLException {
        String query = "DELETE FROM card WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void clearDeck(int playerId) throws SQLException {
        String query = "DELETE FROM card WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


    @Override
    public void addVictories(int playerId) throws SQLException {
        String query = "UPDATE player SET victories = victories + 1 WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addGames(int playerId) throws SQLException {
        String query = "UPDATE player SET games = games + 1 WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


    public List<Player> getAllPlayers() throws SQLException {
        List<Player> allPlayers = new ArrayList<>();
        String query = "SELECT * FROM player";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String playerName = resultSet.getString("namePlayer");
                int games = resultSet.getInt("games");
                int victories = resultSet.getInt("victories");

                Player player = new Player(id, playerName, games, victories);
                allPlayers.add(player);
            }
        }

        return allPlayers;
    }
    
    public ArrayList<Player> returnPlayers(){
    	String allPlayers= "SELECT * FROM player";
    	ArrayList<Player> jugadores = new ArrayList<>();
    	try {
    		PreparedStatement statement = this.connection.prepareStatement(allPlayers);
    		ResultSet resultadito = statement.executeQuery();
    		while(resultadito.next()) {
    			jugadores.add(new Player(resultadito.getInt("id"), resultadito.getString("userPlayer"), resultadito.getString("passwordPlayer"),
    							resultadito.getString("namePlayer"), resultadito.getInt("games"), resultadito.getInt("victories")));
    		}
    		return jugadores;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }


}
