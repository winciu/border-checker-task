package pl.rationalworks.borderchecker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rationalworks.borderchecker.model.CountryRoute;
import pl.rationalworks.borderchecker.service.RoutingService;

import java.io.IOException;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<CountryRoute> findRoute(@PathVariable String origin, @PathVariable String destination) throws IOException {
        CountryRoute route = routingService.findShortestRoute(origin, destination);
        if (route.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(route);
        }

        return ResponseEntity.ok(route);
    }
}
