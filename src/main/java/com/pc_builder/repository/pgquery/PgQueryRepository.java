package com.pc_builder.repository.pgquery;

import com.pc_builder.entity.pgquery.PgQueryResult;

public interface PgQueryRepository {

	PgQueryResult doSomeQuery(String q);

}
