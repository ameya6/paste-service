package org.paste.service;

import com.google.gson.Gson;
import io.minio.ObjectWriteResponse;
import lombok.extern.log4j.Log4j2;
import org.data.model.entity.PasteDetails;
import org.data.model.entity.PasteInfo;
import org.data.model.request.PasteRequest;
import org.data.model.response.DUIDResponse;
import org.data.model.response.PasteResponse;
import org.paste.dao.PasteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PasteService {

    @Value("${default.expiryInSeconds}")
    private Long defaultExpiryInSeconds;

    @Value("${default.domain}")
    private String domain;

    @Value("${default.bucket}")
    private String bucket;

    @Value("${default.dataLimit}")
    private Long dataLimit;

    @Value("${duid-server}")
    private String duidServer;

    @Value("${default.dataStoreLimit}")
    private Integer dataStoreLimit;

    @Autowired
    private Gson gson;

    @Autowired
    private PasteDao pasteDao;

    @Autowired
    private MinioService minioService;

    private final String charMap = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public PasteResponse pasteProcess(PasteRequest pasteRequest) {
        PasteResponse pasteResponse = null;
        try {
            PasteInfo pasteInfo = pasteInfo(pasteRequest);
            PasteDetails pasteDetails = pasteDetails(pasteRequest);

            pasteInfo.setPasteDetails(pasteDetails);
            pasteDetails.setPasteInfo(pasteInfo);

            log.info(pasteInfo + " " + pasteDetails);

            if(pasteDetails.isSaveToFile())
                saveData(pasteInfo.getUserUUID() + "/" + pasteInfo.getUuid(), pasteRequest.getData());
            save(pasteDetails);

            pasteResponse = pasteResponse(domain + pasteInfo.getShortCode(), "Successfully save paste");
            return pasteResponse;
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage(), e);
            return PasteResponse.builder().message("Failed to create paste").build();
        } finally {
            // event log
        }
    }

    private void save(PasteDetails pasteDetails) {
        pasteDao.save(pasteDetails);
    }

    private String dataSize(int length) {
        if(length < 1000)
            return length + " B";
        else if(length >= 1000 && length < 1_000_000)
            return length / 1000 + " KB";
        else if(length >= 1_000_000 && length < dataLimit)
            return Math.round((double)length / 1_000_000) + " MB";
        else
            return "";
    }

    public Long distributedUID() throws Exception {
        //log.info(uidServer);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(duidServer))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), DUIDResponse.class).getDuid();
    }

    public String base62(long n)
    {
        StringBuilder shorturl = new StringBuilder("");
        for(int i = 0; i < 7; i++)
        {
            int mod = (int)(n % 62);
            shorturl.append(charMap.charAt(mod));
            n = n/62;
        }
        return shuffle(shorturl.reverse().toString());
    }

    public String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size() != 0){
            int randomPicker = (int)(Math.random() * characters.size());
            output.append(characters.remove(randomPicker));
        }
        return output.toString();
    }

    private PasteInfo pasteInfo(PasteRequest pasteRequest) throws Exception {
        Long expiryInSeconds = pasteRequest.getExpiryInSeconds() != null ? pasteRequest.getExpiryInSeconds() : defaultExpiryInSeconds;
        return PasteInfo.builder()
                .createdAt(LocalDateTime.now())
                .userId(pasteRequest.getUserId())
                .userUUID(pasteRequest.getUserUUID())
                .shortCode(base62(distributedUID()))
                .uuid(UUID.randomUUID())
                .expiryDate(LocalDateTime.now().plusSeconds(expiryInSeconds))
                .build();
    }

    private PasteDetails pasteDetails(PasteRequest pasteRequest) throws Exception {
        String title = pasteRequest.getTitle() != null ? pasteRequest.getTitle() : "Untitled";
        int dataLength = pasteRequest.getData().toString().getBytes(StandardCharsets.UTF_8).length;
        boolean saveToFile = dataLength > dataStoreLimit ? true : false;
        StringBuilder data = !saveToFile ? pasteRequest.getData() : null;
        //String dataLocation = dataLength > dataLimit ? storeData(data) : null;
        UUID uuid = UUID.randomUUID();

        return PasteDetails.builder()
                .title(title)
                .uuid(uuid)
                .data(data)
                .bucket(bucket)
                .folder(pasteRequest.getUserUUID().toString())
                .domain(domain)
                .dataLength(dataLength)
                .saveToFile(saveToFile)
                .dataSize(dataSize(dataLength))
                .previewText(pasteRequest.getData().substring(0, 20) + "...")
                .build();
    }

    private void saveData(String location, StringBuilder data) throws Exception {
        storeData(location, data);
    }

    public void storeData(String location, StringBuilder data) throws Exception {
        minioService.save(location, data)
                .doOnError(error -> log.info(error))
                .subscribe();
    }

    private PasteResponse pasteResponse(String url, String message) {
        return PasteResponse.builder()
                .url(url)
                .message(message)
                .build();
    }
}
