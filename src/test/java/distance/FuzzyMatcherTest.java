package distance;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class FuzzyMatcherTest
{
  private FuzzyMatcher matcher;
  private final List<String> words = Arrays.asList(
          "RAINSTORM",
          "BRAINSTORM",
          "BRAINSTORMS",
          "EGOIST",
          "ENLIST",
          "AGONIST",
          "EGOTIST",
          "EGRETS",
          "EXERTS",
          "GREETS",
          "FERRETS",
          "RECEIVER"
  );

  @BeforeClass
  public void init()
  {
    matcher = MatcherBuilder.matcher("AAABBCCCDDDEEEEFFGGGHHIIIIJKKLLLMMNNNOOOPPQRRRSSSSTTTUUVVWWXYYZZ");
  }

  @Test
  public void wordsMatch1()
  {
    List<Match> matches = matcher.matches(words, 1);
    Assert.assertEquals(matches, Arrays.asList(
            new Match("BRAINSTORM", "BRAINSTORMS", 1),
            new Match("BRAINSTORM", "RAINSTORM", 1),
            new Match("EGOIST", "EGOTIST", 1)
    ), "Matches");
  }

  @Test
  public void wordsMatch2()
  {
    List<Match> matches = matcher.matches(words, 2);
    Assert.assertEquals(matches, Arrays.asList(
            new Match("AGONIST", "EGOIST", 2),
            new Match("AGONIST", "EGOTIST", 2),
            new Match("BRAINSTORM", "BRAINSTORMS", 1),
            new Match("BRAINSTORM", "RAINSTORM", 1),
            new Match("BRAINSTORMS", "RAINSTORM", 2),
            new Match("EGOIST", "EGOTIST", 1),
            new Match("EGOIST", "ENLIST", 2),
            new Match("EGRETS", "EXERTS", 2),
            new Match("EGRETS", "FERRETS", 2),
            new Match("EGRETS", "GREETS", 2)
    ), "Matches");
  }
}
