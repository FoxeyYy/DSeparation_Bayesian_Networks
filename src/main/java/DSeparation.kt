import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import java.util.*

/**
 * @author Héctor Del Campo Pando.
 * D-Separation Algorithm by Köller/Friedman for Bayesian Networks implementation in kotlin language.
 */

enum class Direction { UP, DOWN }

data class TraverseNode<out V>(val node: V, val Direction: Direction)

/**
 * Gets the reachable nodes for a graph, since a node and given a set of observations.
 * This algorithm is the one proposed by Köller and Friedman.
 * @param graph Graph which contains the nodes.
 * @param origin Node from which all trails will start.
 * @param observations Array which contains the nodes that are observed.
 * @return An array containing all nodes that are reachable from the origin.
 */
inline fun <reified V> findReachableNodes(
        graph: DirectedAcyclicGraph<V, DefaultEdge>,
        origin: V,
        observations: Array<V>) : Array<V> {

    //Phase 1: Insert all ancestors of observations into V
    var nodesToVisit = LinkedList<V>() //Nodes to be visited
    nodesToVisit.addAll(observations)

    var ancestors = ArrayList<V>() //Ancestors of Z

    while (nodesToVisit.isNotEmpty()) {
        val node = nodesToVisit.poll() //Select some Y from L
        if (!ancestors.contains(node)) {
            val parents = Graphs.predecessorListOf(graph, node)
            nodesToVisit.addAll(parents) //Y's parents need to be visited
        }
        ancestors.add(node) // Y is ancestor of evidence
    }

    //Phase 2: Traverse active trails starting from X

    var toVisit = LinkedList<TraverseNode<V>>() // (Node, Direction) to be visited
    toVisit.add(TraverseNode(origin, Direction.UP))

    var visited = mutableSetOf<TraverseNode<V>>() // (Node, Direction) marked as visited
    var reachableNodes = mutableSetOf<V>() //Nodes reachable via active trail

    while (toVisit.isNotEmpty()) {
        val node = toVisit.poll() //Select some (Y,d) from L

        if (!visited.contains(node)) {
            if (!observations.contains(node.node)) {
                reachableNodes.add(node.node) // Y is reachable
            }
            visited.add(node) // Mark (Y,d) as visited

            if (node.Direction == Direction.UP &&
                    !observations.contains(node.node)) { //Trail up through Y active if Y not in Z

                val currentParents = Graphs.predecessorListOf(graph, node.node)
                currentParents.forEach { toVisit.add(TraverseNode(it, Direction.UP)) } // Y's parents to be visited from bottom

                val currentChildren = Graphs.successorListOf(graph, node.node)
                currentChildren.forEach { toVisit.add(TraverseNode(it, Direction.DOWN)) } // Y's children to be visited from top

            } else if (node.Direction == Direction.DOWN) { // Trails down through Y

                if (!observations.contains(node.node)) { //Downward trials to Y's children are active
                    val currentChildren = Graphs.successorListOf(graph, node.node)
                    currentChildren.forEach { toVisit.add(TraverseNode(it, Direction.DOWN)) } // Y's children to be visited from top
                }

                if (ancestors.contains(node.node)) { // v-structure trails are active
                    val currentParents = Graphs.predecessorListOf(graph, node.node)
                    currentParents.forEach { toVisit.add(TraverseNode(it, Direction.UP)) } // Y's parents to be visited from bottom

                }
            }
        }
    }

    return reachableNodes.toTypedArray()

}

/**
 * Finds if two nodes are D-Separated.
 * @param graph Graph which contains the nodes.
 * @param origin First node.
 * @param destiny Second node.
 * @param observations Array which contains the nodes that are observed.
 * @return True if both nodes are D-Separated, false otherwise.
 */
inline fun <reified V> areDSeparated(
        graph: DirectedAcyclicGraph<V, DefaultEdge>,
        origin: V,
        destiny: V,
        observations: Array<V>) = !findReachableNodes(graph, origin, observations).contains(destiny)