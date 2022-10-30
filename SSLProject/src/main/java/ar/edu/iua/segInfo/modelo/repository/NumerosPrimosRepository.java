package ar.edu.iua.segInfo.modelo.repository;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumerosPrimosRepository extends JpaRepository<NumeroPrimo, Long> {
}