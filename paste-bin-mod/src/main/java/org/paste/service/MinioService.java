package org.paste.service;

import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import lombok.extern.log4j.Log4j2;
import org.data.model.request.PasteRequest;
import org.paste.dao.MinioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class MinioService {

    @Autowired
    public MinioDao minioDao;

    @Value("${default.bucket}")
    private String bucket;

    public ObjectWriteResponse save(String location, String data) throws Exception {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .stream(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), data.length(), -1)
                .bucket(bucket)
                .object(location)
                .build();

        return minioDao.save(objectArgs);
    }




}
