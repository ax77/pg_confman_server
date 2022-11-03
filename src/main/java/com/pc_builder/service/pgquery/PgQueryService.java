package com.pc_builder.service.pgquery;

import com.pc_builder.payload.response.JsonResponse;
import com.pc_builder.payload.response.PgQueryResult;

public interface PgQueryService {

	public PgQueryResult execQuery(String q);

	public JsonResponse pgSettings();
}
