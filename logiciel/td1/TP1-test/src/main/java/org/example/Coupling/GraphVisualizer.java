package org.example.Coupling;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;


public class GraphVisualizer {
    public static void visualizeGraph(Graph<String, DefaultWeightedEdge> graph) {
        Map<String, mxCell> vertices = new HashMap<>();
        mxGraph jgraph = new mxGraph();
        Object parent = jgraph.getDefaultParent();

        jgraph.getModel().beginUpdate();
        try {
            for (String vertex : graph.vertexSet()) {
                System.out.println("Adding vertex: " + vertex); // Debug statement
                mxCell cell = (mxCell) jgraph.insertVertex(parent, null, vertex, 0, 0, 80, 30);
                vertices.put(vertex, cell);
            }

            for (DefaultWeightedEdge edge : graph.edgeSet()) {
                System.out.println("Adding edge from " + graph.getEdgeSource(edge) + " to " + graph.getEdgeTarget(edge) + " with weight " + graph.getEdgeWeight(edge)); // Debug statement
                mxCell sourceVertex = vertices.get(graph.getEdgeSource(edge));
                mxCell targetVertex = vertices.get(graph.getEdgeTarget(edge));
                jgraph.insertEdge(parent, null, graph.getEdgeWeight(edge), sourceVertex, targetVertex);
            }
        } finally {
            jgraph.getModel().endUpdate();
        }

        mxCircleLayout layout = new mxCircleLayout(jgraph);
        layout.execute(parent);

        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().add(new mxGraphComponent(jgraph));
        frame.setVisible(true);
    }
}