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
public class ForeignKeyColumns implements Comparable<ForeignKeyColumns> {
    private final String tableColumnName;
    private final String relatedTableColumnName;

    public ForeignKeyColumns(String tableColumnName, String relatedTableColumnName) {
        this.tableColumnName = tableColumnName;
        this.relatedTableColumnName = relatedTableColumnName;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof ForeignKeyColumns))
        {
            return false;
        }
        ForeignKeyColumns foreignKeyColumns = (ForeignKeyColumns) object;
        return this.compareTo(foreignKeyColumns) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tableColumnName, this.relatedTableColumnName);
    }
    
    @Override
    public int compareTo(ForeignKeyColumns foreignKeyColumns) {
        if(foreignKeyColumns != null)
        {
            return ComparisonChain.start()
                .compare(this.tableColumnName, foreignKeyColumns.getTableColumnName())
                .compare(this.relatedTableColumnName, foreignKeyColumns.getRelatedTableColumnName())
                .result();
        }
        return 1;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public String getRelatedTableColumnName() {
        return relatedTableColumnName;
    }
    
}
