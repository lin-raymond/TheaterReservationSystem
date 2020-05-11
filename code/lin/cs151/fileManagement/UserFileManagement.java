package lin.cs151.fileManagement;

import lin.cs151.user.management.User;

import java.io.*;
import java.util.*;

/**
 * Handles the storage of username, salt, and hashed password into a specific file.
 *
 * @author Raymond Lin
 */
public class UserFileManagement {

    private File userFile;

    /**
     * Fetches all {@link lin.cs151.user.management.User users} from specific file
     *
     * @return a list of all {@link lin.cs151.user.management.User users}
     */
    public List<User> userFetch() {
        List<User> users = new ArrayList<>();
        BufferedReader readUser;
        try {
            readUser = new BufferedReader(new FileReader(userFile));
            String userData;
            userData = readUser.readLine();
            while (userData != null) {
                String[] userLine = userData.split(" ");
                User user = new User(userLine[0], userLine[1], userLine[2]);
                users.add(user);
                userData = readUser.readLine();
            }
            readUser.close();
        } catch (FileNotFoundException e) {
            // skip pretend it does not exist
        } catch (IOException e) {
            // pretend file does not exist
        }
        return users;
    }

    /**
     * Records new {@link lin.cs151.user.management.User user} data to a specific file
     *
     * @param user to write to file
     */
    public void addNewUser(User user) {
        BufferedWriter writeUser;
        try {
            writeUser = new BufferedWriter(new FileWriter(userFile, true));
            String userData = user.toString();
            writeUser.write(userData);
            writeUser.newLine();
            writeUser.close();
        } catch (IOException e) {
            System.out.println("Cannot Write to File at .\\data\\users.txt");
        }
    }

    /**
     * Creates new instance User File Management
     */
    public UserFileManagement() {
        userFile = new File(".\\hw1\\data", "users.txt");
    }

}
