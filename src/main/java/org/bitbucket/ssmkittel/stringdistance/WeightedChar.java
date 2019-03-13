package org.bitbucket.ssmkittel.stringdistance;

class WeightedChar implements Comparable<WeightedChar>
{
  final char chr;
  final int weight;

  public WeightedChar(char chr, int weight)
  {
    this.chr = chr;
    this.weight = weight;
  }

  @Override
  public int compareTo(WeightedChar o)
  {
    int c = Integer.compare(this.weight, o.weight);
    if (c != 0)
    {
      return c;
    }
    return Character.compare(this.chr, o.chr);
  }
}