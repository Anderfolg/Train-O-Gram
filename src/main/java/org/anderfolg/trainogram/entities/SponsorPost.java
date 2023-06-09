package org.anderfolg.trainogram.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sponsor_posts")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SponsorPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @OneToOne
    private Post sponsoredPost;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private User sponsor;

    @Column(nullable = false)
    @Convert(converter = ContentType.ContentConverter.FieldConverter.class)
    private ContentType type;

}
