package com.pinnacle.indexing.controller;

import java.io.InputStream;
// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinnacle.indexing.model.Index_Table;
import com.pinnacle.indexing.service.IndexService;

@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/index")
    public void addIndexDetails(@RequestBody Index_Table index) {
        indexService.addIndexDetails(index);
    }

    @GetMapping("/index")
    public List<Index_Table> getAllIndexDetails() {
        return indexService.getAllIndexDetails();
    }

    @PostMapping("/index/all")
    public void addAllIndexDetails(@RequestBody List<Index_Table> index) {
        indexService.addAllIndexDetails(index);
    }

    @PostMapping("/upload")
    public String uploadJsonFile(@RequestParam("file") MultipartFile file){
    long startTime = System.currentTimeMillis(); // Log start time
    try{
    InputStream inputStream = file.getInputStream();
    ObjectMapper objectMapper = new ObjectMapper();

    List<Index_Table> allData = objectMapper.readValue(inputStream, new
    TypeReference<List<Index_Table>>() {});

    System.out.println("Step 1: File reading and parsing completed. Total records: " + allData.size());

    int batchSize = 100000;
    for(int i = 0; i < allData.size(); i += batchSize){
    long batchStartTime = System.currentTimeMillis(); // Log start time
    int end = Math.min(i+batchSize, allData.size());
    List<Index_Table> batch = allData.subList(i, end);
    indexService.addAllIndexDetails(batch);
    long batchEndtime = System.currentTimeMillis(); // Log end time for the batch
    System.out.println("Processed batch " + (i / batchSize + 1) + " of size " +
    batch.size() + " in " + (batchEndtime - batchStartTime) + " ms.");
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total processing time: " + (endTime - startTime) + " ms.");
    return "File uploaded and processed successfully. Total time: " + (endTime
    -startTime) + " ms.";
    } catch (Exception e){
    e.printStackTrace();
    return "Error processing file: " + e.getMessage();
    }
    }
    
}
