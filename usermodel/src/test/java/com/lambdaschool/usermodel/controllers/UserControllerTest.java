package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {

        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);

//        roleList.add(r1);
//        roleList.add(r2);
//        roleList.add(r3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.setUserid(11);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .get(0)
                .setUseremailid(20);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails()
                .get(1)
                .setUseremailid(21);

        userList.add(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.setUserid(12);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .get(0)
                .setUseremailid(30);

        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .get(1)
                .setUseremailid(31);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails()
                .get(2)
                .setUseremailid(32);
        userList.add(u2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.setUserid(13);
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails()
                .get(0)
                .setUseremailid(40);
        userList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.getRoles().add(new UserRoles(u4, r2));
        u4.setUserid(14);
        userList.add(u4);

        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.getRoles().add(new UserRoles(u5, r2));
        u5.setUserid(15);
        userList.add(u5);

//        System.out.println("\n**** BEFORE ****");
//        for (User u : userList) {
//            System.out.println(u.getUserid() + " " + u.getUsername());
//        }
    }

    @After
    public void tearDown() throws Exception {

//        System.out.println("\n**** AFTER ****");
//        for (User u : userList) {
//            System.out.println(u.getUserid() + " " + u.getUsername());
//        }
    }

    @Test
    public void listAllUsers() throws Exception {

        String apiUrl = "/users/users";
        Mockito.when(userService.findAll())
                .thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("REST API returns list", er, tr);
    }

    @Test
    public void getUserById() throws Exception {

        String apiUrl = "/users/user/13";
        Mockito.when(userService.findUserById(13))
                .thenReturn(userList.get(2));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(2));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("REST API returns object", er, tr);
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        String apiUrl = "/users/user/5000";
        Mockito.when(userService.findUserById(5000))
                .thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("REST API returns object: ", er, tr);
    }

    @Test
    public void getUserByName() throws Exception {

        String apiUrl = "/users/user/name/barnbarn";
        Mockito.when(userService.findByName("barnbarn"))
                .thenReturn(userList.get(2));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(2));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("REST API returns object", er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception {

        String apiUrl = "/users/user/name/like/tt";

        List<User> filteredList = userList.stream()
                .filter(u -> u.getUsername().contains("tt"))
                .collect(Collectors.toList());

        Mockito.when(userService.findByNameContaining("tt"))
                .thenReturn(filteredList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(filteredList);

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("REST API returns list", er, tr);
    }

    @Test
    public void addNewUser() throws Exception {

        String apiUrl = "/users/user";

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User user = new User("happy",
                "password",
                "happy@example.com");
        user.getRoles().add(new UserRoles(user, r1));
        user.getRoles().add(new UserRoles(user, r2));
        user.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(user);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/users/user/{userid}";

        Role r2 = new Role("user");
        r2.setRoleid(2);

        // create a user
        String newUsername = "puttat";
        User user = new User(newUsername,
                "password",
                "puttat@example.com");
        user.getRoles().add(new UserRoles(user, r2));
        user.setUserid(14);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(user);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 14)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/users/user/{userid}";

        User user = userList.get(1);
        user.setPrimaryemail("cinnamon@example.com");

        Mockito.when(userService.update(user, 12))
                .thenReturn(user);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, user.getUserid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}