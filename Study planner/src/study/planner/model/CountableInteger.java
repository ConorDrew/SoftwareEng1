package study.planner.model;

import java.io.Serializable;

/**
 * Formality class which provides generalised implementation
 * of counting for Integer.
 * @author Adrian Wesolowski
 */
public class CountableInteger implements Countable<Integer>, Serializable
{
    private Integer n;
    public CountableInteger()
    {
        this.n = null;
    }
    public CountableInteger(Integer n)
    {
        this.n = n;
    }
    @Override
    public void setValue(int n)
    {
        this.n = n;
    }
    @Override
    public Integer getValue()
    {
        return n;
    }
    @Override
    public Integer add(int term)
    {
        n += term;
        return n;
    }
    @Override
    public Integer subtract(int sub)
    {
        n -= sub;
        return n;
    }
    @Override
    public boolean greaterOrEqual(Integer cmp)
    {
        return n >= cmp;
    }
    @Override
    public String toString()
    {
        return n.toString();
    }
}
