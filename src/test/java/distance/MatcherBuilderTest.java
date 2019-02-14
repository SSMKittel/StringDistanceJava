package distance;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class MatcherBuilderTest
{
  @Test
  public void randomStrings()
  {
    FuzzyMatcher f = MatcherBuilder.matcher(Arrays.asList(
            "HAAVGCA",
            "VQQOLBT",
            "VGOPXJT",
            "ZKDEGLB",
            "KUFQKUC",
            "OPDXAAF",
            "YTSAHCY",
            "SQTSQKC",
            "YDCPBTY",
            "UAVJKPY",
            "YPLZTWY",
            "EJGXVWR",
            "RRDTSVG",
            "JVEKZZL",
            "KAZQYDL",
            "NJEPOWE",
            "HRQJZTW",
            "LQWINPT",
            "WDZWDAW",
            "PEZHQGI",
            "TVQGZVVOZVUCTDK",
            "TXXDOJDQHJGXCAC",
            "FZSDAIFPSSIGHSM",
            "JTIYUHFSLKIRDBD",
            "WYFMXYBAJDVPKOI",
            "POLPNKOGMEJZTEU",
            "HDLBEEKYKRSUVQY",
            "OOQECMVHUGEXISF",
            "MJOZGUPVKOHCSRP",
            "DPQRCNYRRBOFHGZ"
    ));
    Assert.assertEquals(f.getSignature(), "AAABCCDDEEFFGHIIJJKKLMNOOPPQQRRRSSSSTTUUVVVVWWWXXXYYZZ");
  }

  @Test
  public void alphabeticallySorted()
  {
    FuzzyMatcher f = MatcherBuilder.matcher(Arrays.asList(
            "AAACGHV",
            "AADFOPX",
            "ABDFIJKMOPVWXYY",
            "ACCDDGHJJOQTXXX",
            "ACHSTYY",
            "ADDWWWZ",
            "ADFFGHIIMPSSSSZ",
            "ADKLQYZ",
            "AJKPUVY",
            "BCDFGHNOPQRRRYZ",
            "BCDPTYY",
            "BDDFHIIJKLRSTUY",
            "BDEEHKKLQRSUVYY",
            "BDEGKLZ",
            "BLOQQTV",
            "CDGKOQTTUVVVVZZ",
            "CEEFGHIMOOQSUVX",
            "CFKKQUU",
            "CGHJKMOOPPRSUVZ",
            "CKQQSST",
            "DGRRSTV",
            "EEGJKLMNOOPPTUZ",
            "EEJNOPW",
            "EGHIPQZ",
            "EGJRVWX",
            "EJKLVZZ",
            "GJOPTVX",
            "HJQRTWZ",
            "ILNPQTW",
            "LPTWYYZ"
    ));
    Assert.assertEquals(f.getSignature(), "AAABCCDDEEFFGHIIJJKKLMNOOPPQQRRRSSSSTTUUVVVVWWWXXXYYZZ");
  }

  @Test
  public void noInput()
  {
    Assert.assertEquals(new MatcherBuilder().build().getSignature(), "");
  }

  @Test
  public void pruned()
  {
    FuzzyMatcher f = MatcherBuilder.matcher(Arrays.asList(
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_abcdefghijklmnopqrstuvwxyz",
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_abcdefghijklmnopqrstuvwxyz",
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_abcdefghijklmnopqrstuvwxyz",
            "+",
            "\\",
            "/"
    ));
    Assert.assertEquals(f.getSignature(), "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz");
  }
}
