package server;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.security.auth.SolarisNumericUserPrincipal;

import shared.Item;
import shared.SellableItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBManager {

	private static final String dbPath = "db.json";
	private static int ITEM_ID = 0;

	private BufferedReader jsonReader;
	private BufferedWriter jsonWritter;
	private JsonObject root;
	private Gson gson;
	private ServerApp server;

	public DBManager(ServerApp server) {
	    this.server = server;
	    // read the existing db

        boolean emptyDatabase = false;
        try {
            this.jsonReader = new BufferedReader(new FileReader(dbPath));            
        } catch (FileNotFoundException e1) {
            Path file = Paths.get(dbPath);
            try {
                emptyDatabase = true;
                this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
                jsonWritter.write("{\n\"items\": []\n}");
                jsonWritter.flush();
                this.jsonReader = new BufferedReader(new FileReader(dbPath));
                } catch (IOException e) {
                e.printStackTrace();
            }
        }

		this.gson = new Gson();
		JsonParser parser = new JsonParser();
		this.root = parser.parse(this.jsonReader).getAsJsonObject();

		if(emptyDatabase)
            initializeDatabase();
		
		//Initialize ITEM_ID as last id used in database
		JsonElement registeredItems = root.get("items");
		if (registeredItems.isJsonArray()){
			int size = registeredItems.getAsJsonArray().size();
			if(size>0) {
				this.ITEM_ID = gson.fromJson(registeredItems.getAsJsonArray().get(size-1),SellableItem.class).getId()+1;
			}
		}
	}

	public Item addItem(Item i){
		i.setId(ITEM_ID++);
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public HashMap<Integer,Item> listItems() {
		HashMap<Integer,Item> items = new HashMap<Integer,Item>();
		if (root!= null ) {
		JsonElement registeredItems = root.get("items");
			if (registeredItems.isJsonArray()){
				for (JsonElement item : registeredItems.getAsJsonArray()){
					Item i = gson.fromJson(item, SellableItem.class);
					items.put(i.getId(), i);
				}
			}
		}
		return items;
	}

	public void updateItem(Item i) {
		// TODO Find item, remove it, replace it with new.
		JsonElement registeredItems = root.get("items");
		if (registeredItems.isJsonArray()){
			for (JsonElement item : registeredItems.getAsJsonArray()){
				if (item.getAsJsonObject().get("name").getAsString().equals(i.getName())) {
					registeredItems.getAsJsonArray().remove(item);
					break;
				}
			}
		}
		this.root.get("items").getAsJsonArray().add(gson.toJsonTree(i));
		try {
			Path file = Paths.get(dbPath);
			this.jsonWritter = Files.newBufferedWriter(file, StandardOpenOption.CREATE);
			jsonWritter.write(root.toString());
			jsonWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void initializeDatabase() {
        Item obj1 = new SellableItem(0,"Botruc", "Petite créature d'une vingtaine de centimètres ayant un aspect végétal et deux longs doigts pointus à chaque main. - Peut crocheter des serrures -", 400, "nDragonneau", 5);
        Item obj2 = new SellableItem(0,"Cerbère nain", "Chien géant à trois tête servant de gardien - Cet exemplaire est de petite taille -", 250, "nDragonneau", 4);
        Item obj3 = new SellableItem(0,"Demiguise", "Créature pouvant se rendre invisible lorsqu'elle est menacée. - Ses poils servent à tisser des toiles d'invisibilité -" , 900, "nDragonneau", 3);
        Item obj4 = new SellableItem(0,"Démonzémerveille", "Créature apparaissant sous forme de boule et se transformant, quand on la lance, en oiseau de proie bleu et vert. - A un attrait particulier pour le cerveau humain -", 1000, "nDragonneau", 2);
        Item obj5 = new SellableItem(0,"Éruptif", "Sorte de Rhinocéros géant vivant en Afrique. Le fluide contenu dans sa corne peut être injecté dans tout type de materiau, provoquant l'explosion de celui-ci. - Sa peau épaisse le rend insensible à la plupart des sorts -", 600, "nDragonneau", 2);
        Item obj6 = new SellableItem(0,"Plume d'Hippogriffe", "L'hippogriffe est une créature volante mi-aigle, mi- cheval. Il est très dangereux tant qu'il n'est pas dressé. - Cette plume a été récoltée dans les alentours de Poudlard et mesure 50 cm -", 150, "nDragonneau", 3);
        Item obj7 = new SellableItem(0,"Niffleur", "Animal à la fourrure noire et au long museau semblable à un ornithorynque. Ils sont attirés par tout ce qui brille. - Formidable voleur -", 250, "nDragonneau", 4);
        Item obj8 = new SellableItem(0,"OEuf d'Occamy", "Les Occamy sont une sorte d'oiseau-serpent. Ils ont la particularité d'être choranaptyxique : leur taille varient en fonction de l'espace dont ils disposent. - La coquille des oeufs d'Occamy est en argent pur -", 700, "nDragonneau", 6);
        Item obj9 = new SellableItem(0,"Oiseau-Tonnerre", "Vivant en Arizona, ces oiseau provoquent des tempêtes lorsqu'ils se sentent menacés. - Leur plume peuvent être utilisées pour fabriquer des baguettes magiques", 1250, "nDragonneau", 5);
        Item obj10 = new SellableItem(0,"OEuf congelé de Serpencendre", "Les serpencendres naissent dans des feux magiques laissés sans surveillance. Ils se cachent dans des recoins de la maison pour y pondre leurs oeufs qui, s'ils réussissent à grandir sans être repérés et chassés, enflamment la maison." , 2000, "nDragonneau", 1);

        addItem(obj1);
        addItem(obj2);
        addItem(obj3);
        addItem(obj4);
        addItem(obj5);
        addItem(obj6);
        addItem(obj7);
        addItem(obj8);
        addItem(obj9);
        addItem(obj10);
    }

}
