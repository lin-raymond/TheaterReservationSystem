package lin.cs151.user.authentication;

import lin.cs151.user.management.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Securely authenticates a {@link User user} into the system by hashing the password.
 *
 * @author Raymond Lin
 */
public class Authentication {

    /**
     * Creates a random salt to hash the password with
     *
     * @param password the password to hash
     * @return a String array containing hashed password and salt
     */
    public String[] generateHashSaltPassword(String password) {
        byte[] salt = generateSalt();
        return hashPassword(password, salt);
    }

    /**
     * Hashes a password with a given salt
     * @param password the password to hash
     * @param salt the salt to use to hash password
     * @return a String array containing salt and hashed password
     */
    private String[] hashPassword(String password, byte[] salt) {
        String[] saltHashedPassword = new String[2];
        try {
            // secure hashing code from https://www.baeldung.com/java-password-hashing by Sam Milington
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            saltHashedPassword[0] = convertSaltAndHashByteArrayToString(salt);
            saltHashedPassword[1] = convertSaltAndHashByteArrayToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return saltHashedPassword;
    }

    /**
     * Creates a new random salt using a secure random number generator
     * @return an array of random bytes
     */
    private byte[] generateSalt() {
        // secure salting code from https://www.baeldung.com/java-password-hashing by Sam Millington
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Converts the byte array to a string using Base64
     * @param bytes the byte array to convert
     * @return a String representing the byte array
     */
    private String convertSaltAndHashByteArrayToString(byte[] bytes) {
        // conversion code from https://howtodoinjava.com/array/convert-byte-array-string-vice-versa/ by Lokesh Gupta
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Converts a String into a byte array using Base64
     * @param string the string to convert
     * @return a byte array representing the string
     */
    private byte[] convertSaltAndHashStringToByteArray(String string) {
        // conversion code from https://howtodoinjava.com/array/convert-byte-array-string-vice-versa/ by Lokesh Gupta
        return Base64.getDecoder().decode(string);
    }

    /**
     * Authenticate user with given password and user recovered
     *
     * @param password plain text password to hash and test against stored hash
     * @param user     the user profile containing salts and hashed password to test against
     * @return boolean whether or not user has been authenticated successfully
     * @throws AuthenticationErrorException when user cannot be authenticated by the system
     */
    public boolean authenticateUser(String password, User user) throws AuthenticationErrorException {
        String salt = user.getSalt();
        byte[] bytes = convertSaltAndHashStringToByteArray(salt);
        String[] userData = hashPassword(password, bytes);
        String hashedPassword = user.getHashedPassword();
        if (userData[1].compareTo(hashedPassword) == 0) {
            return true;
        }
        throw new AuthenticationErrorException("Incorrect username or password.");
    }
}
