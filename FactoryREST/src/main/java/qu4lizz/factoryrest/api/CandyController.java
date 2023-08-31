package qu4lizz.factoryrest.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import qu4lizz.factoryrest.model.Candy;
import qu4lizz.factoryrest.service.CandyService;

import java.util.ArrayList;

@Path("/candies")
public class CandyController {
    private final CandyService candyService = new CandyService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Candy> getItems() {
        return candyService.getCandies();
    }
}
