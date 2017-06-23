/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import java.util.Objects;

/**
 *
 * @author User
 */
public class Table implements Comparable<Table> {
    private final String catalog;
    private final String schema;
    private final String tableName;

    public Table(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(!Strings.isNullOrEmpty(catalog))
        {
            stringBuilder.append(catalog);
            stringBuilder.append(".");
        }
        if(!Strings.isNullOrEmpty(schema))
        {
            stringBuilder.append(schema);
            stringBuilder.append(".");
        }
        stringBuilder.append(tableName);
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof Table))
        {
            return false;
        }
        Table table = (Table) object;
        return this.compareTo(table) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.catalog, this.schema, this.tableName);
    }
    
    @Override
    public int compareTo(Table table) {
        if(table != null)
        {
            return ComparisonChain.start()
                .compare(this.catalog, table.getCatalog())
                .compare(this.schema, table.getSchema())
                .compare(this.tableName, table.getTableName())
                .result();
        }
        return 1;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }
    
}
