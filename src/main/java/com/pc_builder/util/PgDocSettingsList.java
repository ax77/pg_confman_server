package com.pc_builder.util;

import java.io.Serializable;
import java.util.List;

public class PgDocSettingsList implements Serializable {
	private static final long serialVersionUID = 2480625773233293436L;

	private final List<PgDocSettingsEntry> list;

	public PgDocSettingsList(List<PgDocSettingsEntry> list) {
		this.list = list;
	}

	public List<PgDocSettingsEntry> getList() {
		return list;
	}

	public PgDocSettingsEntry find(String bySettingName) {
		for (PgDocSettingsEntry e : list) {
			if (e.getSettingName().equals(bySettingName)) {
				return e;
			}
		}
		return null;
	}
}
