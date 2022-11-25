package com.barteam.barject.Database;

public class TableData {
    private final int columnCount;
    private final int rowCount;
    private final String[] columnNames;
    private final String[][] rows;

    public TableData(int rowCount, int columnCount, String[] columnNames, String[][] rows) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        this.rows = rows;
    }

    public int getColumnCount() { return columnCount; }
    public int getRowCount() { return rowCount; }
    public String[] getColumnNames() { return columnNames; }
    public String[][] getRows() { return rows; }
}
