package com.turkeycrew;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<LegacyData, Integer> {
}