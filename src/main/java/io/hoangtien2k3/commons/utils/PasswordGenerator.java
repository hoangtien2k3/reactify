package io.hoangtien2k3.commons.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating random passwords.
 * Provides methods to generate passwords with a mix of upper case letters, lower case letters, and numbers.
 */
public class PasswordGenerator {

    /**
     * Generates a random password using Apache Commons Lang's RandomStringUtils.
     * The password contains a mix of upper case letters, lower case letters, and numbers.
     *
     * @return a randomly generated password
     */
    public static String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(1);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * Main method for testing the password generation.
     * Prints a randomly generated password to the console.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println(generateCommonLangPassword());
    }
}
