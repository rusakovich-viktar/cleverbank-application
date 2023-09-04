package ru.clevertec.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class representing a user entity in the system.
 * This class contains information about the user, including their identification number, first name, last name,
 * patronymic, login, password, and a list of accounts associated with this user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String identificationNumberOfPassport;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private List<Account> accounts;
}
