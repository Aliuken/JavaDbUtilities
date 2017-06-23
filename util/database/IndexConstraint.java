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
public class IndexConstraint extends TableConstraint implements Comparable<TableConstraint> {

    public IndexConstraint(Table table, String constraintName) {
        super(table, constraintName);
    }
    
    @Override
    public String toString()
    {
        return super.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof IndexConstraint))
        {
            return false;
        }
        IndexConstraint indexConstraint = (IndexConstraint) object;
        return this.compareTo(indexConstraint) == 0;
    }
    
    @Override
    public int compareTo(TableConstraint tableConstraint) {
        IndexConstraint indexConstraint = (IndexConstraint) tableConstraint;
        return super.compareTo(indexConstraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTable(), this.getConstraintName());
    }
    
}
