package org.anderfolg.trainogram.entities.chatentities;

import lombok.*;
import org.anderfolg.trainogram.entities.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Table(name = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender")
    private User sender;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> recipients;

}
