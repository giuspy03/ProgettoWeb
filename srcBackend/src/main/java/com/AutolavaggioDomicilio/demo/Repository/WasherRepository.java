package com.AutolavaggioDomicilio.demo.Repository;

import com.AutolavaggioDomicilio.demo.Entity.Washer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasherRepository extends JpaRepository<Washer, Long> {
    //PUOI RIMUOVERE PROBABILMENTE; NON LA USI
    List<Washer> findByWorkArea(String workArea);
    List<Washer> findByVehicleType(String vehicleType);

}
