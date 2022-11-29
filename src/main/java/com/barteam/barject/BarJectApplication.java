package com.barteam.barject;

import com.barteam.barject.Database.DBManager;
import com.barteam.barject.Database.QUERY_OPTION;
import com.barteam.barject.Database.TableData;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BarJectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        DBManager dbManager = new DBManager("db/products.db");
        dbManager.connect();
        TableData td = dbManager.getTableData("desc", QUERY_OPTION.ALL);

        TableView<ObservableList<String>> tableView = new TableView<>();

        List<String> columnNames = Arrays.asList(td.getColumnNames());
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

        tableView.setPrefHeight(200);

        Scene scene = new Scene(tableView);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}