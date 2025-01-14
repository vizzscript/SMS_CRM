package com.pinnacle.indexing.service;
import java.util.*;
import com.pinnacle.indexing.model.Index_Table;
// import com.pinnacle.indexing.model.Index_Table;

public interface IndexService {
    public Index_Table addIndexDetails(Index_Table index);
    public List<Index_Table> getAllIndexDetails();
    public List<Index_Table> addAllIndexDetails(List<Index_Table> index);
}
