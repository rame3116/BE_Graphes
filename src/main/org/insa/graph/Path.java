package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a path between nodes in a graph.
 * 
 * A path is represented as a list of {@link Arc} and not a list of {@link Node}
 * due to the multigraph nature of the considered graphs.
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        
        // pr utiliser le bon constructeur
        if (nodes.size() == 1) {
        	return new Path(graph, nodes.get(0));
        }
        
        if (nodes.size() >= 2) {
	        boolean hasRightSuccessor;
	        Arc a = null;
	        for (int i = 0; i < nodes.size() - 1; ++i) {
	        	Node n = nodes.get(i);
	        	Node n2 = nodes.get(i+1);
	        	double min = Double.MAX_VALUE;
	        	hasRightSuccessor = false;
	        	for (Arc arc: n) {
	        		if (arc.getDestination() == n2) {
	        			hasRightSuccessor = true;
	        			if (min > arc.getMinimumTravelTime()) {
	        				min = arc.getMinimumTravelTime();
	        				a = arc;
	        			}
	        		}
	        	}
	        	if (hasRightSuccessor == false) {
	        		throw new IllegalArgumentException("The list of nodes is not valid");
	        	}
	        	else {
	        		arcs.add(a);
	        	}
	        }
        }
        return new Path(graph, arcs);
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        
        // pr utiliser le bon constructeur
        if (nodes.size() == 1) {
        	return new Path(graph, nodes.get(0));
        }
        
        if (nodes.size() >= 2) {
	        boolean hasRightSuccessor;
	        Arc a = null;
	        for (int i = 0; i < nodes.size() - 1; ++i) {
	        	Node n = nodes.get(i);
	        	Node n2 = nodes.get(i+1);
	        	double min = Double.MAX_VALUE;
	        	hasRightSuccessor = false;
	        	for (Arc arc: n) {
	        		if (arc.getDestination() == n2) {
	        			hasRightSuccessor = true;
	        			if (min > arc.getLength()) {
	        				min = arc.getLength();
	        				a = arc;
	        			}
	        		}
	        	}
	        	if (hasRightSuccessor == false) {
	        		throw new IllegalArgumentException("The list of nodes is not valid");
	        	}
	        	else {
	        		arcs.add(a);
	        	}
	        }
        }
        return new Path(graph, arcs);
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     */
    public boolean isValid() {
        if (this.isEmpty() || (this.arcs.size() == 0 && this.size() == 1))
        	return true;
        Arc a = this.arcs.get(0);
        Arc a2 = this.arcs.get(1);
        for (int i = 1; i < this.arcs.size() - 1; ++i) {
        	if (a.getDestination() != a2.getOrigin())
        		return false;
        	else {
        		a = a2;
        		a2 = this.arcs.get(i+1);
        	}
        }
        return true;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     */
    public float getLength() {
    	float result = 0;
        for (Arc a : arcs) {
        	result += a.getLength();
        }
        return result;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     */
    public double getTravelTime(double speed) {
        double result = 0;
        for (Arc a : arcs) {
        	result += a.getTravelTime(speed);
        }
        return result;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     */
    public double getMinimumTravelTime() {
        double result = 0;
        for (Arc a : arcs) {
        	result += a.getMinimumTravelTime();
        }
        return result;
    }

}
