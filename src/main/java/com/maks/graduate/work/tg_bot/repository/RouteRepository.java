package com.maks.graduate.work.tg_bot.repository;

import com.maks.graduate.work.tg_bot.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
