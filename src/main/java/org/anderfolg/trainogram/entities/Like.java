package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "new_likes")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
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

}
