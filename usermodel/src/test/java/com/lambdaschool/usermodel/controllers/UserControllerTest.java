package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import com.lambdaschool.usermodel.views.UserNameCountEmails;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    private List<User> userList = new ArrayList<>();
    private List<Role> roleList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);

        roleList.add(r1);
        roleList.add(r2);
        roleList.add(r3);

        // admin, data, user
        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(),
                r1));
        admins.add(new UserRoles(new User(),
                r2));
        admins.add(new UserRoles(new User(),
                r3));
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local",
                admins);
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
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(),
                r3));
        datas.add(new UserRoles(new User(),
                r2));
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local",
                datas);
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
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local",
                users);
        u3.setUserid(13);
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails()
                .get(0)
                .setUseremailid(40);
        userList.add(u3);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda",
                users);
        u4.setUserid(14);
        userList.add(u4);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda",
                users);
        u5.setUserid(15);
        userList.add(u5);

    }

    @After
    public void tearDown() throws Exception {
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
        System.out.println("Actual " + tr);

        assertEquals("REST API returns list: ", er, tr );
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/12";
        Mockito.when(userService.findUserById(12))
                .thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));

        System.out.println("Expect: " + er);
        System.out.println("Actual " + tr);

        assertEquals("REST API returns object: ", er, tr );
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        String apiUrl = "/users/user/1200";
        Mockito.when(userService.findUserById(1200))
                .thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        System.out.println("Expect: " + er);
        System.out.println("Actual " + tr);

        assertEquals("REST API returns object: ", er, tr );
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
        System.out.println("Actual " + tr);

        assertEquals("REST API returns object: ", er, tr );
    }

    @Test
    public void getUserLikeName() throws Exception {
        String apiUrl = "/users/user/name/like/amon";

        List<User> filteredList = userList.stream()
                .filter(u -> u.getUsername().contains("amon"))
                .collect(Collectors.toList());

        Mockito.when(userService.findByNameContaining("amon"))
                .thenReturn(filteredList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(filteredList);

        System.out.println("Expect: " + er);
        System.out.println("Actual " + tr);

        assertEquals("REST API returns list: ", er, tr );
    }

    @Test
    public void addNewUser() throws Exception {
        String apiUrl = "/users/user";

        // create a user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                roleList.get(1)));
        String newUsername = "george";
        User u6 = new User(newUsername,
                "password",
                "george@example.com",
                users);
        u6.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u6);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u6);

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

        // create a user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                roleList.get(1)));
        String newUsername = "puttat";
        User u4 = new User(newUsername,
                "password",
                "puttat@example.com",
                users);
        u4.setUserid(14);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u4);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u4);

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

        User u = userList.get(2);
        u.setPrimaryemail("cinnamon@example.com");

        Mockito.when(userService.update(u, 12))
                .thenReturn(u);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, u.getUserid())
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

    @Test
    public void getNumUserEmails() throws Exception {
        String apiUrl = "/users/user/email/count";

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserRoleByIds() throws Exception {
        String apiUrl = "/users/user/{userid}/role/{roleid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, 12, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postUserRoleByIds() throws Exception {
        String apiUrl = "/users/user/{userid}/role/{roleid}";

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl, 14, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
}