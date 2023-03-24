package org.anderfolg.trainogram.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "messages")
    private String message;

    @ManyToOne
    @JoinColumn(name = "recepient_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false)
    @Convert(converter = NotificationConverter.FieldConverter.class)
    private NotificationType type;

    @Column(name = "content_id")
    private Long contentId;
    @CreatedDate
    private LocalDateTime createDate;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
