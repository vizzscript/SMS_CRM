package com.pinnacle.indexing.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pinnacle.indexing.model.Index_Table;
import com.pinnacle.indexing.repository.IndexRepository;
import com.pinnacle.indexing.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexRepository repo;

    @Override
    public Index_Table addIndexDetails(Index_Table index){
        return repo.save(index);
    }
    
    @Override
    public List<Index_Table> getAllIndexDetails(){
        return repo.findAll();
    }



    @Override
    public List<Index_Table> addAllIndexDetails(List<Index_Table> index) {
        return repo.saveAll(index);
    }
}
