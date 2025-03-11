package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message ="Name is Required !")
    private String name;

    @NotBlank(message = "Email is Required !")
    @Email(message = "Invalid email address !")
    private String email;

    @NotBlank(message ="Phone Number is Required !")
    @Pattern(regexp="(^$|[0-9]{10})", message="Phone number must be 10 digits")
    private String phoneNumber;
    @NotBlank(message ="Address is Required !")
    private String address;
    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;

    //created an Annotation to validate the Image and check the file type, size and resolution.
    @ValidFile
    private MultipartFile contactImage;
    private String picture;
}
