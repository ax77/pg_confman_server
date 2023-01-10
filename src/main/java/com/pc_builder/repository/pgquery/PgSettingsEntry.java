package com.pc_builder.repository.pgquery;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class PgSettingsEntry implements Serializable {
    private static final long serialVersionUID = 5567160849202441039L;

    String name;
    String setting;
    String unit;
    String category;
    String short_desc;
    String extra_desc;
    String context;
    String vartype;
    String source;
    String min_val;
    String max_val;
    String boot_val;
    String reset_val;
    String sourcefile;
    String sourceline;
    boolean pending_restart;

    List<String> docsEn = new ArrayList<>();
    List<String> docsRu = new ArrayList<>();
    String enumvalues;
    List<PgSettingsExtra> printableSettingsValues = new ArrayList<>();

    public List<PgSettingsExtra> getPrintableSettingsValues() {
        return printableSettingsValues;
    }

    public void setPrintableSettingsValues(List<PgSettingsExtra> printableSettingsValues) {
        this.printableSettingsValues = printableSettingsValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getExtra_desc() {
        return extra_desc;
    }

    public void setExtra_desc(String extra_desc) {
        this.extra_desc = extra_desc;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getVartype() {
        return vartype;
    }

    public void setVartype(String vartype) {
        this.vartype = vartype;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMin_val() {
        return min_val;
    }

    public void setMin_val(String min_val) {
        this.min_val = min_val;
    }

    public String getMax_val() {
        return max_val;
    }

    public void setMax_val(String max_val) {
        this.max_val = max_val;
    }

    public String getBoot_val() {
        return boot_val;
    }

    public void setBoot_val(String boot_val) {
        this.boot_val = boot_val;
    }

    public String getReset_val() {
        return reset_val;
    }

    public void setReset_val(String reset_val) {
        this.reset_val = reset_val;
    }

    public String getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(String sourcefile) {
        this.sourcefile = sourcefile;
    }

    public String getSourceline() {
        return sourceline;
    }

    public void setSourceline(String sourceline) {
        this.sourceline = sourceline;
    }

    public boolean isPending_restart() {
        return pending_restart;
    }

    public void setPending_restart(boolean pending_restart) {
        this.pending_restart = pending_restart;
    }

    public List<String> getDocsEn() {
        return docsEn;
    }

    public void setDocsEn(List<String> docsEn) {
        this.docsEn = docsEn;
    }

    public List<String> getDocsRu() {
        return docsRu;
    }

    public void setDocsRu(List<String> docsRu) {
        this.docsRu = docsRu;
    }

    public String getEnumvalues() {
        return enumvalues;
    }

    public void setEnumvalues(String enumvalues) {
        this.enumvalues = enumvalues;
    }

    /// custom

    public void addDocsEn(String e) {
        this.docsEn.add(e);
    }

    public void addDocsRu(String e) {
        this.docsRu.add(e);
    }

}