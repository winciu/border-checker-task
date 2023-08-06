package pl.rationalworks.borderchecker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.rationalworks.borderchecker.controller.RoutingController;
import pl.rationalworks.borderchecker.model.CountryRoute;
import pl.rationalworks.borderchecker.service.RoutingService;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoutingController.class)
public class RoutingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoutingService routingService;

    @Test
    void shouldReturnAListWithCorrectCountryLabels() throws Exception {
        given(routingService.findShortestRoute("CZE", "ITA"))
                .willReturn(new CountryRoute(new String[]{"CZE", "AUT", "ITA"}));

        mvc.perform(get("/routing/CZE/ITA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("route", hasSize(3)))
                .andExpect(jsonPath("route", contains("CZE", "AUT", "ITA")));
    }

    @Test
    void shouldReturnEmptyRouteAndCorrectStatusWhenThereIsNoRoute() throws Exception {
        given(routingService.findShortestRoute("CZE", "XYZ"))
                .willReturn(new CountryRoute(new String[0]));

        mvc.perform(get("/routing/CZE/XYZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("route").isArray())
                .andExpect(jsonPath("route").isEmpty());
    }
}
