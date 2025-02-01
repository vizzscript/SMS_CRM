package com.pinnacle.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinnacle.backend.model.UserPayloadModel;

@Repository
public interface UserPayloadModelRepository extends JpaRepository<UserPayloadModel, Long> {

}
