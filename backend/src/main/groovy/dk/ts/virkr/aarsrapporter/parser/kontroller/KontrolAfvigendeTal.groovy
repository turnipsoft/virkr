package dk.ts.virkr.aarsrapporter.parser.kontroller

import dk.ts.virkr.aarsrapporter.model.Passiver
import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.model.Regnskabstal
import dk.ts.virkr.aarsrapporter.model.RegnskabstalKontrolResultat

import java.text.NumberFormat
import java.time.LocalDate

/**
 * Created by sorenhartvig on 15/12/2017.
 */
class KontrolAfvigendeTal implements RegnskabstalKontrol {

  @Override
  void kontrollerOgBerig(List<RegnskabData> regnskabData) {

    List<RegnskabData> sorteretListe = regnskabData.sort{it.aktueltAarsregnskab.slutdato}

    for (int i=0;i<sorteretListe.size();i++) {
      if (i<sorteretListe.size()-1) {
        // så sammenlignes der forrige års tal i det aktuelle regskab med forrige års aktuelle tal
        diffOgBerigObject(sorteretListe[(i+1)].sidsteAarsregnskab,
          sorteretListe[i].aktueltAarsregnskab)

      }
    }
  }

  void diffOgBerigObject(Object aktuelt, Object sidsteAar) {
    if (aktuelt && sidsteAar) {
      if (aktuelt instanceof Regnskabstal) {
        diffOgBerig(aktuelt, sidsteAar)
        return
      }

      if (!((aktuelt instanceof String) || (aktuelt instanceof LocalDate))) {
        // så er det et objekt og vi kører rekursivt
        aktuelt.properties.each { p->
          if(p.key in ["metaClass","class", "egenkapital", "gaeldsforpligtelser"]) return

          Object av = aktuelt[p.key]
          Object sv = sidsteAar[p.key]
          diffOgBerigObject(av, sv)
        }
      }
    }
  }

  static NF = NumberFormat.getNumberInstance(Locale.GERMAN)

  void diffOgBerig(Regnskabstal aktuelt, Regnskabstal sidsteAar) {
    if (aktuelt == null || sidsteAar == null || aktuelt.vaerdi==null || sidsteAar.vaerdi==null) {
      return
    }

    if (aktuelt.vaerdi != sidsteAar.vaerdi) {
      // præcision skal afgøres udfra decimal
      long precision = Math.pow(10,Math.abs(aktuelt.decimal))
      long rest = sidsteAar.vaerdi%precision
      long beregnetVaerdi = sidsteAar.vaerdi - rest

      beregnetVaerdi = Math.abs(beregnetVaerdi)
      long aktuelVaerdi = Math.abs(aktuelt.vaerdi)

      if (beregnetVaerdi==aktuelVaerdi || beregnetVaerdi+precision == aktuelVaerdi) {
        // alt ok, så er tallet afrundet med den forventede precision
        return
      }
      // der er en difference
      String text = "Der er registreret et andet tal i sidste års tal, i efterfølgende årsregnskab. ${NF.format(aktuelt.vaerdi)} afviger fra ${NF.format(sidsteAar.vaerdi)}"
      RegnskabstalKontrolResultat resultat = new RegnskabstalKontrolResultat()
      resultat.kontrolType = RegnskabstalKontrolResultat.RegnskabstalKontrolType.REGNSKABSTAL_AFVIGER_FRA_NAESTE_AARS_TAL
      resultat.text = text
      sidsteAar.tilfoejKontrol(resultat)
      return
    }
  }
}
