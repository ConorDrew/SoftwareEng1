package study.planner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import org.jgrapht.graph.*;

/**
 * Associate class for representing Task dependencies to the model and, ultimately,
 * the user.
 * @author Adrian Wesolowski
 */
public class TaskDependencyGraph implements Serializable 
{
    private DirectedAcyclicGraph<Task, DefaultEdge> graph;
    
    TaskDependencyGraph()
    {
        graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
    }
    /**
     * Adds a task vertex
     * @param t
     * @return true if successful
     */
    public boolean addTask(Task t)
    {
        return graph.addVertex(t);
    }
    /**
     * Adds a task and its dependency vertex and then attempts to draw an edge
     * indicating the dependency.
     * @param t
     * @param dep
     * @return true if successful, false if not
     */
    public boolean addTask(Task t, Task dep)
    {
        if(dep == null)
        {
            graph.addVertex(t);
        }
        else
        {
            boolean existed = !graph.addVertex(t);
            graph.addVertex(dep);
            try
            {
                graph.addEdge(t, dep);
            }
            catch(IllegalArgumentException ex)
            {
                graph.removeVertex(dep);
                if(!existed)
                    graph.removeVertex(t);
                return false;
            }
            t.addDependency(dep);
            //t.updateDependencies();
        }
        return true;
    }
    /**
     * Removes the task vertex from the graph
     * @param t
     * @return true if present and removed, false if not
     */
    public boolean removeTask(Task t)
    {
        if(graph.removeVertex(t))
        {
            
        }
        return true;
    }
    /**
     * Set a dependency relationship between a parent and a child
     * @param parent
     * @param child
     * @return true if possible, false if not
     */
    public boolean setDependency(Task parent, Task child)
    {
        DefaultEdge e = null;
        try
        {
             e = graph.addEdge(parent, child);
        }
        catch(IllegalArgumentException ex)
        {
            return false;
        }
        if(e != null)
        {
            parent.addDependency(child);
            return true;
        }
        else
            return false;
    }
    /**
     * Gets all the dependencies concerning a task
     * @param t
     * @return list of dependencies of the given task
     */
    public ArrayList<Task> getDependencies(Task t)
    {
        Set<Task> s = graph.getDescendants(t);
        ArrayList<Task> result = new ArrayList<>();
        for(Task i : s)
        {
            result.add(i);
        }
        return result;
    }
}
