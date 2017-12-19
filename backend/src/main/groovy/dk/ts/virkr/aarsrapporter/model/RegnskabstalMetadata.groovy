package dk.ts.virkr.aarsrapporter.model

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class RegnskabstalMetadata {
  List<RegnskabstalKontrolResultat> kontroller = []
  boolean harRegnskabstalKontroller() {
    return this.kontroller.size() > 0
  }
}
