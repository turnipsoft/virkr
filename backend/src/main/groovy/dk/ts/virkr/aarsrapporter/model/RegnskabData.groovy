package dk.ts.virkr.aarsrapporter.model

import dk.ts.virkr.aarsrapporter.db.Regnskabsdata
import dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata
import dk.ts.virkr.aarsrapporter.util.Utils
import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabData extends ModelBase {

  public RegnskabData() {
    resultatopgoerelse = new Resultatopgoerelse()
    balance = new Balance()
    noter =  new Noter()
    virksomhedsdata = new Virksomhedsdata()
  }

  String id
  String cvrnummer
  String startdato
  String slutdato
  String sidsteopdatering
  String aar
  boolean omgoerelse
  String pdfurl
  String xbrlurl

  Resultatopgoerelse resultatopgoerelse
  Balance balance
  Noter noter
  Virksomhedsdata virksomhedsdata

  public static RegnskabData from(Regnskabsdata rd) {
    RegnskabData regnskabsdata = new RegnskabData()
    regnskabsdata.cvrnummer = rd.cvrnummer
    regnskabsdata.startdato = Utils.toString(rd.startdato)
    regnskabsdata.slutdato = Utils.toString(rd.slutdato)
    regnskabsdata.sidsteopdatering = Utils.toString(rd.sidsteopdatering)
    regnskabsdata.pdfurl = rd.pdfurl
    regnskabsdata.xbrlurl = rd.xbrlurl
    regnskabsdata.omgoerelse = rd.omgoerelse
    regnskabsdata.aar = regnskabsdata.slutdato.substring(0, 4)
    regnskabsdata.id = "regnskab_${regnskabsdata.aar}"

    regnskabsdata.virksomhedsdata = Virksomhedsdata.from(rd)
    regnskabsdata.resultatopgoerelse = Resultatopgoerelse.from(rd)
    regnskabsdata.balance = Balance.from(rd)

    return regnskabsdata
  }

  public void berig(Regnskabsdata regnskabsdata) {
    regnskabsdata.cvrnummer = this.cvrnummer
    regnskabsdata.pdfurl = this.pdfurl
    regnskabsdata.xbrlurl = this.xbrlurl
    regnskabsdata.startdato = Utils.toDate(this.startdato)
    regnskabsdata.slutdato = Utils.toDate(this.slutdato)
    regnskabsdata.sidsteopdatering = Utils.toDateTime(this.sidsteopdatering)
    regnskabsdata.omgoerelse = this.omgoerelse

    this.resultatopgoerelse.berig(regnskabsdata)
    this.balance.berig(regnskabsdata)
    this.virksomhedsdata.berig(regnskabsdata)
  }
}
