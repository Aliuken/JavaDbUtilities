/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import java.util.List;

/**
 *
 * @author User
 */
public class QueryData {
    private final Table table;
    private final String[] columnNames;
    private final List<String[]> valueList;

    public QueryData(Table table, String[] columnNames, List<String[]> valueList) {
        this.table = table;
        this.columnNames = columnNames;
        this.valueList = valueList;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(this.getTable().toString());
        stringBuilder.append("(");
        for(String columnName : columnNames)
        {
            stringBuilder.append(columnName);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        for(String[] values : valueList)
        {
            stringBuilder.append("[");
            for(String value : values)
            {
                stringBuilder.append(value);
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    public Table getTable() {
        return table;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<String[]> getValueList() {
        return valueList;
    }
    
}
