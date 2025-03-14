package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeWithSubscriptionDTO {
    private Long id;
    private String title;
    private String description;
    
    @JsonProperty("isSubscribed")
    private boolean isSubscribed;
}
