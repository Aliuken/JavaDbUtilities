# JavaDbUtilities
Java project to get metadata from a database using JDBC.

The file JavaDbUtilities.docx explains in Spanish how to use this project.

I have written this project as the following answer in StackOverflow:

"I have created a project called JavaDbUtilities on GitHub. The project uses JDBC's DatabaseMetaData and it can print:
- All the primary keys or the primary key of a given table.
- All the foreign keys or the foreign keys of a given table. It also retrieves the cardinality of every relationship, which is useful to create the entity-relationship model of a given database.
- All the indices or the indices of a given table.
- All the unique indices or the unique indices of a given table.
- The records a given query or some given queries would return if an eager strategy is used on every foreign key. Useful to understand how ORMs like JPA/Hibernate work.
- The records that reference the resulting records from a given query or some given queries. Useful to know the impact of updating/deleting those resulting records."

This answer was deleted however from the following questions:
- Easiest way to obtain database metadata in Java? (https://stackoverflow.com/questions/985010/easiest-way-to-obtain-database-metadata-in-java/)
- Understanding the methods of DatabaseMetaData (https://stackoverflow.com/questions/11730372/understanding-the-methods-of-databasemetadata/)
- How to get tables and views using DatabaseMetaData in JDBC (https://stackoverflow.com/questions/31859440/how-to-get-tables-and-views-using-databasemetadata-in-jdbc/)
- Generate ER diagram from postgresql database [SchemaSpy] (https://stackoverflow.com/questions/16399078/generate-er-diagram-from-postgresql-database-schemaspy/)
- Auto Generate Database Diagram MySQL [closed] (https://stackoverflow.com/questions/2488/auto-generate-database-diagram-mysql/)
- Is there a database modelling library for Java? (https://stackoverflow.com/questions/290163/is-there-a-database-modelling-library-for-java/)
- Tool to generate a ERD (entity-relation diagram) based on JPA annotations (https://stackoverflow.com/questions/10187048/tool-to-generate-a-erd-entity-relation-diagram-based-on-jpa-annotations/)
- Draw Database Schema based on JPA entities in IntelliJ (https://stackoverflow.com/questions/6867535/draw-database-schema-based-on-jpa-entities-in-intellij/)
- Tools to generate a database diagram/ER diagram from existing Oracle database? [closed] (https://stackoverflow.com/questions/2091599/tools-to-generate-a-database-diagram-er-diagram-from-existing-oracle-database/)
- Generate an E-R Diagram by reverse-engineering a database [closed] (https://stackoverflow.com/questions/185967/generate-an-e-r-diagram-by-reverse-engineering-a-database/)
- Generate table relationship diagram from existing schema (SQL Server) [closed] (https://stackoverflow.com/questions/168724/generate-table-relationship-diagram-from-existing-schema-sql-server/)
- JDBC get the relation+attribute referenced by a foreign key (https://stackoverflow.com/questions/11669341/jdbc-get-the-relationattribute-referenced-by-a-foreign-key/)
- Required Java API to generate Database ER diagram (https://stackoverflow.com/questions/9614957/required-java-api-to-generate-database-er-diagram/)
- Derby Entity Relationship Diagram (https://stackoverflow.com/questions/1018265/derby-entity-relationship-diagram/)
- How to get database metadata from entity manager (https://stackoverflow.com/questions/10607196/how-to-get-database-metadata-from-entity-manager/)
- Get Database VIEW metadata via JDBC (https://stackoverflow.com/questions/25315284/get-database-view-metadata-via-jdbc/)
- Can't get foreign key using JAVA DatabaseMetaData (https://stackoverflow.com/questions/21570748/cant-get-foreign-key-using-java-databasemetadata/)
- Retrieve all Indexes for a given Table with JDBC (https://stackoverflow.com/questions/21001561/retrieve-all-indexes-for-a-given-table-with-jdbc)
- How to get primary keys for all tables in JDBC? (https://stackoverflow.com/questions/40841078/how-to-get-primary-keys-for-all-tables-in-jdbc/)
- get all indexes declared in database from java (https://stackoverflow.com/questions/3656166/get-all-indexes-declared-in-database-from-java/)
- Getting a table's metadata without using resultset in java (https://stackoverflow.com/questions/12599412/getting-a-tables-metadata-without-using-resultset-in-java/)
- Get all the index information for a given schema — db2 (https://stackoverflow.com/questions/19255349/get-all-the-index-information-for-a-given-schema-db2/)
