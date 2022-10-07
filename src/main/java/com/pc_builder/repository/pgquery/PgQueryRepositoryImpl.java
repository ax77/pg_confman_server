package com.pc_builder.repository.pgquery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import com.pc_builder.payload.response.PgQueryResult;

@Repository
public class PgQueryRepositoryImpl implements PgQueryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PgQueryRepositoryImpl(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplate = jdbcTemplateObject;
    }

    public void setTemplate(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = PgQueryRepositoryImpl.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    @Override
    public PgQueryResult doSomeQuery(String q) {
        // String q = getResourceFileAsString("queries/top_tables.sql");

        // Trying to execute the query given.
        List<Map<String, Object>> maybe = new ArrayList<Map<String, Object>>();
        try {
            maybe = jdbcTemplate.queryForList(q);
        } catch (Exception e) {
            return new PgQueryResult(e.getMessage());
        }

        // Let's collect all of the column names
        // In the case when the result-set is empty we need this additional query to a
        // database.
        List<Map<String, String>> fields = new ArrayList<>();
        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(q);
            SqlRowSetMetaData metaData = rowSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                Map<String, String> fieldMap = new LinkedHashMap<String, String>();
                fieldMap.put("ColumnName", metaData.getColumnName(i));
                fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
                fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
                fieldMap.put("CatalogName", metaData.getCatalogName(i));
                fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
                fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
                fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
                fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
                fieldMap.put("SchemaName", metaData.getSchemaName(i));
                fieldMap.put("TableName", metaData.getTableName(i));
                fieldMap.put("SchemaName", metaData.getSchemaName(i));
                fields.add(fieldMap);
            }

        } catch (Exception e) {
            return new PgQueryResult(e.getMessage());
        }

        // meta-info
        //        DatabaseMetaData databaseMetaData;
        //        try {
        //            databaseMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        //            try (ResultSet columns = databaseMetaData.getColumns(null, null, "auth_user", null)) {
        //                while (columns.next()) {
        //                    String columnName = columns.getString("COLUMN_NAME");
        //                    String columnSize = columns.getString("COLUMN_SIZE");
        //                    String datatype = columns.getString("DATA_TYPE");
        //                    String isNullable = columns.getString("IS_NULLABLE");
        //                    String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
        //                    System.out.println(columnName + " " + columnSize + " " + datatype + " " + isNullable + " " + isAutoIncrement);
        //                }
        //            }
        //        } catch (SQLException e1) {
        //            e1.printStackTrace();
        //        }
        //

        // The data itself
        List<Map<String, String>> rows = new ArrayList<>();
        for (Map<String, Object> row : maybe) {
            Map<String, String> r = new LinkedHashMap<>();

            // Where key is a column name, and the value is the cell data.
            for (Entry<String, Object> e : row.entrySet()) {
                // TODO: NULL semantic
                final String value = e.getValue() == null ? "" : e.getValue().toString();
                r.put(e.getKey(), value);
            }
            rows.add(r);
        }

        return new PgQueryResult(fields, rows);

    }

}
