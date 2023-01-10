package com.pc_builder.repository.pgquery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import com.pc_builder.payload.response.JsonResponse;
import com.pc_builder.payload.response.PgQueryResult;
import com.pc_builder.util.PgDocSettingsCache;
import com.pc_builder.util.PgDocSettingsEntry;
import com.pc_builder.util.PgDocSettingsList;

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
        query.append("select *\n");
        query.append("from pg_settings\n");
        query.append("where category like '" + forCategoryLike + "%'\n");
        query.append("order by 1;\n");
        return query;
    }

    private StringBuilder queryForSubcategories(String lhs, String rhs) {
        StringBuilder query = new StringBuilder();
        query.append("select *\n");
        query.append("from pg_settings\n");
        query.append("where   category like '" + lhs + "%' \n");
        query.append("    and category like '%" + rhs + "'\n");
        query.append("order by 1;\n");

        return query;
    }

    private void applySetting(List<PgSettingsExtra> printableSettingsValues, String name, Object o) {
        if (o != null && o.toString().trim().length() > 0) {
            printableSettingsValues.add(new PgSettingsExtra(name, o.toString()));
        }
    }

    private List<PgSettingsEntry> getSettingsValues(StringBuilder query) {

        List<PgSettingsEntry> result = jdbcTemplate.query(query.toString(),
                BeanPropertyRowMapper.newInstance(PgSettingsEntry.class));

        for (PgSettingsEntry e : result) {
            String q = "select enumvals from pg_settings where enumvals is not null AND name = '" + e.getName() + "';";
            List<Map<String, Object>> res = jdbcTemplate.queryForList(q);
            if (res.size() == 1) {
                Map<String, Object> ent = res.get(0);
                String enumvals = "";
                Object o = ent.get("enumvals");
                if (o != null) {
                    enumvals = o.toString();
                }
                e.setEnumvalues(enumvals);
            }
        }

        //vartype:
        //
        //enum   
        //string 
        //bool   
        //integer
        //real   

        for (PgSettingsEntry e : result) {
            e.addDocsEn(e.getShort_desc());
            e.addDocsRu(e.getShort_desc());

            List<PgSettingsExtra> printableSettingsValues = new ArrayList<>();

            applySetting(printableSettingsValues, "unit", e.getUnit());
            applySetting(printableSettingsValues, "context", e.getContext());
            applySetting(printableSettingsValues, "source", e.getSource());
            applySetting(printableSettingsValues, "vartype", e.getVartype());
            applySetting(printableSettingsValues, "min_val", e.getMin_val());
            applySetting(printableSettingsValues, "max_val", e.getMax_val());
            applySetting(printableSettingsValues, "boot_val", e.getBoot_val());
            applySetting(printableSettingsValues, "reset_val", e.getReset_val());
            applySetting(printableSettingsValues, "sourcefile", e.getSourcefile());
            applySetting(printableSettingsValues, "enumvals", e.getEnumvalues());

            e.setPrintableSettingsValues(printableSettingsValues);
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
                    PgSettingsDto groupedDto = new PgSettingsDto(category);

                    List<PgSettingsEntry> set = getSettingsValues(queryForFlatCategory(category));
                    PgSettingsDto dto = new PgSettingsDto(category);
                    dto.setSettings(set);

                    groupedDto.addChild(dto);
                    settings.add(groupedDto);
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

        try {
            PgDocSettingsList list = PgDocSettingsCache.readDocs();
            for (PgSettingsDto e : settings) {
                if (!e.getChildren().isEmpty()) {
                    for (PgSettingsDto child : e.getChildren()) {
                        applyParsedDocs(list, child);
                    }
                } else {
                    applyParsedDocs(list, e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonResponse("ok", settings);
    }

    private void applyParsedDocs(PgDocSettingsList list, PgSettingsDto child) {
        for (PgSettingsEntry realSetting : child.getSettings()) {
            PgDocSettingsEntry parsed = list.find(realSetting.getName());
            if (parsed != null) {
                realSetting.getDocsEn().addAll(parsed.getSettingDescEn());
                realSetting.getDocsRu().addAll(parsed.getSettingDescRu());
            }
        }
    }

    @Override
    public PgQueryResult doSomeQuery(String q) {
        // String q = getResourceFileAsString("queries/top_tables.sql");

        String res = new BasicFormatterImpl().format(q);
        System.out.println(res);

        // Trying to execute the query given.
        List<Map<String, Object>> maybe = new ArrayList<Map<String, Object>>();
        try {
            maybe = jdbcTemplate.queryForList(q);
        } catch (Exception e) {
            String message = e.getMessage();
            return new PgQueryResult(message);
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
