package com.pc_builder.service.impl.pgquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc_builder.payload.response.JsonResponse;
import com.pc_builder.payload.response.PgQueryResult;
import com.pc_builder.repository.pgquery.PgQueryRepository;
import com.pc_builder.service.pgquery.PgQueryService;

@Service
public class PgQueryServiceImpl implements PgQueryService {

	private PgQueryRepository repository;

	@Autowired
	public PgQueryServiceImpl(PgQueryRepository theEmployeeRepository) {
		repository = theEmployeeRepository;
	}

	@Override
	public PgQueryResult execQuery(String q) {
		return repository.doSomeQuery(q);
	}

	@Override
	public JsonResponse pgSettings() {
		return repository.pgSettings();
	}

}
