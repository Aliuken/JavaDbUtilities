/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import com.google.common.collect.ComparisonChain;
import java.util.Objects;

/**
 *
 * @author User
 */
public class TableConstraint implements Comparable<TableConstraint> {
    private final Table table;
    private final String constraintName;

    public TableConstraint(Table table, String constraintName) {
        this.table = table;
        this.constraintName = constraintName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(this.table.toString());
        stringBuilder.append(" - ");
        stringBuilder.append(this.constraintName);
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof TableConstraint))
        {
            return false;
        }
        TableConstraint tableConstaint = (TableConstraint) object;
        return this.compareTo(tableConstaint) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.table, this.constraintName);
    }
    
    @Override
    public int compareTo(TableConstraint tableConstraint) {
        if(tableConstraint != null)
        {
            return ComparisonChain.start()
                .compare(this.table, tableConstraint.getTable())
                .compare(this.constraintName, tableConstraint.getConstraintName())
                .result();
        }
        return 1;
    }

    public Table getTable() {
        return table;
    }

    public String getConstraintName() {
        return constraintName;
    }
    
}
