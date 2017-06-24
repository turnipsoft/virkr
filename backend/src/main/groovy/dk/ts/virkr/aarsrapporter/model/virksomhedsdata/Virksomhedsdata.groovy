package dk.ts.virkr.aarsrapporter.model.virksomhedsdata

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.model.ModelBase
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 26/05/2017.
 */
class Virksomhedsdata extends ModelBase {
  String cvrnummer
  String navn
  String vejnavn
  String husnr
  String postnr
  String bynavn
  String lat
  String lon

  public static Virksomhedsdata from( Regnskabsdata regnskabsdata) {
    Virksomhedsdata virksomhedsdata = new Virksomhedsdata()
    BeanUtils.copyProperties(regnskabsdata, virksomhedsdata)
    return virksomhedsdata
  }
}
