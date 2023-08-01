package account.controller;

import account.log.SecurityLogDTO;
import account.log.SecurityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityEventsController {
	@Autowired
	SecurityLogService logService;

	@GetMapping("api/security/events/")
	ResponseEntity<List<SecurityLogDTO>> getEvents(){
		return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
	}
}
