package com.pc_builder.repository.pgquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgSettingsDto implements Serializable {
    private static final long serialVersionUID = 8983180789548524629L;

    private String title;
    private String id;
    private List<PgSettingsEntry> settings;
    private List<PgSettingsDto> children;

    public PgSettingsDto(String title) {
        this.title = title;
        this.id = UUID.randomUUID().toString();
        this.settings = new ArrayList<>();
        this.children = new ArrayList<>();
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
