package org.paste.service;

import com.google.gson.Gson;
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

    @Value("${default.dataLimit}")
    private Long dataLimit;

    @Value("${duid-server}")
    private String duidServer;

    @Autowired
    private Gson gson;

    @Autowired
    private PasteDao pasteDao;

    private final String charMap = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public PasteResponse pasteProcess(PasteRequest pasteRequest) {
        log.info(pasteRequest);
        PasteResponse pasteResponse = PasteResponse.builder().build();
        try {
            PasteInfo pasteInfo = pasteInfo(pasteRequest);
            PasteDetails pasteDetails = pasteDetails(pasteRequest);

            pasteInfo.setPasteDetails(pasteDetails);
            pasteDetails.setPasteInfo(pasteInfo);

            pasteResponse.setUrl(pasteDetails.getDomain() + pasteDetails.getShortCode());
            pasteResponse.setMessage("Paste saved successfully");

            save(pasteDetails);

            log.info(pasteInfo + " " + pasteDetails);
            return pasteResponse;
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage(), e);
            pasteResponse.setMessage("Failed to create paste");
            return pasteResponse;
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
            return length / 1000 + " MB";
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

    private PasteInfo pasteInfo(PasteRequest pasteRequest) {
        Long expiryInSeconds = pasteRequest.getExpiryInSeconds() != null ? pasteRequest.getExpiryInSeconds() : defaultExpiryInSeconds;
        return PasteInfo.builder()
                .createdAt(LocalDateTime.now())
                .userId(pasteRequest.getUserId())
                .userUUID(pasteRequest.getUserUUID())
                .uuid(UUID.randomUUID())
                .expiryDate(LocalDateTime.now().plusSeconds(expiryInSeconds))
                .build();
    }

    private PasteDetails pasteDetails(PasteRequest pasteRequest) throws Exception {
        String title = pasteRequest.getTitle() != null ? pasteRequest.getTitle() : "Untitled";
        String data = pasteRequest.getData();
        int dataLength = data.getBytes(StandardCharsets.UTF_8).length;

        return PasteDetails.builder()
                .title(title)
                .uuid(UUID.randomUUID())
                .shortCode(base62(distributedUID()))
                .data()
                .domain(domain)
                .dataLength(dataLength)
                .dataSize(dataSize(dataLength))
                .previewText(pasteRequest.getData().substring(0, 20) + "...")
                .build();
    }
}
