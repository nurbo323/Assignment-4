package kz.edu.daa.utils;

import com.google.gson.*;
import kz.edu.daa.graph.model.Graph;
import java.io.FileReader;
import java.io.IOException;

public class GraphLoader {
    public static Graph loadFromJson(String filePath) throws IOException {
        JsonElement element = JsonParser.parseReader(new FileReader(filePath));
        JsonObject jsonObject = element.getAsJsonObject();

        int n = jsonObject.get("n").getAsInt();
        boolean directed = jsonObject.get("directed").getAsBoolean();

        Graph graph = new Graph(n, directed);

        JsonArray edges = jsonObject.getAsJsonArray("edges");
        for (JsonElement edgeElement : edges) {
            JsonObject edge = edgeElement.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            graph.addEdge(u, v, w);
        }

        return graph;
    }

    public static String getWeightModel(String filePath) throws IOException {
        JsonElement element = JsonParser.parseReader(new FileReader(filePath));
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject.get("weight_model").getAsString();
    }

    public static int getSource(String filePath) throws IOException {
        JsonElement element = JsonParser.parseReader(new FileReader(filePath));
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject.get("source").getAsInt();
    }
}
