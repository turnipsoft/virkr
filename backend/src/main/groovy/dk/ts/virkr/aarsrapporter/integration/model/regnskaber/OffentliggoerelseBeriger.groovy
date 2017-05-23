package dk.ts.virkr.aarsrapporter.integration.model.regnskaber

import java.text.SimpleDateFormat

/**
 * Created by sorenhartvig on 22/06/16.
 */
class OffentliggoerelseBeriger {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")

    static Offentliggoerelse berig(Offentliggoerelse offentliggoerelse) {
        offentliggoerelse.regnskab.regnskabsperiode.slutDatoDate =
                sdf.parse(offentliggoerelse.regnskab.regnskabsperiode.slutDato)
        offentliggoerelse.regnskab.regnskabsperiode.startDatoDate =
                sdf.parse(offentliggoerelse.regnskab.regnskabsperiode.startDato)
        return offentliggoerelse
    }
}
