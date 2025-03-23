package org.articlesblog.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "articles")
@Data
public class Article {
    @Id
    @GeneratedValue()
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "text")
    private String text;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "image", length = 500)
    private String image;

    @CreationTimestamp
    @Column(name = "date_create", nullable = false)
    private ZonedDateTime dateCreate;

    @UpdateTimestamp
    @Column(name = "date_change", nullable = false)
    private ZonedDateTime dateChange;
}