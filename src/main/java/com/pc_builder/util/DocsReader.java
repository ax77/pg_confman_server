package com.pc_builder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocsReader {

	class Dto implements Serializable {
		private static final long serialVersionUID = 8581194533134262353L;

		String title;
		List<PgSettingDocs> settings;
		List<Dto> sub;

		public Dto() {
			this.settings = new ArrayList<>();
			this.sub = new ArrayList<>();
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<PgSettingDocs> getSettings() {
			return settings;
		}

		public void setSettings(List<PgSettingDocs> settings) {
			this.settings = settings;
		}

		public List<Dto> getSub() {
			return sub;
		}

		public void setSub(List<Dto> sub) {
			this.sub = sub;
		}

		public void addSub(Dto e) {
			this.sub.add(e);
		}

	}

	private String readFilenameToText(String filename) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("doc/" + filename);

		String text = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
				.collect(Collectors.joining("\n"));

		is.close();
		return text;
	}

	String getTitle(Elements topChilds) {
		Element titleNode = topChilds.remove(0);
		if (!titleNode.hasClass("titlepage")) {
			throw new RuntimeException("expect titlepage");
		}
		String text = titleNode.text();
		text = text.substring(text.indexOf(' '));
		return text.trim();
	}

	private boolean hasSect2(Elements topChilds) {
		for (Element e : topChilds) {
			if (e.hasClass("sect2")) {
				return true;
			}
		}
		return false;
	}

	public List<PgSettingDocs> parseDocs() throws IOException {

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

		List<Dto> dtos = new ArrayList<>();
		List<PgSettingDocs> flat = new ArrayList<>();

		for (String filename : files) {
			Document doc = Jsoup.parse(readFilenameToText(filename));

			//////////////////////////////////////////////////////////////////////
			// section I loop
			Elements top = doc.select("div.sect1");
			if (top.size() != 1) {
				throw new RuntimeException("expect 1 node");
			}
			Elements topChilds = top.get(0).children();
			String title = getTitle(topChilds);

			Dto dto = new Dto();
			dto.setTitle(title);

			if (!hasSect2(topChilds)) {
				List<PgSettingDocs> settings = parseVarlist(topChilds);
				flat.addAll(settings);

				dto.setSettings(settings);
			}

			else {
				//////////////////////////////////////////////////////////////////////
				// section II loop

				for (Element e : topChilds) {
					if (!e.hasClass("sect2")) {
						continue;
					}
					Elements subChilds = e.children();
					title = getTitle(subChilds);

					List<PgSettingDocs> settings = parseVarlist(subChilds);
					flat.addAll(settings);

					Dto sub = new Dto();
					sub.setTitle(title);
					sub.setSettings(settings);
					dto.addSub(sub);
				}
			}

			dtos.add(dto);
		}

		return flat;

//		FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/docs.json");
//		ObjectMapper mapper = new ObjectMapper();
//		String res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flat);
//		fw.write(res + "\n");
//		fw.close();

	}

	private List<PgSettingDocs> parseVarlist(Elements subChilds) {

		List<PgSettingDocs> settings = new ArrayList<>();

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
					settings.add(new PgSettingDocs(settingName.get(0), settingDesc));
				} else {
					for (String s : settingName) {
						settings.add(new PgSettingDocs(s, new ArrayList<String>()));
					}
				}
			}
		}

		return settings;
	}
}
