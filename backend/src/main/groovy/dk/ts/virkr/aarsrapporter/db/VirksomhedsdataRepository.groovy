package dk.ts.virkr.aarsrapporter.db

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by sorenhartvig on 26/05/2017.
 */
interface VirksomhedsdataRepository extends JpaRepository<Virksomhedsdata, String> {

  Virksomhedsdata findByCvrnummer(String cvrnummer)

}
