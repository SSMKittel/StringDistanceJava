package org.bitbucket.ssmkittel.stringdistance;

import java.util.Objects;

public class Match implements Comparable<Match>
{
  public final String first;
  public final String second;
  public final int distance;

  public Match(String first, String second, int distance)
  {
    this.first = first;
    this.second = second;
    this.distance = distance;
  }

  @Override
  public int compareTo(Match o)
  {
    int c = first.compareTo(o.first);
    if (c != 0)
    {
      return c;
    }
    c = second.compareTo(o.second);
    if (c != 0)
    {
      return c;
    }
    return Integer.compare(distance, o.distance);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    Match match = (Match) o;
    return distance == match.distance &&
            Objects.equals(first, match.first) &&
            Objects.equals(second, match.second);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(first, second, distance);
  }

  @Override
  public String toString()
  {
    return "Match{" +
            "first='" + first + '\'' +
            ", second='" + second + '\'' +
            ", distance=" + distance +
            '}';
  }
}
