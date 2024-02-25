package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import model.Player;
import model.Players;

public class FileImplement {

    public void saveAllPlayersDataToFile(List<Player> players) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("players_data.txt", true))) {
            for (Player player : players) {
                writer.println("Player Name: " + player.getName());
                writer.println("Games Played: " + player.getGames());
                writer.println("Victories: " + player.getVictories());
                writer.println("----------------------------");
            }
            System.out.println("All players' data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving players' data to file.");
            e.printStackTrace();
        }
    }
    public void savePlayerDataToXMLUsingDOM(List<Player> players) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element playersElement = document.createElement("players");
            document.appendChild(playersElement);

            for (Player player : players) {
                Element playerElement = document.createElement("player");

                playerElement.setAttribute("id", String.valueOf(player.getId()));

                addChildElement(document, playerElement, "name", player.getName());
                addChildElement(document, playerElement, "games", String.valueOf(player.getGames()));
                addChildElement(document, playerElement, "victories", String.valueOf(player.getVictories()));

                playersElement.appendChild(playerElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            
            String xmlFileName = "players_data_DOM.xml";
            StreamResult result = new StreamResult(new File(xmlFileName));

            transformer.transform(source, result);

            System.out.println("Player data saved to XML using DOM library.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving player data to XML using DOM library.");
        }
    }

    private void addChildElement(Document document, Element parentElement, String elementName, String textContent) {
        Element childElement = document.createElement(elementName);
        Text textNode = document.createTextNode(textContent);
        childElement.appendChild(textNode);
        parentElement.appendChild(childElement);
    }

	
    public void savePlayerDataToXMLUsingJAXB(ArrayList<Player> allPlayers) {
        try {
        	Players players = new Players();
        	players.setPlayers(allPlayers);
            
            JAXBContext context = JAXBContext.newInstance(Players.class);

            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(players, new File("playersDataJAXB.xml"));

            System.out.println("Player data saved to playersDataJAXB.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error saving player data to XML using JAXB.");
        }
    }
}