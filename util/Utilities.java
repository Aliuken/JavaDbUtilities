/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util;

import com.mycompany.exampleproject.util.database.DBUtilities;
import com.mycompany.exampleproject.util.database.ForeignKeyColumns;
import com.mycompany.exampleproject.util.database.ForeignKeyConstraint;
import com.mycompany.exampleproject.util.database.PrimaryKey;
import com.mycompany.exampleproject.util.database.QueryData;
import com.mycompany.exampleproject.util.database.Table;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author User
 */
public class Utilities {

    public static void printPrimaryKeys() throws Exception {
        System.out.println("PRIMARY KEYS");
        System.out.println("------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<Table, PrimaryKey>  pkMap = DBUtilities.getPrimaryKeyColumns(metaData, tables);
            DBUtilities.printPrimaryKeys(pkMap);
        }
    }

    public static void printTablePrimaryKey(Table table) throws Exception {
        System.out.println("TABLE PRIMARY KEY");
        System.out.println("-----------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = new ArrayList<Table>();
            tables.add(table);
            SortedMap<Table, PrimaryKey>  pkMap = DBUtilities.getPrimaryKeyColumns(metaData, tables);
            DBUtilities.printPrimaryKeys(pkMap);
        }
    }
    
    public static void printForeignKeys() throws Exception {
        System.out.println();
        System.out.println("FOREIGN KEYS");
        System.out.println("------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap = DBUtilities.getForeignKeys(metaData, tables);
            SortedMap<Table, PrimaryKey>  pkMap = DBUtilities.getPrimaryKeyColumns(metaData, tables);
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndexColumns(metaData, tables, true, true);
            DBUtilities.printForeignKeys(fkMap, pkMap, uniqueIndexMultimap);
        }
    }

    public static void printTableForeignKeys(Table table) throws Exception {
        System.out.println();
        System.out.println("TABLE FOREIGN KEYS");
        System.out.println("------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMultimap = DBUtilities.getForeignKeysByTable(metaData, table);
            PrimaryKey primaryKey = DBUtilities.getPrimaryKeyColumnsByTable(metaData, table);
            SortedMap<Table, PrimaryKey> pkMap = new TreeMap<Table, PrimaryKey>();
            pkMap.put(table, primaryKey);
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndicesByTable(metaData, table, true, true);
            DBUtilities.printForeignKeys(fkMultimap, pkMap, uniqueIndexMultimap);
        }
    }
    
    public static void printIndices() throws Exception {
        System.out.println();
        System.out.println("INDICES");
        System.out.println("-------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndexColumns(metaData, tables, false, true);
            DBUtilities.printIndices(uniqueIndexMultimap);
        }
    }

    public static void printTableIndices(Table table) throws Exception {
        System.out.println();
        System.out.println("TABLE INDICES");
        System.out.println("-------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndicesByTable(metaData, table, false, true);
            DBUtilities.printIndices(uniqueIndexMultimap);
        }
    }
    
    public static void printUniqueIndices() throws Exception {
        System.out.println();
        System.out.println("UNIQUE INDICES");
        System.out.println("--------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndexColumns(metaData, tables, true, true);
            DBUtilities.printIndices(uniqueIndexMultimap);
        }
    }

    public static void printTableUniqueIndices(Table table) throws Exception {
        System.out.println();
        System.out.println("TABLE UNIQUE INDICES");
        System.out.println("--------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            SortedMap<String, Collection<String>> uniqueIndexMultimap = DBUtilities.getIndicesByTable(metaData, table, true, true);
            DBUtilities.printIndices(uniqueIndexMultimap);
        }
    }
    
    public static void printEagerResultsQueries(List<QueryData> queryDataList, int remainingDepth) throws Exception {
        System.out.println();
        System.out.println("QUERIES EAGER RESULTS");
        System.out.println("---------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap = DBUtilities.getForeignKeys(metaData, tables);
            DBUtilities.printEagerResultsQuery(metaData, queryDataList, 1, remainingDepth, fkMap);
        }
    }
    
    public static void printEagerResultsQuery(Table table, String[] columnNames, List<String[]> valueList, int remainingDepth) throws Exception {
        System.out.println();
        System.out.println("QUERY EAGER RESULTS");
        System.out.println("-------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<QueryData> queryDataList = new ArrayList<QueryData>();
            QueryData queryData = new QueryData(table, columnNames, valueList);
            queryDataList.add(queryData);
            List<Table> tables = DBUtilities.getTables(metaData);
            SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap = DBUtilities.getForeignKeys(metaData, tables);
            DBUtilities.printEagerResultsQuery(metaData, queryDataList, 1, remainingDepth, fkMap);
        }
    }
    
    public static void printIndirectReferencesQueriesResults(List<QueryData> queryDataList, int remainingDepth) throws Exception {
        System.out.println();
        System.out.println("REFERENCES TO QUERIES RESULTS");
        System.out.println("-----------------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<Table> tables = DBUtilities.getTables(metaData);
            Map<Table, PrimaryKey>  pkMap = DBUtilities.getPrimaryKeyColumns(metaData, tables);
            DBUtilities.printIndirectReferences(metaData, queryDataList, 1, remainingDepth, pkMap);
        }
    }
    
    public static void printIndirectReferencesQueryResults(Table table, String[] columnNames, List<String[]> valueList, int remainingDepth) throws Exception {
        System.out.println();
        System.out.println("REFERENCES TO QUERY RESULTS");
        System.out.println("---------------------------");
        DatabaseMetaData metaData = DBUtilities.getConnectionMetaData();
        if(metaData != null)
        {
            List<QueryData> queryDataList = new ArrayList<QueryData>();
            QueryData queryData = new QueryData(table, columnNames, valueList);
            queryDataList.add(queryData);
            List<Table> tables = DBUtilities.getTables(metaData);
            Map<Table, PrimaryKey>  pkMap = DBUtilities.getPrimaryKeyColumns(metaData, tables);
            DBUtilities.printIndirectReferences(metaData, queryDataList, 1, remainingDepth, pkMap);
        }
    }
    
}
