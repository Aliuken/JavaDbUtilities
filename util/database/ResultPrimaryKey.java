/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

/**
 *
 * @author User
 */
public class ResultPrimaryKey {
    private final String table;
    private final String fields;
    private final String constraintName;

    public ResultPrimaryKey(String table, String fields, String constraintName) {
        this.table = table;
        this.fields = fields;
        this.constraintName = constraintName;
    }
    
    public ResultPrimaryKey(PrimaryKey primaryKey) {
        this.table = primaryKey.getTable().toString();
        StringBuilder stringBuilder = new StringBuilder();
        if(primaryKey.getColumnNames() != null && !primaryKey.getColumnNames().isEmpty())
        {
            for(String columnName : primaryKey.getColumnNames())
            {
                stringBuilder.append(columnName);
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        this.fields = stringBuilder.toString();
        this.constraintName = primaryKey.getConstraintName();
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(table);
        stringBuilder.append("(");
        stringBuilder.append(fields);
        stringBuilder.append(") - ");
        stringBuilder.append(constraintName);
        return stringBuilder.toString();
    }

    public String getTable() {
        return table;
    }

    public String getFields() {
        return fields;
    }

    public String getConstraintName() {
        return constraintName;
    }
    
}
