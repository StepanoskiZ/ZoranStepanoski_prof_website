package com.paradox.zswebsite.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.BlogPost} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlogPostDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String content;

    @NotNull
    private Instant publishedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogPostDTO)) {
            return false;
        }

        BlogPostDTO blogPostDTO = (BlogPostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blogPostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogPostDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            "}";
    }
}
