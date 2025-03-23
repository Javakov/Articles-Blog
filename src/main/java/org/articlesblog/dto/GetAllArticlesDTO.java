package org.articlesblog.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllArticlesDTO {
    private UUID id;
    private String label;
    private String author;
    private String title;
    private String description;
    private String dateCreate;
    private String image;
}
