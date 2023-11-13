package com.hrproject.repository;

import com.hrproject.repository.entity.Avanstelebi;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAvansRepository extends JpaRepository<Avanstelebi, Long> {



}