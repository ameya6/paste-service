package org.paste.service;

import io.minio.GetObjectArgs;
import lombok.extern.log4j.Log4j2;
import org.data.model.entity.PasteDetails;
import org.data.model.entity.PasteInfo;
import org.data.model.request.PasteRequest;
import org.data.model.response.PasteResponse;
import org.paste.dao.MinioDao;
import org.paste.dao.PasteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class PasteAccessService {

    @Autowired
    private PasteDao pasteDao;

    @Autowired
    private MinioDao minioDao;

    @Value("${default.bucket}")
    private String bucket;

    public PasteResponse get(String shortCode, UUID userUUID) throws Exception {
        PasteInfo pasteInfo = pasteDao.get(shortCode, userUUID);
        PasteDetails pasteDetails = pasteInfo.getPasteDetails();
        log.debug(pasteInfo + "\n" + pasteDetails);
        StringBuilder data = pasteDetails.getData();
        if(pasteDetails.isSaveToFile()) {
            String location = pasteInfo.getUserUUID() + "/" + pasteInfo.getUuid();
            log.debug("Fetching data from bucket: " + bucket + " location: " + location);
            data = getData(location);
        }
        return pasteResponse(pasteDetails.getPreviewText(), data);
    }

    private StringBuilder getData(String location) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(location).build();
        return minioDao.get(getObjectArgs);
    }

    private PasteResponse pasteResponse(String previewText, StringBuilder data) {
        return PasteResponse.builder()
                .preview(previewText)
                .data(data)
                .build();
    }
}
