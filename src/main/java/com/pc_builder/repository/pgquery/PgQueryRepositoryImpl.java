package com.pc_builder.repository.pgquery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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

import com.pc_builder.payload.response.JsonResponse;
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

	class PgSettingsEntry implements Serializable {
		private static final long serialVersionUID = 5567160849202441039L;
		String settingName;

		public PgSettingsEntry(String settingName) {
			this.settingName = settingName;
		}

		public String getSettingName() {
			return settingName;
		}

		public void setSettingName(String settingName) {
			this.settingName = settingName;
		}

	}

	class PgSettingsDto implements Serializable {
		private static final long serialVersionUID = 8983180789548524629L;

		String title;
		String id;
		List<PgSettingsEntry> settings;
		List<PgSettingsDto> children;

		public PgSettingsDto(String title) {
			this.title = title;
			this.id = makeId(title);
			this.settings = new ArrayList<>();
			this.children = new ArrayList<>();
		}

		private String makeId(String title) {
			String id = new String(title);
			id = id.replaceAll(" ", "_");
			id = id.replaceAll("-", "_");
			id = "sect_" + id;
			return id.toLowerCase();
		}

		public String getTitle() {
			return title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<PgSettingsEntry> getSettings() {
			return settings;
		}

		public void setSettings(List<PgSettingsEntry> settings) {
			this.settings = settings;
		}

		public List<PgSettingsDto> getChildren() {
			return children;
		}

		public void setChildren(List<PgSettingsDto> children) {
			this.children = children;
		}

		public void addChild(PgSettingsDto e) {
			this.children.add(e);
		}

	}

	private List<String> getSubcategories(String categoryLhs) {
		StringBuilder query = new StringBuilder();
		query.append("select trim(both ' ' from split_part(category, '/', 2)) as category\n");
		query.append("from pg_settings\n");
		query.append("where category like '%/%' and category like '" + categoryLhs + "%'\n");
		query.append("group by 1\n");
		query.append("order by 1;\n");

		List<Map<String, Object>> q = jdbcTemplate.queryForList(query.toString());
		List<String> result = new ArrayList<>();

		for (Map<String, Object> m : q) {
			for (Entry<String, Object> e : m.entrySet()) {
				String category = e.getValue().toString().trim();
				result.add(category);
			}
		}

		return result;
	}

	private StringBuilder queryForFlatCategory(String forCategoryLike) {
		StringBuilder query = new StringBuilder();
		query.append("select name\n");
		query.append("from pg_settings\n");
		query.append("where category like '" + forCategoryLike + "%'\n");
		query.append("order by 1;\n");
		return query;
	}

	private StringBuilder queryForSubcategories(String lhs, String rhs) {
		StringBuilder query = new StringBuilder();
		query.append("select name\n");
		query.append("from pg_settings\n");
		query.append("where   category like '" + lhs + "%' \n");
		query.append("    and category like '%" + rhs + "'\n");
		query.append("order by 1;\n");

		return query;
	}

	private List<PgSettingsEntry> getSettingsValues(StringBuilder query) {

		List<PgSettingsEntry> result = new ArrayList<>();

		List<Map<String, Object>> q = jdbcTemplate.queryForList(query.toString());
		for (Map<String, Object> m : q) {
			for (Entry<String, Object> e : m.entrySet()) {
				PgSettingsEntry en = new PgSettingsEntry(e.getValue().toString());
				result.add(en);
			}
		}

		return result;
	}

	@Override
	public JsonResponse pgSettings() {
		StringBuilder query = new StringBuilder();
		query.append("select trim(both ' ' from split_part(category, '/', 1)) \n");
		query.append("from pg_settings \n");
		query.append("group by 1  \n");
		query.append("order by 1; \n");

		List<Map<String, Object>> q = jdbcTemplate.queryForList(query.toString());

		List<PgSettingsDto> settings = new ArrayList<>();

		for (Map<String, Object> m : q) {
			for (Entry<String, Object> e : m.entrySet()) {
				String category = e.getValue().toString();
				List<String> subcategories = getSubcategories(category);
				if (subcategories.isEmpty()) {
					List<PgSettingsEntry> set = getSettingsValues(queryForFlatCategory(category));
					PgSettingsDto dto = new PgSettingsDto(category);
					dto.setSettings(set);
					settings.add(dto);
				} else {
					PgSettingsDto groupedDto = new PgSettingsDto(category);
					for (String sub : subcategories) {
						List<PgSettingsEntry> set = getSettingsValues(queryForSubcategories(category, sub));
						PgSettingsDto childDto = new PgSettingsDto(sub);
						childDto.setSettings(set);
						groupedDto.addChild(childDto);
					}
					settings.add(groupedDto);
				}
			}
		}
		return new JsonResponse("ok", settings);
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
		// DatabaseMetaData databaseMetaData;
		// try {
		// databaseMetaData =
		// jdbcTemplate.getDataSource().getConnection().getMetaData();
		// try (ResultSet columns = databaseMetaData.getColumns(null, null, "auth_user",
		// null)) {
		// while (columns.next()) {
		// String columnName = columns.getString("COLUMN_NAME");
		// String columnSize = columns.getString("COLUMN_SIZE");
		// String datatype = columns.getString("DATA_TYPE");
		// String isNullable = columns.getString("IS_NULLABLE");
		// String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
		// System.out.println(columnName + " " + columnSize + " " + datatype + " " +
		// isNullable + " " + isAutoIncrement);
		// }
		// }
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
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
