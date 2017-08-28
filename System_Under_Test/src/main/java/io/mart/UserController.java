package io.mart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mart.dto.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Controller
@Api(value = "users", description = "Endpoint for users management", tags = "Users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(nickname = "createUser", value = "Create an user", notes = "", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created user")
    })
    public User createUser(@RequestBody User user) {
        return repository.createUsers(user);
    }

    @ApiOperation(nickname = "getAllUsers", value = "Gets all users", notes = "", response = User.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of all users")
    })
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Collection<User> allUsers() {
        return repository.allUsers();
    }

    @RequestMapping(value = "/user/{name}", method = RequestMethod.GET)
    @ApiOperation(nickname = "getUserByName", value = "Get user by name", notes = "", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Found user by given name")
    })
    @ResponseBody
    public User getUserByName(@PathVariable("name") String name) {
        return repository.getByName(name);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    @ApiOperation(nickname = "deleteUsers", value = "Delete all users", notes = "", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "All Users deleted")
    })
    @ResponseBody
    public String deleteUsers() {
        repository.deleteUsers();
        return "All users deleted";
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(nickname = "notifyUser", value = "Send push notification to user", notes = "", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "sent")
    })
    public User notifyUser(@RequestBody User user) throws IOException {
        Request
                .Post("http://localhost:8090/notify")
                .bodyString(mapper.writeValueAsString(user), ContentType.APPLICATION_JSON)
                .execute();
        return user;
    }
}
