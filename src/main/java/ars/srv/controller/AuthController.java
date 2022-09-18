package ars.srv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 7200)
public class AuthController {

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return new ResponseEntity<>("that's fine", HttpStatus.OK);
	}

}
