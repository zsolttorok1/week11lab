package businesslogic;

import dataaccess.UserDB;
import domainmodel.Role;
import domainmodel.User;
import java.util.List;

public class UserService {

    private UserDB userDB;

    public UserService() {
        userDB = new UserDB();
    }

    public User getByUsername(String username) {
        return userDB.getByUsername(username);
    }
    
    public User getByEmail(String email) {
        return userDB.getByEmail(email);
    }
    
    public List<User> getAll() throws Exception {
        return userDB.getAll();
    }

    public boolean update(String username, String password, String email, boolean active, String firstname, String lastname) throws Exception {
        User user = userDB.getByUsername(username);
        user.setPassword(password);
        user.setActive(active);
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return userDB.update(user);
    }

    public boolean delete(String username) throws Exception {
        User deletedUser = userDB.getByUsername(username);
        return userDB.delete(deletedUser);
    }

    public boolean insert(String username, String password, String email, boolean active, String firstname, String lastname) throws Exception {
        User user = new User(username, password, email, active, firstname, lastname);
        Role role = new Role(2);  // default regular user role
        user.setRole(role);
        return userDB.insert(user);
    }
}
