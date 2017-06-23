/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 *
 * @author User
 */
public class DBUtilities {
    public static List<Table> getTables(DatabaseMetaData metaData) throws Exception {
        ResultSet tables = metaData.getTables(null, null, "%", null);
        if(tables != null)
        {
            List<Table> tableList = new ArrayList<Table>();
            while (tables.next()) {
                tableList.add(new Table(
                    tables.getString("TABLE_CAT"), 
                    tables.getString("TABLE_SCHEM"),
                    tables.getString("TABLE_NAME")
                ));
            }
            return tableList;
        }
        return null;
    }
    
    public static SortedMap<Table, PrimaryKey> getPrimaryKeyColumns(DatabaseMetaData metaData, List<Table> tables) throws Exception {
        if(tables != null)
        {
            SortedMap<Table, PrimaryKey> pkMap = new TreeMap<Table, PrimaryKey>();
            for(Table table : tables)
            {
                PrimaryKey primaryKey = getPrimaryKeyColumnsByTable(metaData, table);
                if(primaryKey != null)
                {
                    pkMap.put(table, primaryKey);
                }
            }
            return pkMap;
        }
        return null;
    }
    
    public static PrimaryKey getPrimaryKeyColumnsByTable(DatabaseMetaData metaData, Table table) throws Exception {
        ResultSet primaryKeyColumns = (table != null) ? metaData.getPrimaryKeys(table.getCatalog(), table.getSchema(), table.getTableName()) : null;
        if(primaryKeyColumns != null && primaryKeyColumns.next())
        {
            Table pkTable = new Table(
                primaryKeyColumns.getString("TABLE_CAT"),
                primaryKeyColumns.getString("TABLE_SCHEM"),
                primaryKeyColumns.getString("TABLE_NAME"));
            String pkConstraintName = primaryKeyColumns.getString("PK_NAME");
            List<String> pkColumnNames = new ArrayList<String>();
            do {
                pkColumnNames.add(primaryKeyColumns.getString("COLUMN_NAME"));
            } while (primaryKeyColumns.next());
            PrimaryKey primaryKey = new PrimaryKey(pkTable, pkConstraintName, pkColumnNames);
            return primaryKey;
        }
        return null;
    }
    
    public static SortedMap<String, Collection<String>> getIndexColumns(DatabaseMetaData metaData, List<Table> tables, boolean unique, boolean approximate) throws Exception {
        if(tables != null)
        {
            SortedMap<String, Collection<String>> indexMap = new TreeMap<String, Collection<String>>();
            for(Table table : tables)
            {
                SortedMap<String, Collection<String>> tableIndexMap = getIndicesByTable(metaData, table, unique, approximate);
                if(tableIndexMap != null)
                {
                    indexMap.putAll(tableIndexMap);
                }
            }
            return indexMap;
        }
        return null;
    }
    
    public static SortedMap<String, Collection<String>> getIndicesByTable(DatabaseMetaData metaData, Table table, boolean unique, boolean approximate) throws Exception {
        ResultSet indexColumns = (table != null) ? metaData.getIndexInfo(table.getCatalog(), table.getSchema(), table.getTableName(), unique, approximate) : null;
        if(indexColumns != null && indexColumns.next())
        {
            SortedSetMultimap<String, String> indexMultimap = TreeMultimap.create();
            Table tableAux = new Table(
                indexColumns.getString("TABLE_CAT"),
                indexColumns.getString("TABLE_SCHEM"),
                indexColumns.getString("TABLE_NAME"));
            do {
                
                String indexConstraintName = indexColumns.getString("INDEX_NAME");
                IndexConstraint indexConstraint = new IndexConstraint(tableAux, indexConstraintName);
                String indexColumnName = indexColumns.getString("COLUMN_NAME");
                indexMultimap.put(indexConstraint.toString(), indexColumnName);
            } while (indexColumns.next());
            SortedMap indexMap = new TreeMap<String, Collection<String>>();
            indexMap.putAll(indexMultimap.asMap());
            return indexMap;
        }
        return null;
    }
    
    public static SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> getForeignKeys(DatabaseMetaData metaData, List<Table> tables) throws Exception {
        if(tables != null)
        {
            SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap = new TreeMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>>();
            for(Table table : tables)
            {
                SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> tableFKMap = getForeignKeysByTable(metaData, table);
                if(tableFKMap != null)
                {
                    fkMap.putAll(tableFKMap);
                }
            }
            return fkMap;
        }
        return null;
    }
    
    public static SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> getForeignKeysByTable(DatabaseMetaData metaData, Table table) throws Exception {
        ResultSet foreignKeys = (table != null) ? metaData.getImportedKeys(table.getCatalog(), table.getSchema(), table.getTableName()) : null;
        if(foreignKeys != null && foreignKeys.next())
        {
            SortedSetMultimap<ForeignKeyConstraint, ForeignKeyColumns> fkMultimap = TreeMultimap.create();
            Table fkTable = new Table(
                foreignKeys.getString("FKTABLE_CAT"),
                foreignKeys.getString("FKTABLE_SCHEM"),
                foreignKeys.getString("FKTABLE_NAME"));
            do {
                String fkConstraintName = foreignKeys.getString("FK_NAME");
                Table pkTable = new Table(
                    foreignKeys.getString("PKTABLE_CAT"),
                    foreignKeys.getString("PKTABLE_SCHEM"),
                    foreignKeys.getString("PKTABLE_NAME"));
                ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint(fkTable, fkConstraintName, pkTable);
                ForeignKeyColumns fkColumns = new ForeignKeyColumns(
                    foreignKeys.getString("FKCOLUMN_NAME"),
                    foreignKeys.getString("PKCOLUMN_NAME"));
                fkMultimap.put(fkConstraint, fkColumns);
            } while (foreignKeys.next());
            SortedMap fkMap = new TreeMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>>();
            fkMap.putAll(fkMultimap.asMap());
            return fkMap;
        }
        return null;
    }
    
    public static List<QueryData> getReferencedColumnsValues(DatabaseMetaData metaData, QueryData queryData, SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap, int depth) throws Exception {
        if(queryData != null && queryData.getTable() != null && queryData.getTable().getTableName() != null &&
            queryData.getColumnNames() != null && queryData.getColumnNames().length > 0 && 
            queryData.getValueList() != null && !queryData.getValueList().isEmpty())
        {
            StringBuilder sql = new StringBuilder("select * from ");
            sql.append(queryData.getTable().toString());
            sql.append(" where ");
            
            StringBuilder sqlCondition = new StringBuilder("(");
            for(String columnName : queryData.getColumnNames())
            {
                sqlCondition.append(columnName);
                sqlCondition.append(" = ? and ");
            }
            sqlCondition.delete(sqlCondition.length() - 5, sqlCondition.length());
            sqlCondition.append(") or ");
            
            for(String[] values : queryData.getValueList())
            {
                boolean areFilledReferencingColumnsValues = isFilledArray(values);
                if(areFilledReferencingColumnsValues)
                {
                    sql.append(sqlCondition);
                }
            }
            if(" or ".equals(sql.substring(sql.length() - 4, sql.length())))
            {
                sql.delete(sql.length() - 4, sql.length());
            }
            
            PreparedStatement statement = metaData.getConnection().prepareStatement(sql.toString());
            StringBuilder sqlParams = new StringBuilder();
            int i = 0;
            for(String[] values : queryData.getValueList())
            {
                boolean areFilledReferencingColumnsValues = isFilledArray(values);
                if(areFilledReferencingColumnsValues)
                {
                    sqlParams.append("[");
                    for(String value : values)
                    {
                        statement.setString(i+1, value);
                        sqlParams.append(value);
                        sqlParams.append(",");
                        i++;
                    }
                    sqlParams.deleteCharAt(sqlParams.length() - 1);
                    sqlParams.append("]");
                }
            }
            System.out.println("depth " + depth + ": " + sql.toString() + sqlParams.toString());
            ResultSet rs = statement.executeQuery();
            if(rs != null) 
            {
                SortedSetMultimap<ForeignKeyConstraint, ForeignKeyColumns> tableFkMultimap = TreeMultimap.create();
                for(ForeignKeyConstraint fKConstraint : fkMap.keySet())
                {
                    if(queryData.getTable().equals(fKConstraint.getTable()))
                    {
                        tableFkMultimap.putAll(fKConstraint, fkMap.get(fKConstraint));
                    }
                }
                List<QueryData> nextQueryDataList = new ArrayList<QueryData>();
                Map<String, Table> constraintTableMap = new HashMap<String, Table>();
                Map<String, String[]> constraintColumnsMap = new HashMap<String, String[]>();
                ListMultimap<String, String[]> constraintValuesMultimap = ArrayListMultimap.create();
                while (rs.next()) {
                    for(ForeignKeyConstraint tableFKConstraint : tableFkMultimap.keySet())
                    {
                        SortedSet<ForeignKeyColumns> tableFkColumnsList = tableFkMultimap.get(tableFKConstraint);
                        String[] relatedTableColumnNames = new String[tableFkColumnsList.size()];
                        String[] relatedTableColumnValues = new String[tableFkColumnsList.size()];
                        i = 0;
                        for(ForeignKeyColumns tableFKColumns : tableFkColumnsList)
                        {
                            relatedTableColumnNames[i] = tableFKColumns.getRelatedTableColumnName();
                            relatedTableColumnValues[i] = rs.getString(tableFKColumns.getTableColumnName());
                            i++;
                        }
                        constraintTableMap.put(tableFKConstraint.getConstraintName(), tableFKConstraint.getRelatedTable());
                        constraintColumnsMap.put(tableFKConstraint.getConstraintName(), relatedTableColumnNames);
                        constraintValuesMultimap.put(tableFKConstraint.getConstraintName(), relatedTableColumnValues);
                    }
                }
                for(String constraint : constraintTableMap.keySet())
                {
                    Table table = constraintTableMap.get(constraint);
                    String[] columNames = constraintColumnsMap.get(constraint);
                    List<String[]> columValues = constraintValuesMultimap.get(constraint);
                    QueryData nextQueryData = new QueryData(table, columNames, columValues);
                    nextQueryDataList.add(nextQueryData);
                }
                return nextQueryDataList;
            }
        }
        return null;
    }
    
    public static List<QueryData> getReferencingColumnsValues(DatabaseMetaData metaData, QueryData queryData, Map<Table, PrimaryKey>  pkMap, int depth) throws Exception {
        if(queryData != null && queryData.getTable() != null && queryData.getTable().getTableName() != null &&
            queryData.getColumnNames() != null && queryData.getColumnNames().length > 0 && 
            queryData.getValueList() != null && !queryData.getValueList().isEmpty())
        {
            StringBuilder sql = new StringBuilder("select * from ");
            sql.append(queryData.getTable().toString());
            sql.append(" where ");
            
            StringBuilder sqlCondition = new StringBuilder("(");
            for(String columnName : queryData.getColumnNames())
            {
                sqlCondition.append(columnName);
                sqlCondition.append(" = ? and ");
            }
            sqlCondition.delete(sqlCondition.length() - 5, sqlCondition.length());
            sqlCondition.append(") or ");
            
            for(String[] values : queryData.getValueList())
            {
                boolean areFilledReferencingColumnsValues = isFilledArray(values);
                if(areFilledReferencingColumnsValues)
                {
                    sql.append(sqlCondition);
                }
            }
            if(" or ".equals(sql.substring(sql.length() - 4, sql.length())))
            {
                sql.delete(sql.length() - 4, sql.length());
            }
            
            PreparedStatement statement = metaData.getConnection().prepareStatement(sql.toString());
            StringBuilder sqlParams = new StringBuilder();
            int i = 0;
            for(String[] values : queryData.getValueList())
            {
                boolean areFilledReferencingColumnsValues = isFilledArray(values);
                if(areFilledReferencingColumnsValues)
                {
                    sqlParams.append("[");
                    for(String value : values)
                    {
                        statement.setString(i+1, value);
                        sqlParams.append(value);
                        sqlParams.append(",");
                        i++;
                    }
                    sqlParams.deleteCharAt(sqlParams.length() - 1);
                    sqlParams.append("]");
                }
            }
            System.out.println("depth " + depth + ": " + sql.toString() + sqlParams.toString());
            ResultSet rs = statement.executeQuery();
            if(rs != null) 
            {
                PrimaryKey queryPrimaryKey = pkMap.get(queryData.getTable());
                List<String[]> queryPKValuesList = new ArrayList<String[]>();
                while (rs.next()) {
                    String[] queryPKValues = new String[queryPrimaryKey.getColumnNames().size()];
                    i = 0;
                    for(String queryPKColumnName : queryPrimaryKey.getColumnNames())
                    {
                        queryPKValues[i] = rs.getString(queryPKColumnName);
                        i++;
                    }
                    queryPKValuesList.add(queryPKValues);
                }
                SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> inversedFkMap = getInversedForeignKeysByTable(metaData, queryData.getTable());
                if(inversedFkMap != null)
                {
                    List<QueryData> nextQueryDataList = new ArrayList<QueryData>();
                    for(ForeignKeyConstraint inversedFKConstraint : inversedFkMap.keySet())
                    {
                        if(queryPrimaryKey.getConstraintName().equals(inversedFKConstraint.getConstraintName()))
                        {
                            Collection<ForeignKeyColumns> inversedFkColumnsList = inversedFkMap.get(inversedFKConstraint);
                            String[] relatedTableColumnNames = new String[queryPrimaryKey.getColumnNames().size()];

                            for(ForeignKeyColumns inversedFkColumns : inversedFkColumnsList)
                            {
                                relatedTableColumnNames[queryPrimaryKey.getColumnOrderMap().get(inversedFkColumns.getTableColumnName())] = inversedFkColumns.getRelatedTableColumnName();
                            }
                            QueryData nextQueryData = new QueryData(inversedFKConstraint.getRelatedTable(), relatedTableColumnNames, queryPKValuesList);
                            nextQueryDataList.add(nextQueryData);
                        }
                    }
                    return nextQueryDataList;
                }
            }
        }
        return null;
    }
    
    public static SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> getInversedForeignKeysByTable(DatabaseMetaData metaData, Table table) throws Exception {
        ResultSet foreignKeys = (table != null) ? metaData.getExportedKeys(table.getCatalog(), table.getSchema(), table.getTableName()) : null;
        if(foreignKeys != null && foreignKeys.next())
        {
            SortedSetMultimap<ForeignKeyConstraint, ForeignKeyColumns> inversedFkMultimap = TreeMultimap.create();
            do {
                Table pkTable = new Table(
                    foreignKeys.getString("PKTABLE_CAT"),
                    foreignKeys.getString("PKTABLE_SCHEM"),
                    foreignKeys.getString("PKTABLE_NAME"));
                String pkConstraintName = foreignKeys.getString("PK_NAME");
                Table fkTable = new Table(
                    foreignKeys.getString("FKTABLE_CAT"),
                    foreignKeys.getString("FKTABLE_SCHEM"),
                    foreignKeys.getString("FKTABLE_NAME"));
                ForeignKeyConstraint pkConstraint = new ForeignKeyConstraint(pkTable, pkConstraintName, fkTable);
                ForeignKeyColumns fkColumns = new ForeignKeyColumns(
                    foreignKeys.getString("PKCOLUMN_NAME"),
                    foreignKeys.getString("FKCOLUMN_NAME"));
                inversedFkMultimap.put(pkConstraint, fkColumns);
            } while (foreignKeys.next());
            SortedMap inversedFkMap = new TreeMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>>();
            inversedFkMap.putAll(inversedFkMultimap.asMap());
            return inversedFkMap;
        }
        return null;
    }
    
    public static void printPrimaryKeys(SortedMap<Table, PrimaryKey>  pkMap) {
        List<ResultPrimaryKey> pkList = getResultPrimaryKeys(pkMap);
        if(pkList != null)
        {
            for(ResultPrimaryKey pk : pkList)
            {
                System.out.println(pk);
            }
        }
    }
    
    public static List<ResultPrimaryKey> getResultPrimaryKeys(SortedMap<Table, PrimaryKey>  pkMap) {
        if(pkMap != null)
        {
            List<ResultPrimaryKey> pkList = new ArrayList<ResultPrimaryKey>();
            for(Map.Entry<Table, PrimaryKey> pkMapEntry : pkMap.entrySet())
            {
                ResultPrimaryKey resultPk = new ResultPrimaryKey(pkMapEntry.getValue());
                pkList.add(resultPk);
            }
            return pkList;
        }
        return null;
    }
    
    public static void printForeignKeys(SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap, SortedMap<Table, PrimaryKey> pkMap, SortedMap<String, Collection<String>> uniqueIndexMap) {
        List<ResultForeignKey> resultForeignKeyList = getResultForeignKeys(fkMap, pkMap, uniqueIndexMap);
        if(resultForeignKeyList != null)
        {
            for(ResultForeignKey resultForeignKey : resultForeignKeyList)
            {
                System.out.println(resultForeignKey);
            }
        }
    }
    
    public static List<ResultForeignKey> getResultForeignKeys(SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap, SortedMap<Table, PrimaryKey> pkMap, SortedMap<String, Collection<String>> uniqueIndexMap) {
        if(fkMap != null)
        {
            List<ResultForeignKey> resultForeignKeyList = new ArrayList<ResultForeignKey>();
            for(ForeignKeyConstraint fkConstraint : fkMap.keySet())
            {
                Collection<ForeignKeyColumns> fkColumnsList = fkMap.get(fkConstraint);
                
                StringBuilder fkColumnsStr = new StringBuilder();
                StringBuilder pkColumnsStr = new StringBuilder();
                if(fkColumnsList != null && !fkColumnsList.isEmpty())
                {
                    for(ForeignKeyColumns fkColumns : fkColumnsList)
                    {
                        fkColumnsStr.append(fkColumns.getTableColumnName());
                        fkColumnsStr.append(",");
                        pkColumnsStr.append(fkColumns.getRelatedTableColumnName());
                        pkColumnsStr.append(",");
                    }
                    fkColumnsStr.deleteCharAt(fkColumnsStr.length() - 1);
                    pkColumnsStr.deleteCharAt(pkColumnsStr.length() - 1);
                }
                
                StringBuilder stringBuilder = new StringBuilder(fkConstraint.getTable().toString());
                stringBuilder.append(fkColumnsStr);
                stringBuilder.append(" -> ");
                stringBuilder.append(fkConstraint.getRelatedTable().toString());
                stringBuilder.append(pkColumnsStr);
                PrimaryKey primaryKey = isPrimaryKey(fkConstraint.getTable(), fkColumnsList, pkMap);
                if(primaryKey != null)
                {
                    resultForeignKeyList.add(new ResultForeignKey("1:1", fkConstraint.getTable().toString(), fkColumnsStr.toString(), 
                        fkConstraint.getRelatedTable().toString(), pkColumnsStr.toString(), fkConstraint.getConstraintName(), primaryKey.getConstraintName()));
                }
                else
                {
                    String uniqueIndexConstraint = isUniqueIndex(fkConstraint.getTable(), fkColumnsList, uniqueIndexMap);
                    if(uniqueIndexConstraint != null)
                    {
                        resultForeignKeyList.add(new ResultForeignKey("1:1", fkConstraint.getTable().toString(), fkColumnsStr.toString(), 
                            fkConstraint.getRelatedTable().toString(), pkColumnsStr.toString(), fkConstraint.getConstraintName(), uniqueIndexConstraint.substring(fkConstraint.getTable().toString().length() + 3)));
                    }
                    else
                    {
                        resultForeignKeyList.add(new ResultForeignKey("N:1", fkConstraint.getTable().toString(), fkColumnsStr.toString(), 
                            fkConstraint.getRelatedTable().toString(), pkColumnsStr.toString(), fkConstraint.getConstraintName(), null));
                    }
                }
            }
            return resultForeignKeyList;
        }
        return null;
    }
    
    public static void printIndices(SortedMap<String, Collection<String>> indexMap) {
        List<ResultIndex> resultIndices = getResultIndices(indexMap);
        if(resultIndices != null)
        {
            for(ResultIndex resultIndex : resultIndices)
            {
                System.out.println(resultIndex);
            }
        }
    }
    
    public static List<ResultIndex> getResultIndices(SortedMap<String, Collection<String>> indexMap) {
        if(indexMap != null)
        {
            List<ResultIndex> resultIndices = new ArrayList<ResultIndex>();
            for(String indexConstraint : indexMap.keySet())
            {
                String[] indexConstraintAttributes = indexConstraint.split(" - ");
                
                StringBuilder stringBuilder = new StringBuilder(indexConstraintAttributes[0]);
                Collection<String> indexColumns = indexMap.get(indexConstraint);
                if(indexColumns != null && !indexColumns.isEmpty())
                {
                    for(String indexColumn : indexColumns)
                    {
                        stringBuilder.append(indexColumn);
                        stringBuilder.append(",");
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                resultIndices.add(
                    new ResultIndex(indexConstraintAttributes[0], stringBuilder.toString(), indexConstraintAttributes[1]));
            }
            return resultIndices;
        }
        return null;
    }
    
    public static void printEagerResultsQuery(DatabaseMetaData metaData, List<QueryData> queryDataList, int depth, int remainingDepth, SortedMap<ForeignKeyConstraint, Collection<ForeignKeyColumns>> fkMap) throws Exception {
        if(queryDataList != null && !queryDataList.isEmpty())
        {
            printObjectsFromList(queryDataList);
            if(remainingDepth > 0)
            {
                List<QueryData> nextQueryDataList = new ArrayList<QueryData>();
                for(QueryData queryData : queryDataList)
                {
                    List<QueryData> nextQueryDataListAux = getReferencedColumnsValues(metaData, queryData, fkMap, depth);
                    if(nextQueryDataListAux != null)
                    {
                        nextQueryDataList.addAll(nextQueryDataListAux);
                    }
                }
                printEagerResultsQuery(metaData, nextQueryDataList, depth + 1, remainingDepth - 1, fkMap);
            }
        }
    }
    
    public static void printIndirectReferences(DatabaseMetaData metaData, List<QueryData> queryDataList, int depth, int remainingDepth, Map<Table, PrimaryKey>  pkMap) throws Exception {
        if(queryDataList != null && !queryDataList.isEmpty())
        {
            printObjectsFromList(queryDataList);
            if(remainingDepth > 0)
            {
                List<QueryData> nextQueryDataList = new ArrayList<QueryData>();
                for(QueryData queryData : queryDataList)
                {
                    List<QueryData> nextQueryDataListAux = getReferencingColumnsValues(metaData, queryData, pkMap, depth);
                    if(nextQueryDataListAux != null)
                    {
                        nextQueryDataList.addAll(nextQueryDataListAux);
                    }
                }
                printIndirectReferences(metaData, nextQueryDataList, depth + 1, remainingDepth - 1, pkMap);
            }
        }
    }
    
    public static PrimaryKey isPrimaryKey(Table table, Collection<ForeignKeyColumns> fkColumnsList, SortedMap<Table, PrimaryKey> pkMap)
    {
        PrimaryKey primaryKey = pkMap.get(table);
        for(String columnName : primaryKey.getColumnNames())
        {
            boolean containsPkColumnName = false;
            for(ForeignKeyColumns fkColumns : fkColumnsList)
            {
                if(columnName.equals(fkColumns.getTableColumnName()))
                {
                    containsPkColumnName = true;
                }
            }
            if(!containsPkColumnName)
            {
                return null;
            }
        }
        return primaryKey;
    }
    
    public static String isUniqueIndex(Table table, Collection<ForeignKeyColumns> fkColumnsList, SortedMap<String, Collection<String>> uniqueIndexMap)
    {
        indexLoop:
        for(String uniqueIndexConstraint : uniqueIndexMap.keySet())
        {
            if(uniqueIndexConstraint.startsWith(table.toString() + " - "))
            {
                Collection<String> uniqueIndexColumns = uniqueIndexMap.get(uniqueIndexConstraint);
                for(String uniqueIndexColumn : uniqueIndexColumns)
                {
                    boolean containsUniqueIndexColumn = false;
                    for(ForeignKeyColumns fkColumns : fkColumnsList)
                    {
                        if(uniqueIndexColumn.equals(fkColumns.getTableColumnName()))
                        {
                            containsUniqueIndexColumn = true;
                        }
                    }
                    if(!containsUniqueIndexColumn)
                    {
                        continue indexLoop;
                    }
                }
                return uniqueIndexConstraint;
            }
        }
        return null;
    }
    
    private static <T> boolean isFilledArray(T[] array)
    {
        if(array == null)
        {
            for(T object : array)
            {
                if(object == null)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static <T> void printObjectsFromList(List<T> list) {
        if(list != null)
        {
            for(T object : list)
            {
                System.out.println(object);
            }
        }
    }
  
    public static DatabaseMetaData getConnectionMetaData() throws Exception {
        String url = "jdbc:derby://localhost:1527/exampleProjectDB";
        String username = "APP";
        String password = "123";

        Connection connection = DriverManager.getConnection(url, username, password);
        if(connection != null)
        {
            return connection.getMetaData();
        }
        return null;
    }
}
