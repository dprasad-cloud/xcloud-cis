package com.extremecloud.cis.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author dprasad
 */
@RestController
@Slf4j
public class FileController {

    @PostMapping(value = {"/v1/update-serial-contract"})
    @ResponseBody
    public ResponseEntity<String> handleUpdateSerialContractData(@RequestBody(required = false) String requestBody) {
        if (StringUtils.isEmpty(requestBody)) {
            return ResponseEntity.badRequest().body("Error - Empty request body");
        }
        try {
            requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
            String uid = UUID.randomUUID().toString();
            String filename = uid + ".csv";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/" + filename))) {
                String[] lines = requestBody.split(Pattern.quote("\\n"));
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            return ResponseEntity.ok(uid);
        } catch (Exception e) {
            log.error("Exception processing file upload request -> ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
