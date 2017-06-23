/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import java.util.Objects;

/**
 *
 * @author User
 */
public class ForeignKeyConstraint extends TableConstraint implements Comparable<TableConstraint> {
    private final Table relatedTable;

    public ForeignKeyConstraint(Table table, String constraintName, Table relatedTable) {
        super(table, constraintName);
        this.relatedTable = relatedTable;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(this.getTable().toString());
        stringBuilder.append(" -> ");
        stringBuilder.append(this.relatedTable);
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof ForeignKeyConstraint))
        {
            return false;
        }
        ForeignKeyConstraint foreignKeyConstraint = (ForeignKeyConstraint) object;
        return this.compareTo(foreignKeyConstraint) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTable(), this.getConstraintName());
    }
    
    @Override
    public int compareTo(TableConstraint tableConstraint) {
        ForeignKeyConstraint foreignKeyConstraint = (ForeignKeyConstraint) tableConstraint;
        return super.compareTo(foreignKeyConstraint);
    }

    public Table getRelatedTable() {
        return relatedTable;
    }
    
}
