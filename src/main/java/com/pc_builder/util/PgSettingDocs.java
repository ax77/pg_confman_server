package com.pc_builder.util;

import java.io.Serializable;
import java.util.List;

public class PgSettingDocs implements Serializable {
	private static final long serialVersionUID = 3477752014770789859L;

	final String settingName;
	final List<String> settingDesc;

	public PgSettingDocs(String settingName, List<String> settingDesc) {
		this.settingName = settingName;
		this.settingDesc = settingDesc;
	}

	public String getSettingName() {
		return settingName;
	}

	public List<String> getSettingDesc() {
		return settingDesc;
	}

	@Override
	public String toString() {
		return "Setting [settingName=" + settingName + ", settingDesc=" + settingDesc + "]";
	}

}