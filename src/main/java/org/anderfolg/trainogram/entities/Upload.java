package org.anderfolg.trainogram.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedBy
    private String username;


    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String fileType;

    @CreatedDate
    private LocalDateTime createDate;

    public Upload( String newFileName, String fileUrl, String username, String contentType ) {
        this.fileName = newFileName;
        this.url = fileUrl;
        this.username = username;
        this.fileType = contentType;
    }


    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
