package dk.ts.virkr.aarsrapporter.db

import org.springframework.data.jpa.repository.JpaRepository

import java.time.LocalDate

/**
 * Created by sorenhartvig on 19/05/2017.
 */
interface RegnskabsdataRepository extends JpaRepository<Regnskabsdata, Long> {

    List<Regnskabsdata> findAllByCvrnummerAndStartdatoAndSlutdato(String cvrnummer, LocalDate startdato,
                                                                  LocalDate slutdato)

    List<Regnskabsdata> findAllByCvrnummerOrderByStartdato(String cvrnummer)

}
