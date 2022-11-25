package com.barteam.barject.Database;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private String filePath = "";
    private Connection connection = null;

    public DBManager() {}

    public DBManager(String filePath) { this.filePath = filePath; }

    public DBManager connect() throws SQLException {
        String url = String.format("jdbc:sqlite:%s", filePath);
        connection = DriverManager.getConnection(url);

        System.out.println("Connection to SQLite has been established.");
        return this;
    }

    public TableData getTableData(String tableName, QUERY_OPTION option, String... args) {
        final String query = String.format("SELECT * FROM %s", tableName);

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format("PRAGMA table_info(%s);", tableName));
            ArrayList<String> columnList = new ArrayList<>();
            while (rs.next()) {
                columnList.add(rs.getString(2));
            }
            String[] columns = new String[columnList.size()];
            columnList.toArray(columns);

            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmt = rs.getMetaData();
            final int columnCount = rsmt.getColumnCount();
            ArrayList<String[]> rows = new ArrayList<>();

            int j = 0; // iterator
            while (rs.next()) {
                if (option == QUERY_OPTION.INCLUDES && !rs.getString(2).toLowerCase().contains(args[0].toLowerCase())) // CHECK IF SECOND COLUMN HAS FILTER NAME
                    continue;

                if (option == QUERY_OPTION.STARTS && !rs.getString(2).toLowerCase().startsWith(args[0].toLowerCase()))
                    continue;

                ArrayList<String> row = new ArrayList<>();

                for (int i = 0; i < columnCount; i++) {
                    row.add(rs.getString(i + 1));
                }

                String[] rowArray = new String[row.size()];
                row.toArray(rowArray);
                rows.add(rowArray);
            }

            String[][] arrayOfRows = new String[rows.size()][columnCount];
            rows.toArray(arrayOfRows);

            return new TableData(arrayOfRows.length, columnCount, columns, arrayOfRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public String getFilePath() { return filePath.equals("") ? "no connection" : filePath; }
    public void setFilePath() { this.filePath = filePath; }
}
