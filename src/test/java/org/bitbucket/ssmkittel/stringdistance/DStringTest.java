package org.bitbucket.ssmkittel.stringdistance;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DStringTest
{
  @DataProvider
  public Object[][] editsInput()
  {
    return new Object[][]{
            {"", "", 0},
            {"A", "", 1},
            {"A", "A", 0},
            {"", "A", 1},
            {"", "AB", 2},
            {"AB", "AB", 0},
            {"AB", "", 2},
            {"", "ABC", 3},
            {"ABC", "", 3},
            {"ABC", "ABC", 0},
            {"ABC", "BC", 1},
            {"ABC", "CB", 2},
            {"BC", "ABC", 1},
            {"CB", "ABC", 2},
            {"EPLYYYYYMDVWGK", "ELRPYYYYYMDVWGK", 2},
            {"ELRPYYYYYMDVWGK", "EPLYYYYYMDVWGK", 2},
            {"ADLTTGAR", "DPALTTGAR", 2},
            {"DPALTTGAR", "ADLTTGAR", 2},
            {"NDLWFR", "DGNLWFR", 2},
            {"DGNLWFR", "NDLWFR", 2},
    };
  }

  @Test(dataProvider = "editsInput")
  public void edits(String first, String second, int distance)
  {
    assertEquals(new DString(first, 0L).edits(new DString(second, 0L)), distance, "distance");
  }

  @Test(dataProvider = "editsInput")
  public void distance(String first, String second, Integer distance)
  {
    assertEquals(new DString(first, 0L).distance(new DString(second, 0L), 3), distance, "distance");
  }
}
