package dk.ts.virkr.cvr.integration.model.virksomhed

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by sorenhartvig on 29/05/2017.
 */
class VrvirksomhedWrapper {

  Vrvirksomhed Vrvirksomhed

  @JsonProperty("Vrvirksomhed")
  Vrvirksomhed getVrvirksomhed() {
    return Vrvirksomhed
  }

  void setVrvirksomhed(Vrvirksomhed vrvirksomhed) {
    this.Vrvirksomhed = Vrvirksomhed
  }
}
