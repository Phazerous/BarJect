package com.barteam.barject;

import com.barteam.barject.Database.DBManager;
import com.barteam.barject.Database.QUERY_OPTION;
import com.barteam.barject.Database.TableData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
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

        welcomeText.setText(stringBuilder.toString());
    }
}