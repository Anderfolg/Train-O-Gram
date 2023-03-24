package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "commentaries")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Like> likes;

    @Column(nullable = false)
    @Convert(converter = ContentConverter.FieldConverter.class)
    private ContentType type;


    public Comment( String comment, User user, Post post) {
        this.comment = comment;
        this.user = user;
        this.post = post;
        this.type = ContentType.COMMENT;
    }
}