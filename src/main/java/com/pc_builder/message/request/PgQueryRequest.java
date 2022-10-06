package com.pc_builder.message.request;

import javax.validation.constraints.NotBlank;

public class PgQueryRequest {
	@NotBlank
	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
