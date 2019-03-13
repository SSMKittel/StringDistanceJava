package org.bitbucket.ssmkittel.stringdistance;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FuzzyMatcherTest
{
  private FuzzyMatcher matcher;
  private FuzzyMatcher matcherEmpty;
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
    matcherEmpty = MatcherBuilder.matcher(Collections.emptyList());
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


  @Test
  public void wordsMatchEmpty1()
  {
    List<Match> matches = matcherEmpty.matches(words, 1);
    Assert.assertEquals(matches, Arrays.asList(
            new Match("BRAINSTORM", "BRAINSTORMS", 1),
            new Match("BRAINSTORM", "RAINSTORM", 1),
            new Match("EGOIST", "EGOTIST", 1)
    ), "Matches");
  }

  @Test
  public void wordsMatchEmpty2()
  {
    List<Match> matches = matcherEmpty.matches(words, 2);
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

  @Test
  public void edgeMatch0()
  {
    matcher.matches(Collections.emptyList(), 2);
  }

  @Test
  public void edgeMatch1()
  {
    matcher.matches(Collections.singletonList("A"), 2);
  }

  @Test
  public void edgeMatch2()
  {
    List<Match> matches = matcher.matches(Arrays.asList("A", "A"), 2);
    Assert.assertEquals(matches, Collections.singletonList(
            new Match("A", "A", 0)
    ), "Matches");
  }

  @Test
  public void edgeMatchEmpty0()
  {
    matcherEmpty.matches(Collections.emptyList(), 2);
  }

  @Test
  public void edgeMatchEmpty1()
  {
    matcherEmpty.matches(Collections.singletonList("A"), 2);
  }

  @Test
  public void edgeMatchEmpty2()
  {
    List<Match> matches = matcherEmpty.matches(Arrays.asList("A", "A"), 2);
    Assert.assertEquals(matches, Collections.singletonList(
            new Match("A", "A", 0)
    ), "Matches");
  }
}
