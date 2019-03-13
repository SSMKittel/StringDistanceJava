package org.bitbucket.ssmkittel.stringdistance;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ShortMap;
import it.unimi.dsi.fastutil.chars.Char2ShortOpenHashMap;

import java.util.*;

public class MatcherBuilder
{
  private int total;
  private Char2ObjectMap<List<Integer>> counts = new Char2ObjectOpenHashMap<>();

  public static FuzzyMatcher matcher(String signature)
  {
    if (signature.length() > 64)
    {
      throw new IllegalArgumentException("Signature too large");
    }
    // Special logic isn't needed; as long as it fits within 64 bits,
    // the normal algorithm will reconstruct the bit pattern without errors
    MatcherBuilder matcherBuilder = new MatcherBuilder();
    matcherBuilder.update(signature);
    return matcherBuilder.build();
  }

  // Convenience method for just creating a matcher from a set of data.
  public static FuzzyMatcher matcher(Collection<? extends CharSequence> strs)
  {
    MatcherBuilder matcherBuilder = new MatcherBuilder();
    matcherBuilder.update(strs);
    return matcherBuilder.build();
  }

  public void update(Collection<? extends CharSequence> strs)
  {
    for (CharSequence str : strs)
    {
      update(str);
    }
  }

  public void update(CharSequence str)
  {
    if (str.length() == 0)
    {
      return;
    }
    for (CCount cc : CCount.decompose(str).values())
    {
      List<Integer> vals = counts.computeIfAbsent(cc.chr, ArrayList::new);
      while (vals.size() < cc.count)
      {
        // Insert zeros until the array of counts can support the value we want to insert
        vals.add(0);
      }
      // We can't have a count of 0, so count positions need to be offset by 1
      vals.set(cc.count - 1, vals.get(cc.count - 1) + 1);
    }
    total++;
  }

  // A map of
  Char2ObjectMap<Queue<Integer>> computeCosts()
  {
    Char2ObjectMap<Queue<Integer>> allCosts = new Char2ObjectOpenHashMap<>();
    for (Char2ObjectMap.Entry<List<Integer>> e : counts.char2ObjectEntrySet())
    {
      char ch = e.getCharKey();
      List<Integer> count = e.getValue();

      // Including the total number of strings used to compute the weights simplifies the cost calculation
      int[] cost = new int[1 + count.size()];
      cost[0] = total;
      int i = 1;
      for (int countVal : count)
      {
        cost[i] = countVal;
        i++;
      }

      // Back-fill the array so we are using running totals
      // e.g. when a character appears 4 times in a string, also say that it appears 3, 2, and 1 times in the same string
      // This also means that cost[i] >= cost[i + 1]
      for (i = cost.length - 1; i > 1; i--)
      {
        cost[i - 1] = cost[i - 1] + cost[i];
      }

      // Simple algorithm to determine cost values, by only looking at one character on its own
      // A character that never appears in any of our strings is just as useless as a character that always appears
      // A better choice is characters which appear 50% of the time, as it can cut out half of the values we are comparing
      // Similarly, a choice which halves 4 to 2 is less useful than one which halves 100 to 50
      // So the cost is determined by multiplying the size of the subset of words that it pertains to,
      // with a fraction determined by how close the reduction is to half the subset's size
      for (i = cost.length - 1; i > 1; i--)
      {
        int previous = cost[i - 1];
        int halfpoint = previous / 2; // Rounding is irrelevant
        int current = cost[i];
        if (current > halfpoint)
        {
          // Drop it below the half point so it has roughly the same distance to the halfpoint, but is smaller than it
          current = previous - current;
        }
        double ranking = current / (double) halfpoint;
        cost[i] = (int) Math.round(ranking * previous);
      }

      ArrayDeque<Integer> costQ = new ArrayDeque<>(cost.length - 1);
      // First element in the cost array was used for setup only and is ignored
      for (i = 1; i < cost.length; i++)
      {
        costQ.add(cost[i]);
      }
      allCosts.put(ch, costQ);
    }
    return allCosts;
  }

  public FuzzyMatcher build()
  {
    Char2ObjectMap<Queue<Integer>> costs = computeCosts();

    // Setup a priority queue in largest-first order, with the first cost value for each character
    PriorityQueue<WeightedChar> q = new PriorityQueue<>(Collections.reverseOrder());
    for (Char2ObjectMap.Entry<Queue<Integer>> e : costs.char2ObjectEntrySet())
    {
      q.add(new WeightedChar(e.getCharKey(), e.getValue().remove()));
    }

    Char2ShortMap bitCounts = new Char2ShortOpenHashMap();
    int iterations = 64; // bits in a Long
    while (iterations > 0 && !q.isEmpty())
    {
      WeightedChar wc = q.remove();
      // Increment bit count for the character
      bitCounts.put(wc.chr, (short) (bitCounts.getOrDefault(wc.chr, (short) 0) + 1));
      Queue<Integer> vals = costs.get(wc.chr);
      if (vals != null && !vals.isEmpty())
      {
        // Costs queue still has values to go through, put it back into the priority queue
        q.add(new WeightedChar(wc.chr, vals.remove()));
      }
      iterations--;
    }

    // Flatten the bitCounts map into an array of (character, bits)
    CCount[] bits = new CCount[bitCounts.size()];
    int i = 0;
    for (Char2ShortMap.Entry e : bitCounts.char2ShortEntrySet())
    {
      bits[i] = new CCount(e.getCharKey(), e.getShortValue());
      i++;
    }
    Arrays.sort(bits, Comparator.comparingInt(a -> a.chr));
    return new FuzzyMatcher(bits);
  }
}
