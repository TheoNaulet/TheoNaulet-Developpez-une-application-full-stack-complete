package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class UserAuthDTO {
    private String email;
    private String password;
}
