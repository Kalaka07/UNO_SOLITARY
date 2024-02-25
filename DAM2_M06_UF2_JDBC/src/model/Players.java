package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Players")
public class Players {
	
	ArrayList<Player> players = new ArrayList<>();
	public Players() {
		
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	@XmlElement(name = "player")
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
}
