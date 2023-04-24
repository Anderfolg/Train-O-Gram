package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "new_likes")
@NoArgsConstructor
@Setter
@Getter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "content_id")
    private Long contentId;

    @Convert(converter = ContentType.ContentConverter.FieldConverter.class)
    @Column(name = "content_type",nullable = false)
    private ContentType contentType;


    //  TODO (Bogdan O.) 24/4/23: remove this
    public Like( User user, Long contentId, ContentType contentType) {
        this.user = user;
        this.contentId = contentId;
        this.contentType = contentType;
    }
}
