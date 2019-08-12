package study.planner.model;

/**
 * Interface for classes which requires implementation of various scheduling and
 * other time related methods.
 * @author Adrian Wesolowski
 * @param <T> class used to represent a date
 */
public interface Schedulable<T> 
{
    public void setDeadline(T deadline);
    public void setDeadline(String deadline);
    public T getDeadline();
    public boolean isDue();
}