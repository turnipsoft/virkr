package dk.ts.virkr.cvr.integration.model.deltager

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by sorenhartvig on 06/10/2017.
 */
class VrdeltagerpersonWrapper {
  Vrdeltagerperson Vrdeltagerperson

  @JsonProperty("Vrdeltagerperson")
  Vrdeltagerperson getVrdeltagerperson() {
    return Vrdeltagerperson
  }

  void setVrdeltagerperson(Vrdeltagerperson Vrdeltagerperson) {
    this.Vrdeltagerperson = Vrdeltagerperson
  }
}
