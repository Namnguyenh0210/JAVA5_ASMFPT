package com.example.projectend.repository;

import com.example.projectend.entity.TrangThaiDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrangThaiDonHangRepository extends JpaRepository<TrangThaiDonHang, Integer> {
    Optional<TrangThaiDonHang> findByTenTTDH(String tenTTDH);
}
