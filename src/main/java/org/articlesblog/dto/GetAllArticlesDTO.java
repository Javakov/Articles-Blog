package org.articlesblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllArticlesDTO {
    int id;
    String label;
    String author;
    String title;
    String description;
    String dateCreate;
    String image;

    @Override
    public String toString() {
        return "Статья: " +
                "id=" + id +
                ", label='" + label + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateCreate='" + dateCreate + '\'' +
                ", image='" + image + '\'';
    }
}
