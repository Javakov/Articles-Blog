package org.articlesblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetArticleDTO {
    int id;
    String title;
    String text;
    String author;
    String label;
    String dateCreate;
    String dateChange;
    String image;

    @Override
    public String toString() {
        return "Статья: " +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", label='" + label + '\'' +
                ", dateCreate='" + dateCreate + '\'' +
                ", dateChange='" + dateChange + '\'' +
                ", image='" + image + '\'';
    }
}
