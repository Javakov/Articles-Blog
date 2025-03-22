package org.articlesblog.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", length = 250)
    private String title;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column(name = "author", length = 250)
    private String author;

    @Column(name = "label", length = 250)
    private String label;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "date_create", nullable = false, columnDefinition = "timestamp with time zone")
    private LocalDateTime dateCreate;

    @Column(name = "date_change", columnDefinition = "timestamp with time zone")
    private LocalDateTime dateChange;
}