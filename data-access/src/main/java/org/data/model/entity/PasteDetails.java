package org.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "paste_details")
public class PasteDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private String title;

    @Column(name = "preview_text")
    private String previewText;
    private String data;

    @Column(name = "data_location")
    private String dataLocation;

    @Column(name = "data_length")
    private Integer dataLength;

    @Column(name = "data_size")
    private String dataSize;

    @Column(name = "short_code")
    private String shortCode;
    private String domain;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paste_info_id")
    private PasteInfo pasteInfo;

    @Override
    public String toString() {
        return "PasteDetails{" +
                "title='" + title + '\'' +
                ", previewText='" + previewText + '\'' +
                ", data='" + data + '\'' +
                ", dataLocation='" + dataLocation + '\'' +
                ", dataSize=" + dataSize +
                ", dataLength=" + dataLength +
                ", shortCode='" + shortCode + '\'' +
                ", domain='" + domain + '\'' +
                ", pasteInfo='" + pasteInfo.getUuid() + '\'' +
                '}';
    }

    public PasteDetails() {}
}
