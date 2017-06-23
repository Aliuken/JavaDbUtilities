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
public class ResultForeignKey {
    private final String cardinality;
    private final String referencingTable;
    private final String referencingFields;
    private final String referencedTable;
    private final String referencedFields;
    private final String constraintName;
    private final String uniqueConstraintName;

    public ResultForeignKey(String cardinality, String referencingTable, String referencingFields, String referencedTable, String referencedFields, String constraintName, String uniqueConstraintName) {
        this.cardinality = cardinality;
        this.referencingTable = referencingTable;
        this.referencingFields = referencingFields;
        this.referencedTable = referencedTable;
        this.referencedFields = referencedFields;
        this.constraintName = constraintName;
        this.uniqueConstraintName = uniqueConstraintName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(cardinality);
        stringBuilder.append(" - ");
        stringBuilder.append(referencingTable);
        stringBuilder.append("(");
        stringBuilder.append(referencingFields);
        stringBuilder.append(") -> ");
        stringBuilder.append(referencedTable);
        stringBuilder.append("(");
        stringBuilder.append(referencedFields);
        stringBuilder.append(") - ");
        stringBuilder.append(constraintName);
        stringBuilder.append(" - ");
        stringBuilder.append(uniqueConstraintName);
        return stringBuilder.toString();
    }

    public String getCardinality() {
        return cardinality;
    }

    public String getReferencingTable() {
        return referencingTable;
    }

    public String getReferencingFields() {
        return referencingFields;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedFields() {
        return referencedFields;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getUniqueConstraintName() {
        return uniqueConstraintName;
    }
    
}
