package com.pc_builder.entity.pgquery;

import java.util.List;
import java.util.Map;

public class PgQueryResult {

	private List<String> headers;
	private List<List<String>> rows;

	public PgQueryResult() {
	}

	public PgQueryResult(List<String> headers, List<List<String>> rows) {
		this.headers = headers;
		this.rows = rows;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

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
	//
	// {ColumnName=ticket_no, TableName=ticket_flights, ColumnType=1,
	// ColumnClassName=java.lang.String, Precision=13, CatalogName=,
	// ColumnLabel=ticket_no, Scale=0, ColumnTypeName=bpchar, SchemaName=}
	// {ColumnName=flight_id, TableName=ticket_flights, ColumnType=4,
	// ColumnClassName=java.lang.Integer, Precision=10, CatalogName=,
	// ColumnLabel=flight_id, Scale=0, ColumnTypeName=int4, SchemaName=}
	// {ColumnName=fare_conditions, TableName=ticket_flights, ColumnType=12,
	// ColumnClassName=java.lang.String, Precision=10, CatalogName=,
	// ColumnLabel=fare_conditions, Scale=0, ColumnTypeName=varchar, SchemaName=}
	// {ColumnName=amount, TableName=ticket_flights, ColumnType=2,
	// ColumnClassName=java.math.BigDecimal, Precision=10, CatalogName=,
	// ColumnLabel=amount, Scale=2, ColumnTypeName=numeric, SchemaName=}
}
