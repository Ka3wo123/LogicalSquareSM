package pl.logicalsquare.IOproject.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.logicalsquare.IOproject.domain.Airplane;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Integer>{
}
