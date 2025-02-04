package com.scm.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {

    @NotBlank(message="Username is required")
    @Size(min=3,message="Minimum 3 Character required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message="Password is required")
    @Size(min=6,message="Minimum 6 Character required")
    private String password;
    @NotBlank(message="Phone Number is required")
    @Size(min=8,max=12,message="Phone Number is Invalid")
    private String phoneNumber;
    @NotBlank(message="About is required")
    private String about;
}
//This is DTO

/*An Entity is designed to map to the database, whereas a DTO can include validations specific to the form (like ensuring the password is confirmed or validating a specific format). */