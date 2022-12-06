package org.paste.dao;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MinioDao {

    @Autowired
    private MinioClient minioClient;

    public ObjectWriteResponse save(PutObjectArgs putObjectArgs) throws Exception {
        return minioClient.putObject(putObjectArgs);
    }
}
