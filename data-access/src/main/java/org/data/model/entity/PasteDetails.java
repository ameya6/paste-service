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

    @Column(name = "data_length")
    private Integer dataLength;

    @Column(name = "data_size")
    private String dataSize;

    @Column(name = "short_code")
    private String shortCode;
    private String domain;
    private String bucket;
    private String folder;
    private String filename;

    @Column(name = "save_to_file")
    private boolean saveToFile;

    @Column(name = "version_id")
    private String versionId;
    private String etag;
    private String region;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paste_info_id")
    private PasteInfo pasteInfo;

    @Override
    public String toString() {
        return "PasteDetails{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title='" + title + '\'' +
                ", previewText='" + previewText + '\'' +
                ", data='" + data + '\'' +
                ", dataLength=" + dataLength +
                ", dataSize='" + dataSize + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", domain='" + domain + '\'' +
                ", bucket='" + bucket + '\'' +
                ", folder='" + folder + '\'' +
                ", pasteInfo='" + pasteInfo.getUuid() + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

    public PasteDetails() {}
}
