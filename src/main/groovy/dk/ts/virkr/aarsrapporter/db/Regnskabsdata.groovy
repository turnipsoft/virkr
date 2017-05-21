package dk.ts.virkr.aarsrapporter.db

import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import dk.ts.virkr.aarsrapporter.util.Utils

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Temporal
import javax.persistence.TemporalType
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Entity(name="regnskabsdata")
class Regnskabsdata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id

    @Column(nullable=false)
    String cvrnummer

    @Column(nullable=false)
    LocalDate startdato

    @Column(nullable=false)
    LocalDate slutdato

    @Column(nullable=false)
    LocalDateTime sidsteopdatering

    @Column
    boolean omgoerelse

    @Column
    String pdfurl

    @Column
    String xbrlurl

    @Column
    Long bruttofortjeneste

    @Column
    Long driftsresultat

    @Column
    Long aaretsresultat

    @Column
    Long resultatfoerskat

    @Column
    Long skatafaaretsresultat

    @Column
    Long gaeldsforpligtelser

    @Column
    Long egenkapital

    public static Regnskabsdata from(RegnskabData rd) {
        Regnskabsdata regnskabsdata = new Regnskabsdata()
        regnskabsdata.cvrnummer = rd.cvrNummer
        regnskabsdata.startdato = Utils.toDate(rd.startDato)
        regnskabsdata.slutdato = Utils.toDate(rd.slutDato)
        regnskabsdata.sidsteopdatering = Utils.toDateTime(rd.sidsteOpdatering)
        regnskabsdata.omgoerelse = rd.omgoerelse
        regnskabsdata.aaretsresultat = rd.aaretsresultat
        regnskabsdata.bruttofortjeneste = rd.bruttofortjeneste
        regnskabsdata.driftsresultat = rd.driftsresultat
        regnskabsdata.egenkapital = rd.egenkapital
        regnskabsdata.gaeldsforpligtelser = rd.gaeldsforpligtelser
        regnskabsdata.resultatfoerskat = rd.resultatfoerskat
        regnskabsdata.skatafaaretsresultat = rd.skatafaaretsresultat
        regnskabsdata.pdfurl = rd.pdfUrl
        regnskabsdata.xbrlurl = rd.xbrlUrl
        return regnskabsdata
    }


    public RegnskabData toJsonModel() {
        RegnskabData regnskabsdata = new RegnskabData()

        regnskabsdata.cvrNummer = this.cvrnummer
        regnskabsdata.startDato = Utils.toString(this.startdato)
        regnskabsdata.slutDato = Utils.toString(this.slutdato)
        regnskabsdata.sidsteOpdatering = Utils.toString(this.sidsteopdatering)
        regnskabsdata.aaretsresultat = this.aaretsresultat
        regnskabsdata.bruttofortjeneste = this.bruttofortjeneste
        regnskabsdata.driftsresultat = this.driftsresultat
        regnskabsdata.egenkapital = this.egenkapital
        regnskabsdata.gaeldsforpligtelser = this.gaeldsforpligtelser
        regnskabsdata.resultatfoerskat = this.resultatfoerskat
        regnskabsdata.skatafaaretsresultat = this.skatafaaretsresultat
        regnskabsdata.pdfUrl = this.pdfurl
        regnskabsdata.xbrlUrl = this.xbrlurl
        regnskabsdata.aar = regnskabsdata.slutDato.substring(0,4)
        regnskabsdata.id = "regnskab_${regnskabsdata.aar}"
        return regnskabsdata
    }
}
