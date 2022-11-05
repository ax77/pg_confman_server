package com.pc_builder.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PgDocSettingsEntry implements Serializable {
	private static final long serialVersionUID = 3477752014770789859L;

	final String settingName;
	final List<String> settingDescEn;
	List<String> settingDescRu;

	public PgDocSettingsEntry(String settingName, List<String> settingDescEn) {
		this.settingName = settingName;
		this.settingDescEn = settingDescEn;
		this.settingDescRu = new ArrayList<String>();
	}

	public String getSettingName() {
		return settingName;
	}

	public List<String> getSettingDescEn() {
		return settingDescEn;
	}

	public List<String> getSettingDescRu() {
		return settingDescRu;
	}

	public void setSettingDescRu(List<String> settingDescRu) {
		this.settingDescRu = settingDescRu;
	}

	@Override
	public String toString() {
		return "PgDocSettingsEntry [settingName=" + settingName + ", settingDescEn=" + settingDescEn
				+ ", settingDescRu=" + settingDescRu + "]";
	}

}
