
package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity // El decorador @Entity indica que esta clase es una entidad JPA, JPA es Java Persistence API y es la API de Java para la persistencia de datos
@Table(name = "Comment") // El decorador @Table indica el nombre de la tabla en la base de datos
public class Comment {

    @Id // El @Id ya implica que es unique y not empty
    @JsonProperty("id")
    private String id;
    @JsonProperty("body")
    @NotEmpty(message = "The message cannot be empty.")
    @Column(columnDefinition="TEXT") // El decorador @Column es para definir el tipo de dato en la base de datos
    private String body;

    @JsonProperty("author")
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    // name es el nombre de la columna en la tabla de la base de datos Comment
    // referencedColumnName es el nombre de la columna en la tabla de la base de datos User que equivale a la columna en Comment
    @OneToOne(cascade=CascadeType.ALL) // Esto significa que las operaciones que se hacen sobre Comment se harán también sobre User
    private User author;

    @JsonProperty("created_at")
    @NotEmpty(message = "The field created_at cannot be empty.")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Comment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("body");
        sb.append('=');
        sb.append(((this.body == null) ? "<null>" : this.body));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null) ? "<null>" : this.author));
        sb.append(',');
        sb.append("createdAt");
        sb.append('=');
        sb.append(((this.createdAt == null) ? "<null>" : this.createdAt));
        sb.append(',');
        sb.append("updatedAt");
        sb.append('=');
        sb.append(((this.updatedAt == null) ? "<null>" : this.updatedAt));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
