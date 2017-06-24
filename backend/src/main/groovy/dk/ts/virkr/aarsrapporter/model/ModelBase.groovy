package dk.ts.virkr.aarsrapporter.model

import org.springframework.beans.BeanUtils

/**
 * Created by sorenhartvig on 24/06/2017.
 */
class ModelBase {

  public void berig(RegnskabData rd) {
    BeanUtils.copyProperties(this, rd)
  }
}
