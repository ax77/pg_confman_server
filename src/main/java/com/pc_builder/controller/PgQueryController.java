package com.pc_builder.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pc_builder.payload.request.PgQueryRequest;
import com.pc_builder.payload.response.JsonResponse;
import com.pc_builder.payload.response.PgQueryResult;
import com.pc_builder.service.pgquery.PgQueryService;

@RestController
@RequestMapping("/api/v1/queries/")
@CrossOrigin(origins = "*", maxAge = 7200)
public class PgQueryController {

	private PgQueryService pgService;

	@Autowired
	public PgQueryController(PgQueryService pgService) {
		this.pgService = pgService;
	}

	@PostMapping("execute")
	public ResponseEntity<?> executeQuery(@Valid @RequestBody PgQueryRequest req) {
		PgQueryResult row = pgService.execQuery(req.getQuery());
		return ResponseEntity.ok(new JsonResponse("ok", row));
	}

	@PostMapping("settings")
	public ResponseEntity<?> settings() {
		return ResponseEntity.ok(pgService.pgSettings());
	}

}
