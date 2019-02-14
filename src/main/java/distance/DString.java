package distance;

import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;

import java.util.Objects;

class DString implements Comparable<DString>
{
  public final String value;
  private final long presence;

  DString(String value, long presence)
  {
    this.value = value;
    this.presence = presence;
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
    DString dString = (DString) o;
    return presence == dString.presence && Objects.equals(value, dString.value);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(value, presence);
  }

  public Integer distance(DString other, int threshold)
  {
    // The number of tolerable differences can be double what is shown
    // e.g. AC -> DE is an edit of 2, but the presence xor is a difference of 4
    if (Long.bitCount(presence ^ other.presence) > (2 * threshold))
    {
      return null;
    }
    int edits = edits(other);
    return edits <= threshold ? edits : null;
  }

  // Modified from https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/Damerau.java
  int edits(DString other)
  {
    String s1 = this.value;
    String s2 = other.value;
    // INFinite distance is the max possible distance
    int inf = s1.length() + s2.length();

    // Create and initialize the character array indices
    Char2IntOpenHashMap da = new Char2IntOpenHashMap();

    for (int d = 0; d < s1.length(); d++)
    {
      da.put(s1.charAt(d), 0);
    }

    for (int d = 0; d < s2.length(); d++)
    {
      da.put(s2.charAt(d), 0);
    }

    // Create the distance matrix H[0 .. value.length+1][0 .. other.length+1]
    int[][] h = new int[s1.length() + 2][s2.length() + 2];

    // initialize the left and top edges of H
    for (int i = 0; i <= s1.length(); i++)
    {
      h[i + 1][0] = inf;
      h[i + 1][1] = i;
    }

    for (int j = 0; j <= s2.length(); j++)
    {
      h[0][j + 1] = inf;
      h[1][j + 1] = j;
    }

    // fill in the distance matrix H
    // look at each character in value
    for (int i = 1; i <= s1.length(); i++)
    {
      int db = 0;

      // look at each character in b
      for (int j = 1; j <= s2.length(); j++)
      {
        int i1 = da.get(s2.charAt(j - 1));
        int j1 = db;

        int cost = 1;
        if (s1.charAt(i - 1) == s2.charAt(j - 1))
        {
          cost = 0;
          db = j;
        }

        h[i + 1][j + 1] = min(
                h[i][j] + cost, // substitution
                h[i + 1][j] + 1, // insertion
                h[i][j + 1] + 1, // deletion
                h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
      }

      da.put(s1.charAt(i - 1), i);
    }

    return h[s1.length() + 1][s2.length() + 1];
  }

  private static int min(int a, int b, int c, int d)
  {
    return Math.min(a, Math.min(b, Math.min(c, d)));
  }

  @Override
  public int compareTo(DString o)
  {
    int c = Integer.compare(this.value.length(), o.value.length());
    if (c != 0)
    {
      return c;
    }
    return this.value.compareTo(o.value);
  }
}
