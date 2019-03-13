package org.bitbucket.ssmkittel.stringdistance;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FuzzyMatcher
{
  private final CCount[] bits;

  FuzzyMatcher(CCount[] bits)
  {
    int bitCount = Arrays.stream(bits).mapToInt(ccount -> ccount.count).sum();
    if (bitCount > 64)
    {
      // Sanity check; we're dealing with Longs, so more than 64 bits would be an error somewhere.
      throw new AssertionError("Bits: " + bits.length);
    }
    this.bits = bits;
  }

  // Signature string which describes the bit pattern used by the matcher.  Can be used to recreate the matcher.
  public String getSignature()
  {
    StringBuilder b = new StringBuilder();
    for (CCount bit : bits)
    {
      short count = bit.count;
      while (count > 0)
      {
        b.append(bit.chr);
        count--;
      }
    }
    return b.toString();
  }

  @Override
  public String toString()
  {
    return getSignature();
  }

  // Determines all matches that are at most threshold distance away from each other
  // Results are returned in natural ordering, with matched pairs sorted within the match as well
  // e.g. Match(a,b), Match(chr,d), Match(d,e)
  public List<Match> matches(Collection<String> candidates, int threshold)
  {
    DString[] wCand = wrap(candidates);

    // range() is (inclusive, exclusive)
    // since we are looking at a minimum of two values, subtract 1 so we ignore the last element in wCand - there is nothing to compare it with
    return IntStream.range(0, wCand.length - 1).parallel().mapToObj(i -> {
      DString current = wCand[i];

      // Values are ordered by length ascending,
      // so the moment the difference in length is greater than the threshold value we cannot get any more matches,
      // as there must be more inserts than are allowed
      int maxLength = current.value.length() + threshold;
      List<Match> matches = new ArrayList<>();
      for (int j = i + 1; j < wCand.length; j++)
      {
        DString check = wCand[j];
        if (check.value.length() > maxLength)
        {
          break;
        }
        Integer distance = current.distance(check, threshold);
        if (distance != null)
        {
          // Apply ordering so Match{first<=second}
          if (current.value.compareTo(check.value) > 0)
          {
            matches.add(new Match(check.value, current.value, distance));
          }
          else
          {
            matches.add(new Match(current.value, check.value, distance));
          }
        }
      }
      return matches;
    }).flatMap(Collection::stream).sorted().collect(Collectors.toList());
  }

  DString wrap(String s)
  {
    // Presence is a tallymark-like system for counting character occurrences in a string, regardless of position
    long presence = 0L;

    Char2ObjectMap<CCount> counts = CCount.decompose(s);
    for (CCount cbits : bits)
    {
      CCount c = counts.get(cbits.chr);

      int marked = c == null ? 0 : c.count;
      int upTo = cbits.count;
      for (int i = 0; i < upTo; i++)
      {
        presence = presence << 1;
        if (marked > 0)
        {
          presence |= 1;
          marked--;
        }
      }
    }
    return new DString(s, presence);
  }

  DString[] wrap(Collection<String> candidates)
  {
    DString[] results = new DString[candidates.size()];
    int i = 0;
    for (String s : candidates)
    {
      results[i] = wrap(s);
      i++;
    }
    Arrays.sort(results);
    return results;
  }
}
