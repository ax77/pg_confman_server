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

import com.pc_builder.entity.pgquery.PgQueryResult;

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

		List<Map<String, Object>> maybe = new ArrayList<Map<String, Object>>();
		try {
			maybe = jdbcTemplate.queryForList(q);
		} catch (Exception e) {
			System.out.println("THE ERROR IS 1: " + e.toString());
		}

		List<String> headers = getHeaders(q);

		List<List<String>> rows = new ArrayList<>();

		for (Map<String, Object> row : maybe) {
			List<String> r= new ArrayList<>();
			for (Entry<String, Object> e : row.entrySet()) {
				final String value = e.getValue() == null ? "" : e.getValue().toString();
				r.add(value);
			}
			rows.add(r);
		}

		return new PgQueryResult(headers, rows);

	}

	List<String> getHeaders(String query) {
		List<String> headers = new ArrayList<String>();

		try {

			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);
			SqlRowSetMetaData metaData = rowSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				headers.add(metaData.getColumnName(i));
			}

		} catch (Exception e) {
			System.out.println("THE ERROR IS 2: " + e.toString());
		}

		return headers;
	}

	// void meta1() {
	// SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from
	// bookings.ticket_flights limit 32;");
	// SqlRowSetMetaData metaData = rowSet.getMetaData();
	// int columnCount = metaData.getColumnCount();
	// for (int i = 1; i <= columnCount; i++) {
	// Map<String, String> fieldMap = new HashMap<String, String>();
	// fieldMap.put("ColumnName", metaData.getColumnName(i));
	// fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
	// fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
	// fieldMap.put("CatalogName", metaData.getCatalogName(i));
	// fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
	// fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
	// fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
	// fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
	// fieldMap.put("SchemaName", metaData.getSchemaName(i));
	// fieldMap.put("TableName", metaData.getTableName(i));
	// fieldMap.put("SchemaName", metaData.getSchemaName(i));
	// System.out.println(fieldMap);
	// }
	// }
	//
	// void meta2() {
	// String sql = "select * from bookings.ticket_flights limit 32;";
	// RowCountCallbackHandler rcch = new RowCountCallbackHandler();
	// jdbcTemplate.query(sql, rcch);
	//
	// final String[] names = rcch.getColumnNames();
	// final int[] types = rcch.getColumnTypes();
	//
	// for (int i = 0; i < rcch.getColumnCount(); i++) {
	// System.out.printf("name=%s, type=%s\n", names[i], getTypeName(types[i]));
	// }
	// }
	//
	// String getTypeName(int type) {
	// switch (type) {
	// case Types.ARRAY:
	// break;
	// case Types.BIGINT:
	// return "BIGINT";
	// case Types.BINARY:
	// return "BINARY";
	// case Types.BIT:
	// return "BIT";
	// case Types.BLOB:
	// return "BLOB";
	// case Types.BOOLEAN:
	// return "BOOLEAN";
	// case Types.VARCHAR:
	// return "varchar";
	// }
	// return "?";
	// }

}
