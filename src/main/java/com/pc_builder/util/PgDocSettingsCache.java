package com.pc_builder.util;

import java.io.IOException;

public abstract class PgDocSettingsCache {

	private static PgDocSettingsList list = null;

	public static PgDocSettingsList readDocs() throws IOException {
		if (list == null) {
			list = new PgDocSettingsReader().readDocs();
		}
		return list;
	}

}
