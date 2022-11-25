package com.barteam.barject.Database;

import java.sql.SQLException;

public class SAMPLE_RUN {
    public static void main(String[] args) throws SQLException {
        DBManager dbManager = new DBManager("db/products.db");
        dbManager.connect();

        TableData td = dbManager.getTableData("products", QUERY_OPTION.INCLUDES, "en");
        System.out.println(td.getRowCount());
        System.out.println(String.join(" ", td.getColumnNames()));

        for (String[] row : td.getRows()) {
            System.out.println(String.join(" ", row));
        }

    }
}