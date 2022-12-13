package com.barteam.barject;

import com.barteam.barject.Database.DBManager;
import com.barteam.barject.Database.QUERY_OPTION;
import com.barteam.barject.Database.TableData;
import com.barteam.barject.generator.EAN13Generator;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class BarJectController {

    private EAN13Generator generator;
    private DBManager dbManager;
    private List<String> columnNames;

    @FXML
    private ToggleButton barButton;

    @FXML
    private ToggleButton descButton;

    @FXML
    private ImageView barImage;

    @FXML
    private Label description;

    @FXML
    private AnchorPane descriptionPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleGroup toggl;


    @FXML
    private TableView<ObservableList<String>> tableView;

    @FXML
    void initialize() {
        generator = new EAN13Generator();
        // Additional arguments
        generator.setImageHeight(120);
        generator.setImageWidth(130);
        generator.setStartYPos(0);
        generator.setStartXPos(0);
        generator.setDefaultGuardHeight(120);
        generator.setDefaultLineHeight(80);

        try {
            dbManager = new DBManager("db/products.db");
            dbManager.connect();
        }catch (Exception e) {

        }

        searchButton.setOnAction(event -> {
            String s = searchField.getText();
            resetTable(s);
        });

        TableView.TableViewSelectionModel<ObservableList<String>> a = tableView.getSelectionModel();
        a.selectedItemProperty().addListener(new ChangeListener<ObservableList<String>>(){
            public void changed(ObservableValue<? extends ObservableList<String>> val, ObservableList<String> oldVal, ObservableList<String> newVal){
                descButton.setVisible(true);
                barButton.setVisible(true);
                String s = "";
                if(newVal != null) {
                    descriptionPane.setVisible(true);
                    String barcode = newVal.get(0);
                    while (barcode.length() < 12) {
                        barcode = barcode + "0";
                    }
                    try {
                        BufferedImage bufferedImage = generator.generateEAN13(barcode);
                        ImageIO.write(bufferedImage, "PNG", new File("src/main/resources/images/barcodeTest.PNG"));
                        barImage.setImage(convertToFxImage(bufferedImage));
                    }
                    catch (Exception e) {}
                    for (int i = 0; i < newVal.size(); i ++) {
                        s += columnNames.get(i)+ ": " + newVal.get(i) + "\n";
                    }
                }
                else {
                    descriptionPane.setVisible(false);
                }

                description.setText(s);
            }
        });

        barButton.setOnAction(event ->{
            description.setVisible(false);
            barImage.setVisible(true);
        });
        descButton.setOnAction(event ->{
            description.setVisible(true);
            barImage.setVisible(false);
        });

        setTable();
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    private void setTable() {
        TableData td;

        try {
            td = dbManager.getTableData("desc", QUERY_OPTION.ALL);

            columnNames = Arrays.asList(td.getColumnNames());
            for (int i = 0; i < columnNames.size(); i++) {
                final int idx = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                        columnNames.get(i)
                );
                column.setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().get(idx))
                );
                tableView.getColumns().add(column);
            }

            String[][] rows = td.getRows();

            for (int i = 0; i < rows.length; i++) {
                tableView.getItems().add(
                        FXCollections.observableArrayList(
                                Arrays.asList(rows[i])
                        )
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void resetTable(String search) {
        TableData td;

        try {
            td = dbManager.getTableData("desc", QUERY_OPTION.INCLUDES, search);

            String[][] rows = td.getRows();
            tableView.getItems().clear();

            for (int i = 0; i < rows.length; i++) {
                tableView.getItems().add(
                        FXCollections.observableArrayList(
                                Arrays.asList(rows[i])
                        )
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void ButtonExit() {
        Platform.exit();
        System.exit(0);
    }
    public void about() {
        descButton.setVisible(false);
        barButton.setVisible(false);
        barImage.setVisible(false);
        descriptionPane.setVisible(true);
        description.setText("Информация о проекте\n еще что-то...");
        description.setVisible(true);
    }
    public void support() {
        descButton.setVisible(false);
        barImage.setVisible(false);
        barButton.setVisible(false);
        descriptionPane.setVisible(true);
        description.setText("К нам можно обратиться следующими способами\n еще что-то...");
        description.setVisible(true);
    }
}