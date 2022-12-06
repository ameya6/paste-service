package org.paste.dao;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MinioDao {

    @Autowired
    private MinioClient minioClient;

    public Mono<ObjectWriteResponse> save(PutObjectArgs putObjectArgs) throws Exception {
        return Mono.just(minioClient.putObject(putObjectArgs));
    }

    public StringBuilder get(GetObjectArgs getObjectArgs) throws Exception {
        byte[] data = minioClient.getObject(getObjectArgs).readAllBytes();
        return new StringBuilder(new String(data, "UTF-8"));
    }
}
