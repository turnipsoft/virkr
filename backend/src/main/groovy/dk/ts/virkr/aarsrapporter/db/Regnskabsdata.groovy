package dk.ts.virkr.aarsrapporter.db

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Resultatopgoerelse
import dk.ts.virkr.aarsrapporter.util.Utils

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Entity(name = "regnskabsdata")
class Regnskabsdata {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id

  @Column(nullable = false)
  String cvrnummer

  @Column(nullable = false)
  LocalDate startdato

  @Column(nullable = false)
  LocalDate slutdato

  @Column(nullable = false)
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

  @Column
  Long finansielleindtaegter

  @Column
  Long finansielleomkostninger

  @Column
  Long medarbejderomkostninger

  @Column
  Long omsaetning

  @Column
  Long vareforbrug  // CostOfSales

  @Column
  Long driftsindtaegter // OtherOperatingIncome

  @Column
  Long andreeksterneomkostninger //OtherExternalExpenses

  @Column
  Long regnskabsmaessigeafskrivninger
  //DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss

  @Column
  Long udbytte

  @Column
  Long variableomkostninger

  @Column
  Long lokalomkostninger

  @Column
  Long administrationsomkostninger

  @Column
  Long eksterneomkostninger

  @Column
  String navn

  @Column
  String vejnavn

  @Column
  String husnr

  @Column
  String postnr

  @Column
  String bynavn

  @Column
  String lat

  @Column
  String lon

  public static Regnskabsdata from(RegnskabData rd) {
    Regnskabsdata regnskabsdata = new Regnskabsdata()
    rd.berig(regnskabsdata)

    return regnskabsdata
  }


  public RegnskabData toJsonModel() {
    RegnskabData regnskabsdata = RegnskabData.from(this)
    return regnskabsdata
  }
}
