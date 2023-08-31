package qu4lizz.factoryrest.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import qu4lizz.factoryrest.exceptions.ActivationNotApprovedException;
import qu4lizz.factoryrest.exceptions.BlockedUserException;
import qu4lizz.factoryrest.model.LoginDTO;
import qu4lizz.factoryrest.model.User;
import qu4lizz.factoryrest.service.UserService;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;

@Path("/users")
public class UserController {
    private final UserService service = new UserService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getUsers() {
        return service.getUsers();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam(value = "username") String username) {
        User student = service.getByUsername(username);
        if (student != null) {
            return Response.status(200).entity(student).build();
        } else {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        if (service.register(user)) {
            return Response.status(200).entity(user).build();
        } else {
            return Response.status(400).entity("User already exists").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO user) {
        try {
            User existingUser = service.login(user);
            return Response.status(200).entity(existingUser).build();
        }
        catch (CredentialException | BlockedUserException | ActivationNotApprovedException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

}
