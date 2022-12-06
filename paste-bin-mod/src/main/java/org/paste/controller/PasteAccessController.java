package org.paste.controller;

import lombok.extern.log4j.Log4j2;
import org.data.model.request.PasteRequest;
import org.data.model.response.PasteResponse;
import org.paste.service.PasteAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@Log4j2
public class PasteAccessController {

    @Autowired
    private PasteAccessService pasteAccessService;

    @PostMapping(value = "/{shortCode}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasteResponse> paste(@PathVariable("shortCode") String shortCode, @RequestBody PasteRequest pasteRequest) {
        try {
            return ResponseEntity.ok(pasteAccessService.get(shortCode, pasteRequest.getUserUUID()));
        } catch (Exception e) {
            log.error("Exception :" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PasteResponse.builder().message("Failed creating Paste").build());
        }
    }
}
