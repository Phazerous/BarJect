package com.barteam.barject;

import com.barteam.barject.Database.DBManager;
import com.barteam.barject.Database.QUERY_OPTION;
import com.barteam.barject.Database.TableData;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;
import java.util.List;


public class BarJectController {
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
                String s = "";
                if(newVal != null) {
                    descriptionPane.setVisible(true);
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
}