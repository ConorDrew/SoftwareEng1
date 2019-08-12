package study.planner.model;

import java.io.Serializable;
import java.time.Duration;

/**
 * Class mapping designed to generalise required counting methods for
 * java.time.Duration
 * @author Adrian Wesolowski
 */
public class CountableDuration implements Countable<Duration>, Serializable
{
    private Duration duration;
    public CountableDuration()
    {
        this.duration = null;
    }
    public CountableDuration(Duration duration)
    {
        this.duration = duration;
    }
    @Override
    public Duration getValue()
    {
        return duration;
    }
    @Override
    public void setValue(int n)
    {
        this.duration = Duration.ofMinutes(n);
    }
    @Override
    public Duration add(int term)
    {
       Duration convert = Duration.ofMinutes(term);
       duration = duration.plus(convert);
       return duration;
    }
    @Override
    public Duration subtract(int sub)
    {
        Duration convert = Duration.ofMinutes(sub);
        duration = duration.minus(convert);
        return duration;
    }
    @Override
    public boolean greaterOrEqual(Duration cmp)
    {
        Duration diff = duration.minus(cmp);
        return !diff.isNegative();
    }
    @Override
    public String toString()
    {
        return duration.toString();
    }
}
