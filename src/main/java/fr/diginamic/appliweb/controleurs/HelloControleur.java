package fr.diginamic.appliweb.controleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/hello")
public class HelloControleur {

    private final HelloService helloService;

    @Autowired
    public HelloControleur(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping
    public String direHello() {
        return helloService.salutations();
    }
}


