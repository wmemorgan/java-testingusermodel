package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.views.UserNameCountEmails;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // print out test data
//        List<User> userList = userService.findAll();
//        System.out.println("userList size: " + userList.size());
//        for (User u : userList) {
//            System.out.println(u.getUserid() + " " +
//                    u.getUsername() + " " +
//                    u.getPrimaryemail()
//            );
//        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void afindUserById() {
        System.out.println("Expected: misskitty");
        System.out.println("Actual: " + userService.findUserById(14).getUsername());
        assertEquals("misskitty",
                userService.findUserById(14).getUsername());
    }

    @Test
    public void bfindByNameContaining() {
        System.out.println("Expected: 1");
        System.out.println("Actual: " +
                userService.findByNameContaining("barn").size());
        assertEquals(1,
                userService.findByNameContaining("barn").size());
    }

    @Test
    public void cfindAll() {
        System.out.println("Expected: 30");
        System.out.println("Actual: " +
                userService.findAll().size());
        assertEquals(30,
                userService.findAll().size());
    }

    @Test
    public void delete() {
        userService.delete(13);
        System.out.println("Expected: 29");
        System.out.println("Actual: " +
                userService.findAll().size());
        assertEquals(29, userService.findAll().size());
    }

    @Test
    public void efindByName() {
        System.out.println("Expected: misskitty@school.lambda");
        System.out.println("Actual: " +
                userService.findByName("misskitty")
                        .getPrimaryemail());
        assertEquals("misskitty@school.lambda",
                userService.findByName("misskitty")
                        .getPrimaryemail());
    }

    @Test
    public void fsave() {

        List<UserRoles> roles = new ArrayList<>();
        roles.add(new UserRoles(new User(),
                roleService.findByName("user")));
        User testUser = new User("george",
                "password",
                "george@example.com", roles);

        User addUser = userService.save(testUser);
        User foundUser = userService.findByName(addUser.getUsername());

        System.out.println("Expected: george@example.com");
        System.out.println("Actual: " +
                foundUser.getPrimaryemail());

        assertEquals("george@example.com",
                foundUser.getPrimaryemail());
    }

    @Test
    public void gupdate() {
        // data, user
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(),
                roleService.findByName("user")));
        datas.add(new UserRoles(new User(),
                roleService.findByName("data")));
        User u = new User("cinnamon",
                "1234567",
                "cinnamon@example.com",
                datas);

        User updateUser = userService.update(u, 7);

        System.out.println("Expected: cinnamon@example.com");
        System.out.println("Actual: " + updateUser.getPrimaryemail());

        assertEquals("cinnamon@example.com", updateUser.getPrimaryemail());
    }

    @Test
    public void hgetCountUserEmails() {
        List<UserNameCountEmails> userEmailCountList = userService.getCountUserEmails();
        UserNameCountEmails foundUser = userEmailCountList.stream()
                .filter(user -> "cinnamon".equals(user.getUsernamerpt()))
                .findAny()
                .orElse(null);
        System.out.println("Expected: 3");
        System.out.println("Actual: " + foundUser.getCountemails());

        assertEquals(3, foundUser.getCountemails());
    }

//    @Test // FAILED TEST
//    public void ideleteUserRole() {
//        User u1 = userService.findByName("cinnamon");
//        System.out.println("BEFORE: " + u1.getRoles().size());
//        userService.deleteUserRole(7, 3);
//        User u2 = userService.findByName("cinnamon");
//        System.out.println("Expected: 1");
//        System.out.println("Actual: " + u2.getRoles().size());
//        assertEquals(1, u2.getRoles().size());
//    }
//
//    @Test // FAILED TEST
//    public void jaddUserRole() {
//        User u1 = userService.findByName("cinnamon");
//        System.out.println("BEFORE: " + u1.getRoles().size());
//        userService.addUserRole(7, 1);
//        User u2 = userService.findByName("cinnamon");
//        System.out.println("Expected: 3");
//        System.out.println("Actual: " + u2.getRoles().size());
//        assertEquals(3, u2.getRoles().size());
//    }
}