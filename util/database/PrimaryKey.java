/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author User
 */
public class PrimaryKey extends TableConstraint implements Comparable<TableConstraint> {
    private final List<String> columnNames;
    private final Map<String, Integer> columnOrderMap;

    public PrimaryKey(Table table, String constraintName, List<String> columnNames) {
        super(table, constraintName);
        this.columnNames = columnNames;
        this.columnOrderMap = new HashMap<String, Integer>();
        for(int i = 0; i < columnNames.size(); i++)
        {
            columnOrderMap.put(columnNames.get(i), i);
        }
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
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof PrimaryKey))
        {
            return false;
        }
        PrimaryKey primaryKey = (PrimaryKey) object;
        return this.compareTo(primaryKey) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTable(), this.getConstraintName());
    }
    
    @Override
    public int compareTo(TableConstraint tableConstraint) {
        PrimaryKey indexConstraint = (PrimaryKey) tableConstraint;
        return super.compareTo(indexConstraint);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Map<String, Integer> getColumnOrderMap() {
        return columnOrderMap;
    }
    
}
