package com.techbase.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author nguyentanh
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
}
