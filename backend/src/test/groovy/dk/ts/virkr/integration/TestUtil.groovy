package dk.ts.virkr.integration

/**
 * Created by sorenhartvig on 25/05/2017.
 */
class TestUtil {

  public static String load(String filename) {
    return TestUtil.class.getResource(filename).text
  }

}
