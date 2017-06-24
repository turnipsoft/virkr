package dk.ts.virkr.aarsrapporter.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by sorenhartvig on 26/05/2017.
 */
@Entity
class Virksomhedsdata {

  @Id
  String cvrnummer

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

  static Virksomhedsdata from(dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata vd) {
    Virksomhedsdata v = new Virksomhedsdata()
    v.cvrnummer = vd.cvrnummer
    v.navn = vd.navn
    v.vejnavn = vd.vejnavn
    v.husnr = vd.husnr
    v.postnr = vd.postnr
    v.bynavn = vd.bynavn
    v.lat = vd.lat
    v.lon = vd.lon

    return v;
  }

  dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata tilVirksomhedsdata() {
    dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata v =
      new dk.ts.virkr.aarsrapporter.model.virksomhedsdata.Virksomhedsdata()

    v.cvrnummer = this.cvrnummer
    v.navn = this.navn
    v.vejnavn = this.vejnavn
    v.husnr = this.husnr
    v.postnr = this.postnr
    v.bynavn = this.bynavn
    v.lat = this.lat
    v.lon = this.lon

    return v;
  }
}
