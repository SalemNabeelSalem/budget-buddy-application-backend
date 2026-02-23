package isalem.dev.budget_buddy.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status", "/health"})
public class HomeController {

    @GetMapping
    public String healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).body(
                "<h1 style='color: green; text-align: center; margin-top: 50px;'>" +
                    "budget buddy is up and running🆗" +
                "</h1>"
        ).getBody();
    }
}