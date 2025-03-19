package m1.uasz.sn;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {
    public static void main(String[] args) {
        String rawPassword = "admin";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        System.out.println("Hash Bcrypt : " + hashedPassword);
    }
}
