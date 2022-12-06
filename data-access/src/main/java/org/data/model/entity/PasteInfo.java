package org.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "paste_info")
public class PasteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_uuid")
    private UUID userUUID;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "short_code")
    private String shortCode;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    private boolean expired;
    private boolean deleted;


    @OneToOne(mappedBy = "pasteInfo", fetch = FetchType.EAGER)
    private PasteDetails pasteDetails;

    @Override
    public String toString() {
        return "PasteInfo{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", userId=" + userId +
                ", userUUID=" + userUUID +
                ", createdAt=" + createdAt +
                ", expiryDate=" + expiryDate +
                ", expired=" + expired +
                ", shortCode='" + shortCode + '\'' +
                ", deleted=" + deleted +
                ", pasteDetails=" + pasteDetails.getUuid() +
                '}';
    }

    public PasteInfo() {}
}
