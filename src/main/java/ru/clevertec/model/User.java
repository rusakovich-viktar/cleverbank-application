package ru.clevertec.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
