package org.bitbucket.ssmkittel.stringdistance;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

class CCount
{
  final char chr;
  final short count;

  public CCount(char chr, short count)
  {
    this.chr = chr;
    this.count = count;
  }

  public static Char2ObjectMap<CCount> decompose(CharSequence str)
  {
    Map<Character, Short> m = new HashMap<>();
    for (int i = 0; i < str.length(); i++)
    {
      char chr = str.charAt(i);

      Short val = m.get(chr);
      if (val == null)
      {
        val = 0;
      }
      m.put(chr, (short) (val + 1));
    }

    Char2ObjectMap<CCount> results = new Char2ObjectOpenHashMap<>();
    for (Map.Entry<Character, Short> e : m.entrySet())
    {
      char key = e.getKey();
      results.put(key, new CCount(key, e.getValue()));
    }
    return results;
  }
}
