package com.pc_builder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PgDocSettingsReader {

	private String readFilenameToText(String filename, String lang) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("doc/14/" + lang + "/" + filename);

		String text = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
				.collect(Collectors.joining("\n"));

		is.close();
		return text;
	}

	private boolean hasSect2(Elements topChilds) {
		for (Element e : topChilds) {
			if (e.hasClass("sect2")) {
				return true;
			}
		}
		return false;
	}

	public PgDocSettingsList readDocs() throws IOException {

		PgDocSettingsList listEn = readSettings("en");
		PgDocSettingsList listRu = readSettings("ru");

		for (PgDocSettingsEntry e : listEn.getList()) {
			PgDocSettingsEntry ru = listRu.find(e.getSettingName());
			if (ru != null) {
				e.setSettingDescRu(ru.getSettingDescEn());
			}
		}

		return listEn;

	}

	private List<String> getFilenames() {
		List<String> files = new ArrayList<String>();
		files.add("ch20s02.html");
		files.add("ch20s03.html");
		files.add("ch20s04.html");
		files.add("ch20s05.html");
		files.add("ch20s06.html");
		files.add("ch20s07.html");
		files.add("ch20s08.html");
		files.add("ch20s09.html");
		files.add("ch20s10.html");
		files.add("ch20s11.html");
		files.add("ch20s12.html");
		files.add("ch20s13.html");
		files.add("ch20s14.html");
		files.add("ch20s15.html");
		files.add("ch20s16.html");
		files.add("ch20s17.html");
		return files;
	}

	private PgDocSettingsList readSettings(String lang) throws IOException {

		List<String> files = getFilenames();
		List<PgDocSettingsEntry> flat = new ArrayList<PgDocSettingsEntry>();

		for (String filename : files) {
			Document doc = Jsoup.parse(readFilenameToText(filename, lang));

			//////////////////////////////////////////////////////////////////////
			// section I loop
			Elements top = doc.select("div.sect1");
			if (top.size() != 1) {
				throw new RuntimeException("expect 1 node");
			}
			Elements topChilds = top.get(0).children();

			if (!hasSect2(topChilds)) {
				List<PgDocSettingsEntry> settings = parseVarlist(topChilds);
				flat.addAll(settings);
			}

			else {
				//////////////////////////////////////////////////////////////////////
				// section II loop

				for (Element e : topChilds) {
					if (!e.hasClass("sect2")) {
						continue;
					}
					List<PgDocSettingsEntry> settings = parseVarlist(e.children());
					flat.addAll(settings);
				}
			}

		}

		return new PgDocSettingsList(flat);
	}

	private List<PgDocSettingsEntry> parseVarlist(Elements subChilds) {

		List<PgDocSettingsEntry> settings = new ArrayList<PgDocSettingsEntry>();

		for (Element settingNode : subChilds.select("dl.variablelist")) {
			if (!settingNode.tagName().equals("dl")) {
				throw new RuntimeException("unknown: " + settingNode.tagName());
			}

			Elements x = settingNode.children();
			while (!x.isEmpty()) {
				Element first = x.remove(0);
				if (!first.tagName().equals("dt")) {
					throw new RuntimeException("unknown: " + first.tagName());
				}

				Element second = x.remove(0);
				if (!second.tagName().equals("dd")) {
					throw new RuntimeException("unknown: " + second.tagName());
				}

				// name
				List<String> settingName = new ArrayList<String>();
				for (Element v : first.select("code.varname")) {
					String name = v.text().trim();
					if (!name.isEmpty()) {
						settingName.add(name);
					}
				}

				// description
				List<String> settingDesc = new ArrayList<String>();
				for (Element fromTheSecond : second.children()) {
					if (!fromTheSecond.tagName().equals("p")) {
						continue;
					}
					settingDesc.add(fromTheSecond.text().trim());
				}

				// we will ignore all groups of settings.
				// that's fine, because there are few groups like this -> debug_print_ and
				// _stats...
				if (settingName.size() == 1) {
					settings.add(new PgDocSettingsEntry(settingName.get(0), settingDesc));
				} else {
					for (String s : settingName) {
						settings.add(new PgDocSettingsEntry(s, new ArrayList<String>()));
					}
				}
			}
		}

		return settings;
	}

}
