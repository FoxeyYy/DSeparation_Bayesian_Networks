import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.Assert
import org.junit.Test

/**
 * Sample tests for D-Separation algorithm.
 * @author Héctor Del Campo Pando.
 */
class Tests {

    @Test
    fun graph1Test() {

        // Seminar 4, exercise 4
        val graph = DirectedAcyclicGraph<Char, DefaultEdge>(DefaultEdge::class.java)
        for (c in 'a'..'f') {
            graph.addVertex(c)
        }

        graph.addEdge('a', 'c')
        graph.addEdge('a', 'd')
        graph.addEdge('b', 'd')
        graph.addEdge('b', 'f')
        graph.addEdge('d', 'e')
        graph.addEdge('d', 'f')


        // No observations (4.a)
        Assert.assertFalse(areDSeparated(graph, 'a', 'd', arrayOf()))
        Assert.assertFalse(areDSeparated(graph, 'a', 'f', arrayOf()))
        Assert.assertTrue(areDSeparated(graph, 'a', 'b', arrayOf()))
        Assert.assertTrue(areDSeparated(graph, 'c', 'b', arrayOf()))
        Assert.assertFalse(areDSeparated(graph, 'e', 'f', arrayOf()))
        Assert.assertFalse(areDSeparated(graph, 'a', 'e', arrayOf()))
        Assert.assertFalse(areDSeparated(graph, 'c', 'f', arrayOf()))

        // Observations included (4.b)
        Assert.assertTrue(areDSeparated(graph, 'a', 'e', arrayOf('d')))
        Assert.assertTrue(areDSeparated(graph, 'e', 'f', arrayOf('d')))
        Assert.assertFalse(areDSeparated(graph, 'a', 'b', arrayOf('d')))
        Assert.assertFalse(areDSeparated(graph, 'c', 'b', arrayOf('d')))
        Assert.assertTrue(areDSeparated(graph, 'c', 'f', arrayOf('a', 'd')))
        Assert.assertFalse(areDSeparated(graph, 'e', 'f', arrayOf('a', 'b')))
        Assert.assertFalse(areDSeparated(graph, 'a', 'f', arrayOf('d')))

    }

    @Test
    fun graph2Test() {

        // Ej Student Network
        val graph = DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java)

        graph.addVertex("Difficulty")
        graph.addVertex("Grade")
        graph.addVertex("Letter")
        graph.addVertex("Intelligence")
        graph.addVertex("SAT")


        graph.addEdge("Difficulty", "Grade")
        graph.addEdge("Intelligence", "Grade")
        graph.addEdge("Grade", "Letter")
        graph.addEdge("Intelligence", "SAT")

        Assert.assertFalse(areDSeparated(graph, "Difficulty", "Grade", arrayOf()))
        Assert.assertFalse(areDSeparated(graph, "Difficulty", "Grade", arrayOf("SAT")))

        Assert.assertFalse(areDSeparated(graph, "Difficulty", "Letter", arrayOf()))
        Assert.assertFalse(areDSeparated(graph, "Difficulty", "Letter", arrayOf("Intelligence")))
        Assert.assertTrue(areDSeparated(graph, "Difficulty", "Letter", arrayOf("Intelligence", "Grade")))

        Assert.assertTrue(areDSeparated(graph, "Difficulty", "SAT", arrayOf()))
        Assert.assertFalse(areDSeparated(graph, "Difficulty", "SAT", arrayOf("Grade")))
        Assert.assertTrue(areDSeparated(graph, "Difficulty", "SAT", arrayOf("Grade", "Intelligence")))

        Assert.assertFalse(areDSeparated(graph, "SAT", "Grade", arrayOf()))
        Assert.assertFalse(areDSeparated(graph, "SAT", "Grade", arrayOf("Difficulty")))
        Assert.assertTrue(areDSeparated(graph, "SAT", "Grade", arrayOf("Intelligence")))

    }

}