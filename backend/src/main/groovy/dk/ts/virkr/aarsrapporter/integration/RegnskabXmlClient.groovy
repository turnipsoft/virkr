package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.model.RegnskabData
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Dokument
import dk.ts.virkr.aarsrapporter.integration.model.regnskaber.Offentliggoerelse
import dk.ts.virkr.aarsrapporter.parser.RegnskabXmlParser

import java.text.SimpleDateFormat
import java.util.zip.GZIPInputStream

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabXmlClient {

  RegnskabXmlParser parser = new RegnskabXmlParser()

  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  List<RegnskabData> hentRegnskabData(List<Offentliggoerelse> offentliggoerelser) {
    List<RegnskabData> regnskabdata = []

    offentliggoerelser.each { offentliggoerelse ->
      if (offentliggoerelse.offentliggoerelsestype == 'regnskab') {
        Dokument dokument = offentliggoerelse.dokumenter.find {
          it.dokumentMimeType == 'application/xml' && it.dokumentType == 'AARSRAPPORT'
        }
        if (dokument) {
          byte[] bytes = new URL(dokument.dokumentUrl).getBytes()
          GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))
          String unzippedData = new String(gzipInputStream.bytes, 'UTF-8')
          RegnskabData data = new RegnskabData()
          data.cvrnummer = offentliggoerelse.cvrNummer
          data.omgoerelse = offentliggoerelse.omgoerelse
          data.pdfurl = offentliggoerelse.dokumenter.find {
            it.dokumentMimeType == 'application/pdf' && it.dokumentType == 'AARSRAPPORT'
          }?.dokumentUrl
          data.xbrlurl = offentliggoerelse.dokumenter.find {
            it.dokumentMimeType == 'application/xml' && it.dokumentType == 'AARSRAPPORT'
          }?.dokumentUrl
          data.startdato = offentliggoerelse.regnskab.regnskabsperiode.startDato
          data.slutdato = offentliggoerelse.regnskab.regnskabsperiode.slutDato
          data.sidsteopdatering = sdf.format(offentliggoerelse.sidstOpdateret)
          data = parser.parseOgBerig(data, unzippedData)
          data.virksomhedsdata = parser.hentVirksomhedsdataFraRegnskab(unzippedData, data)
          regnskabdata << data
        }
      }
    }

    return regnskabdata
  }
}
