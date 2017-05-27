package dk.ts.virkr.aarsrapporter.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by sorenhartvig on 19/05/2017.
 */
interface RegnskabsdataRepository extends JpaRepository<Regnskabsdata, Long> {

  List<Regnskabsdata> findAllByCvrnummerAndStartdatoAndSlutdato(String cvrnummer, LocalDate startdato,
                                                                LocalDate slutdato)

  List<Regnskabsdata> findAllByCvrnummerOrderByStartdato(String cvrnummer)

  List<Regnskabsdata> findAllBySidsteopdateringGreaterThan(LocalDateTime sidsteOpdatering)

  @Query('''select rd from regnskabsdata rd 
            where rd.sidsteopdatering = (select max(rd2.sidsteopdatering) 
            from regnskabsdata rd2 where rd2.cvrnummer = rd.cvrnummer) 
            and rd.sidsteopdatering < :sidsteopdatering''')
  List<Regnskabsdata> hentSenesteRegnskabstal(@Param("sidsteopdatering") LocalDateTime sidsteopdatering)


}
