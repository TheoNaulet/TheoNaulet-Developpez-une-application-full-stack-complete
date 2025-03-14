package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    public UserDTO(Long id, String username, String email, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
