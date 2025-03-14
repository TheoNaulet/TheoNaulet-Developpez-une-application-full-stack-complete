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
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
