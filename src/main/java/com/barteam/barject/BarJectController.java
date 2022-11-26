package com.barteam.barject;

import com.barteam.barject.Database.DBManager;
import com.barteam.barject.Database.QUERY_OPTION;
import com.barteam.barject.Database.TableData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class BarJectController {
    @FXML
    private Label table;

    @FXML
    protected void getTable() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            DBManager dbManager = new DBManager("db/products.db");
            dbManager.connect();
            TableData td = dbManager.getTableData("products", QUERY_OPTION.ALL);

            for (String[] row : td.getRows()) {
                stringBuilder.append(String.join(" ", row) + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        table.setText(stringBuilder.toString());
    }
    @FXML
    protected void ButtonExit() {
        Platform.exit();
        System.exit(0);
    }
}