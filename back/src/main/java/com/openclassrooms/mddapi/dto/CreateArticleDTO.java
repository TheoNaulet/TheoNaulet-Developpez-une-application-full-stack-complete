package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class CreateArticleDTO {
    private String title;
    private String content;
    private Long themeId;
}
