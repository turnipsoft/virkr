package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class RegnskabstalKontrolResultat {

  enum RegnskabstalKontrolType {
    REGNSKABSTAL_AFVIGER_FRA_NAESTE_AARS_TAL
  }

  RegnskabstalKontrolType kontrolType
  String text
  boolean fejl
}
