package com.example.projectend.repository;

import com.example.projectend.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * VAI TRO REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 2: Authentication & Authorization (load role by name)
 */
@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
    VaiTro findByTenVT(String tenVT); // TODO THÀNH VIÊN 2: Dùng trong user registration & security
}
