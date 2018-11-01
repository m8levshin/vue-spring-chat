package ru.moolls.webchat.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class RegDto {


    @NotNull
    @Size(min = 3, max = 50)
    private String userName;

    @NotNull

    @Size(min = 3, max = 50)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String repeatPassword;


    @AssertTrue(message = "passwords do not match")
    private boolean isPasswordEquals() {
        return password.equals(repeatPassword);
    }
}
