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
  public String cvrnummer

  @Column(nullable = false)
  public LocalDate startdato

  @Column(nullable = false)
  public LocalDate slutdato

  @Column(nullable = false)
  public LocalDateTime sidsteopdatering

  @Column
  public boolean omgoerelse

  @Column
  public String pdfurl

  @Column
  public String xbrlurl

  @Column
  public Long bruttofortjeneste

  @Column
  public Long driftsresultat

  @Column
  public Long aaretsresultat

  @Column
  public Long resultatfoerskat

  @Column
  public Long skatafaaretsresultat

  @Column
  public Long gaeldsforpligtelser

  @Column
  public Long egenkapital

  @Column
  public Long finansielleindtaegter

  @Column
  public Long finansielleomkostninger

  @Column
  public Long medarbejderomkostninger

  @Column
  public Long omsaetning

  @Column
  public Long vareforbrug  // CostOfSales

  @Column
  public Long driftsindtaegter // OtherOperatingIncome

  @Column
  public Long andreeksterneomkostninger //OtherExternalExpenses

  @Column
  public Long regnskabsmaessigeafskrivninger
  //DepreciationAmortisationExpenseAndImpairmentLossesOfPropertyPlantAndEquipmentAndIntangibleAssetsRecognisedInProfitOrLoss

  @Column
  public Long udbytte

  @Column
  public Long variableomkostninger

  @Column
  public Long lokalomkostninger

  @Column
  public Long administrationsomkostninger

  @Column
  public Long kapitalandeleiassocieredevirksomheder

  @Column
  public Long eksterneomkostninger

  @Column
  public String navn

  @Column
  public String vejnavn

  @Column
  public String husnr

  @Column
  public String postnr

  @Column
  public String bynavn

  @Column
  public String lat

  @Column
  public String lon

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
