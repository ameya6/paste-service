package org.paste.service;

import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import lombok.extern.log4j.Log4j2;
import org.paste.dao.MinioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class MinioService {

    @Autowired
    public MinioDao minioDao;

    @Value("${default.bucket}")
    private String bucket;

    public Mono<ObjectWriteResponse> save(String location, StringBuilder data) throws Exception {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .stream(new ByteArrayInputStream(data.toString().getBytes(StandardCharsets.UTF_8)), data.length(), -1)
                .bucket(bucket)
                .object(location)
                .build();
        return minioDao.save(objectArgs);
    }




}
