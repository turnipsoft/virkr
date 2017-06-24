package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class Resultatopgoerelse extends ModelBase {

  public OmsaetningTal omsaetningTal
  public BruttoresultatTal bruttoresultatTal
  public NettoresultatTal nettoresultatTal
  public AaretsresultatTal aaretsresultatTal

  public Resultatopgoerelse() {
    this.omsaetningTal = new OmsaetningTal()
    this.bruttoresultatTal = new BruttoresultatTal()
    this.nettoresultatTal = new NettoresultatTal()
    this.aaretsresultatTal = new AaretsresultatTal()
  }

  public static Resultatopgoerelse from(Regnskabsdata rd) {
    Resultatopgoerelse ro = new Resultatopgoerelse()
    OmsaetningTal oms = OmsaetningTal.from(rd)
    ro.omsaetningTal = oms
    ro.bruttoresultatTal = BruttoresultatTal.from(rd)
    ro.nettoresultatTal = NettoresultatTal.from(rd)
    ro.aaretsresultatTal = AaretsresultatTal.from(rd)
    return ro
  }

  public void berig(Regnskabsdata regnskabsdata) {
    this.omsaetningTal.berig(regnskabsdata)
    this.bruttoresultatTal.berig(regnskabsdata)
    this.nettoresultatTal.berig(regnskabsdata)
    this.aaretsresultatTal.berig(regnskabsdata)
  }
}
