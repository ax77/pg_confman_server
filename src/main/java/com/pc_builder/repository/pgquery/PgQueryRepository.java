package com.pc_builder.repository.pgquery;

import com.pc_builder.payload.response.PgQueryResult;

public interface PgQueryRepository {

	PgQueryResult doSomeQuery(String q);

}
