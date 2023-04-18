package org.anderfolg.trainogram.entities.chatentities;

import lombok.*;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "messages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long chatId;

    private String text;
    @CreatedDate
    private LocalDateTime createDate;

    private boolean viewed;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @PrePersist
    public void initCreateDate() {
        this.createDate = LocalDateTime.now();
    }
}
