package view;

import dao.UserDao;

import java.security.NoSuchAlgorithmException;

import model.User;

import static utils.GenericUtils.giveInputFromUser;
import static utils.Password.generateHash;

public class Login {

    public static User loginUser() throws NoSuchAlgorithmException {
        boolean check = false;
        UserDao ud = new UserDao();
        User user = new User();
        do {
            String username = giveInputFromUser("username: ");
            String password = generateHash(giveInputFromUser("password: "), "MD5");
            user = ud.getUserByNameAndPassword(username, password);
            if (user.getId() == 0) {
                System.out.println("Wrong username or password!");
            } else {
                check = true;
            }
        } while (check == false);
        return user;
    }

}
