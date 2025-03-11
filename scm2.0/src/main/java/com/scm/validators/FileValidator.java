package com.scm.validators;


import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile>{

    //Here 2 is the size in MB
    private static final long MAX_FILE_SIZE=1024*1024*2;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        //if File is not selected
        if (file==null || file.isEmpty()) {
            return false;
        }
        //if file size is more than 2
        if(file.getSize()>MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should be less the 2MB").addConstraintViolation();
            return false;
        }



        return true;
    }

}
