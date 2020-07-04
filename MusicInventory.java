/*
 * MusicInventory.java
 * Justin McVicker
 */

// TO-DO:
// SHORT TERM (before initial release):
// Auto-load of db
// Add search function/field
// validation error testing, add more exception handling
// refactor code into MVE 
// MEDIUM TERM:
// Use bindings and/or listeners etc to update table instead of refresh()
// - Implement hashing to store releases 


package musicinventory;

import javafx.application.Application;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Float.parseFloat;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


/**
 *
 * @author Justin
 */
public class MusicInventory extends Application {
       
    // list of releases (data model)
    private final static ObservableList<Release> inventoryList = FXCollections.observableArrayList();
    
    @FXML private TableView<Release> releaseTable = new TableView(inventoryList);
    @FXML private TableColumn<Release,String> artistCol = new TableColumn();
    @FXML private TableColumn<Release,String> albumCol = new TableColumn();
    @FXML private TableColumn<Release,String> quantityCol = new TableColumn();;
    @FXML private TableColumn<Release,String> priceCol = new TableColumn();;
    
    public ObservableList<Release> getMusicColl() {
        return inventoryList;
    }
    
    public void addRelease(String a, String t, int q, float p) {
        Release tempR = new Release(a, t, q, p);
        inventoryList.add(tempR); 
        inventoryList.sort(Comparator.comparing(Release::getArtist));
        // to-do: replace with hashing, hash table instead of observable list?

    }
    

    
    public void removeRelease(Release r) {
        System.out.println(inventoryList.size());
        for (int i = 0; i < inventoryList.size(); i++) {
            if (inventoryList.get(i).equals(r)) {
                inventoryList.remove(i);
                System.out.println(inventoryList.size());
            }
        }
    }
    
    public Release getRelease(int index) {
        return inventoryList.get(index);
    }
    
    public int stringToInt(String s) {
        int i;
        try {
            i = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            i = 0;
        }
        return i;
    }
    
    public int findReleaseIndex(String a, String t) {
        Release tempR = new Release(a, t, 0, 0);
        for (int i = 0; i < releaseTable.getItems().size() ; i++){
            if (releaseTable.getItems().get(i).equals(tempR)) {
                return i;
            }
        }
        return -1;
    }
    
    @FXML protected void removeRelease(ActionEvent event) {
        Release selectedRelease = releaseTable.getSelectionModel().getSelectedItem();
        releaseTable.getItems().remove(selectedRelease);
        removeRelease(selectedRelease);
    }
    
    @FXML protected void saveDBfile(ActionEvent event) {
        BufferedWriter out = null;
        
        try {
            FileWriter fstream = new FileWriter("collectionDB.csv", false); //false tells not to append data.
            out = new BufferedWriter(fstream);
            for (int i = 0; i < inventoryList.size(); i++) {
                out.write(inventoryList.get(i).getArtist() + "," + inventoryList.get(i).getTitle() + "," + inventoryList.get(i).getQuantity() + "," + inventoryList.get(i).getPrice() + "\n");
            }
        }

        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    
    @FXML protected void loadDBfile(ActionEvent event) {
        BufferedReader in = null;
        String inString;

        inventoryList.clear();
        releaseTable.getItems().clear();
        try {
            File dbFile = new File("collectionDB.csv");
            in = new BufferedReader(new FileReader(dbFile));
            while((inString = in.readLine()) != null) { // while there's still (a) line(s) to be read in the file, process each line
                String[] tempEntry = inString.split(",");
                addRelease(tempEntry[0], tempEntry[1], stringToInt(tempEntry[2]), parseFloat(tempEntry[3])); // load ObservableList with db file entries
                artistCol.setCellValueFactory(new PropertyValueFactory("artist"));
                albumCol.setCellValueFactory(new PropertyValueFactory("title"));
                quantityCol.setCellValueFactory(new PropertyValueFactory("quantity"));
                priceCol.setCellValueFactory(new PropertyValueFactory("price"));
                Release temp = new Release(tempEntry[0], tempEntry[1], stringToInt(tempEntry[2]), parseFloat(tempEntry[3]));
                ObservableList<Release> tempOL = releaseTable.getItems();
                tempOL.add(temp);
            }
        }

        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ayayhaaaaaaaaaaaaaaaaaaaaaaaaa");
                }
            }
        }
        
    }
    
    @FXML public void addDialog(ActionEvent event) {
        Dialog<Release> dialog = new Dialog<>();
        dialog.setTitle("Add Release"); 
        dialog.setHeaderText("Add Release");
        GridPane gridPane = new GridPane();
        
        gridPane.setHgap(5);
        gridPane.setVgap(16);

        
        DialogPane dialogPane = dialog.getDialogPane();
        
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Label artistLabel = new Label("Artist:");
        GridPane.setRowIndex(artistLabel,0);
        GridPane.setColumnIndex(artistLabel, 0);
        GridPane.setHalignment(artistLabel,HPos.RIGHT);
        
        Label albumLabel = new Label("Album:");
        GridPane.setRowIndex(albumLabel,1);
        GridPane.setColumnIndex(artistLabel, 0);
        GridPane.setHalignment(albumLabel,HPos.RIGHT);
        
        Label quantLabel = new Label("Quantity:");
        GridPane.setRowIndex(quantLabel,2);
        GridPane.setColumnIndex(quantLabel, 0);
        GridPane.setHalignment(quantLabel,HPos.RIGHT);
        
        Label priceLabel = new Label("Price:");
        GridPane.setRowIndex(priceLabel,3);
        GridPane.setColumnIndex(priceLabel, 0);
        GridPane.setHalignment(priceLabel,HPos.RIGHT);
        
        TextField arField = new TextField();
        GridPane.setRowIndex(arField,0);
        GridPane.setColumnIndex(arField, 2);
        
        TextField alField = new TextField();
        GridPane.setRowIndex(alField,1);
        GridPane.setColumnIndex(alField, 2);
        
        TextField quField = new TextField();
        GridPane.setRowIndex(quField,2);
        GridPane.setColumnIndex(quField, 2);
        
        TextField prField = new TextField();
        GridPane.setRowIndex(prField,3);
        GridPane.setColumnIndex(prField, 2);
        
        
        gridPane.getChildren().addAll(artistLabel,albumLabel,quantLabel,priceLabel,arField,alField,quField,prField);
        
        dialogPane.setContent(gridPane);
        Platform.runLater(arField::requestFocus);
        
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                addRelease(arField.getText(),alField.getText(), stringToInt(quField.getText()), parseFloat(prField.getText()));
                artistCol.setCellValueFactory(new PropertyValueFactory("artist"));
                albumCol.setCellValueFactory(new PropertyValueFactory("title"));
                quantityCol.setCellValueFactory(new PropertyValueFactory("quantity"));
                priceCol.setCellValueFactory(new PropertyValueFactory("price"));
                releaseTable.getItems().add(new Release(arField.getText(), alField.getText(), stringToInt(quField.getText()), parseFloat(prField.getText())));
                releaseTable.getItems().sort(Comparator.comparing(Release::getArtist)
                .thenComparing(Release::getTitle));
            }
            return null;
        });
        
        Optional<Release> optionalRelease = dialog.showAndWait();
        optionalRelease.ifPresent((Release release) -> {
            System.out.println(
                release.getArtist() + " - " + release.getTitle());
        });       
    }
    
    @FXML public void editDialog(ActionEvent event) {
        Release editingRelease = releaseTable.getSelectionModel().getSelectedItem();
        Dialog<Release> dialog = new Dialog<>();
        dialog.setTitle("Add Release"); 
        dialog.setHeaderText("Add Release");
        GridPane gridPane = new GridPane();
        
        gridPane.setHgap(5);
        gridPane.setVgap(16);

        DialogPane dialogPane = dialog.getDialogPane();
        
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Label artistLabel = new Label("Artist:");
        GridPane.setRowIndex(artistLabel,0);
        GridPane.setColumnIndex(artistLabel, 0);
        GridPane.setHalignment(artistLabel,HPos.RIGHT);
        
        Label albumLabel = new Label("Album:");
        GridPane.setRowIndex(albumLabel,1);
        GridPane.setColumnIndex(artistLabel, 0);
        GridPane.setHalignment(albumLabel,HPos.RIGHT);
        
        Label quantLabel = new Label("Quantity:");
        GridPane.setRowIndex(quantLabel,2);
        GridPane.setColumnIndex(quantLabel, 0);
        GridPane.setHalignment(quantLabel,HPos.RIGHT);
        
        Label priceLabel = new Label("Price:");
        GridPane.setRowIndex(priceLabel,3);
        GridPane.setColumnIndex(priceLabel, 0);
        GridPane.setHalignment(priceLabel,HPos.RIGHT);
        
        TextField arField = new TextField(editingRelease.getArtist());
        GridPane.setRowIndex(arField,0);
        GridPane.setColumnIndex(arField, 2);
        
        TextField alField = new TextField(editingRelease.getTitle());
        GridPane.setRowIndex(alField,1);
        GridPane.setColumnIndex(alField, 2);
        
        TextField quField = new TextField(Integer.toString(editingRelease.getQuantity()));
        GridPane.setRowIndex(quField,2);
        GridPane.setColumnIndex(quField, 2);
        
        TextField prField = new TextField(Float.toString(editingRelease.getPrice()));
        GridPane.setRowIndex(prField,3);
        GridPane.setColumnIndex(prField, 2);
        
        
        gridPane.getChildren().addAll(artistLabel,albumLabel,quantLabel,priceLabel,arField,alField,quField,prField);
        
        dialogPane.setContent(gridPane);
        Platform.runLater(arField::requestFocus);
        
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                inventoryList.get(releaseTable.getSelectionModel().getSelectedIndex()).setArtist(arField.getText());
                inventoryList.get(releaseTable.getSelectionModel().getSelectedIndex()).setTitle(alField.getText());
                inventoryList.get(releaseTable.getSelectionModel().getSelectedIndex()).setQuantity(stringToInt(quField.getText()));
                inventoryList.get(releaseTable.getSelectionModel().getSelectedIndex()).setPrice(parseFloat(prField.getText()));
                int index = releaseTable.getSelectionModel().getSelectedIndex();
                Release release  = releaseTable.getItems().get(index);
                System.out.println(release.getTitle());
                release.setArtist(arField.getText());
                release.setTitle(alField.getText());
                release.setQuantity(stringToInt(quField.getText()));
                release.setPrice(parseFloat(prField.getText()));
                releaseTable.refresh();
            }
            return null;
        });
        
        Optional<Release> optionalRelease = dialog.showAndWait();
        optionalRelease.ifPresent((Release release) -> {
            System.out.println(
                release.getArtist() + " - " + release.getTitle());
        });
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("music_inventory.fxml"));
        
        Scene scene = new Scene(root, 1000, 700);
        
        stage.setTitle("Music Inventory");
        
        stage.setScene(scene);
       
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        launch(args);
    }
}
