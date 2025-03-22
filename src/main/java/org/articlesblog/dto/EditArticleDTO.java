package org.articlesblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditArticleDTO {
    int id;
    String title;
    String description;
    String text;
    String author;
    String label;
    String image;

    @Override
    public String toString() {
        return "Статья: " +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", label='" + label + '\'' +
                ", image='" + image + '\'';
    }

}
