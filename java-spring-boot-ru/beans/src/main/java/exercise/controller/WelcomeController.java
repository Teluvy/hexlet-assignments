package exercise.controller;

import exercise.daytime.Day;
import exercise.daytime.Daytime;
import exercise.daytime.Night;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

// BEGIN
@RestController
public class WelcomeController {

    private final Daytime daytime;

    public WelcomeController(Daytime daytime) {
        this.daytime = daytime;
    }

    @GetMapping("/welcome")
    public String getWelcome() {
        return "It is " + daytime.getName() + " now! Welcome to Spring!";
    }
}
// END
