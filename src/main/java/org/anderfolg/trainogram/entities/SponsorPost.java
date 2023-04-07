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
    @Convert(converter = ContentConverter.FieldConverter.class)
    private ContentType type;

    //  TODO (Bogdan O.) 7/4/23: needs to be removed
    public SponsorPost( Post sponsoredPost, User sponsor, ContentType type ) {
        this.sponsoredPost = sponsoredPost;
        this.sponsor = sponsor;
        this.type = type;
    }
}
