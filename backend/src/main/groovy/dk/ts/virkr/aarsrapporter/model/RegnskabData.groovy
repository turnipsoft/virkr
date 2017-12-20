package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import dk.ts.virkr.aarsrapporter.util.Utils
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabData {

  RegnskabData() {
    virksomhedsdata = new Virksomhedsdata()
  }

  int antalRegnskaber
  String id
  String cvrnummer
  String sidsteopdatering
  String offentliggoerelsestidspunkt
  String indlaesningstidspunkt
  boolean omgoerelse
  String pdfurl
  String xbrlurl
  String startdato
  String slutdato
  String aar

  Virksomhedsdata virksomhedsdata

  boolean findesTal

  Regnskab aktueltAarsregnskab
  Regnskab sidsteAarsregnskab

}
