package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Player")
@XmlType(propOrder = {"id", "user", "password", "name", "games", "victories"})
public class Player {

    private int id;
    private String user;
    private String password;
    private String name;
    private int games;
    private int victories;

    public Player(String nombre) {
        this.name = nombre;
        this.games = 0;
        this.victories = 0;
    }

    public Player(int id, String name, int games, int victories) {
        this.id = id;
        this.name = name;
        this.games = games;
        this.victories = victories;
    }

    public Player() {

    }
    
    public Player(int id, String user, String password, String name, int games, int victories) {
    	this.id = id;
    	this.user = user;
    	this.password = password;
    	this.name = name;
    	this.games = games;
    	this.victories = victories;
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "games")
    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }
    @XmlElement(name = "user")
    public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	@XmlElement(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement(name = "victories")
    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    @Override
    public String toString() {
        return "Player [id=" + id + ", name=" + name + ", games=" + games + ", victories=" + victories + "]";
    }
}
