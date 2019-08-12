package study.planner.model;

/**
 * Interface designed to make the given generic type countable based on integer.
 * @author Adrian Wesolowski
 * @param <T> type which is made countable
 */
public interface Countable<T>
{
    public void setValue(int val);
    public T getValue();
    public T add(int term);
    public T subtract(int sub);
    public boolean greaterOrEqual(T cmp);
}
