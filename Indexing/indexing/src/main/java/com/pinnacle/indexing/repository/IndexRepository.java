package com.pinnacle.indexing.repository;

// import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinnacle.indexing.model.Index_Table;

@Repository
public interface IndexRepository extends JpaRepository<Index_Table, Integer> {
    
}
