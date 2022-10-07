package com.pc_builder.payload.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PgQueryResult {

    private String errorMessage;
    private List<Map<String, String>> fields;
    private List<Map<String, String>> rows;

    public PgQueryResult() {
        this.errorMessage = "";
        this.fields = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public PgQueryResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.fields = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public PgQueryResult(List<Map<String, String>> fields, List<Map<String, String>> rows) {
        this.errorMessage = "";
        this.fields = fields;
        this.rows = rows;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Map<String, String>> getFields() {
        return fields;
    }

    public void setFields(List<Map<String, String>> fields) {
        this.fields = fields;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

}
