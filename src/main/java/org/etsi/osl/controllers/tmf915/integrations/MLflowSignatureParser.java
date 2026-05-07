package org.etsi.osl.controllers.tmf915.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class MLflowSignatureParser {

    /**
     * Parses an MLflow MLmodel YAML string and generates a sample 
     * inference JSON request body based on its signature.
     * 
     * @param mlmodelYaml The MLmodel YAML string content
     * @return A Jackson JsonNode representing the dataframe_split request body
     * @throws IOException If parsing fails
     */
    public static JsonNode generateInferencePayload(String mlmodelYaml) throws IOException {
        // MLmodel is typically YAML, so we use Jackson with a YAMLFactory
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        ObjectMapper jsonMapper = new ObjectMapper();

        JsonNode mlmodel = yamlMapper.readTree(mlmodelYaml);
        
        JsonNode signature = mlmodel.path("signature");
        if (signature.isMissingNode() || signature.path("inputs").isMissingNode()) {
            throw new IllegalArgumentException("No signature inputs found in MLmodel configuration");
        }

        // In MLflow's MLmodel file, signature inputs are stored as a JSON-formatted string
        String inputsStr = signature.path("inputs").asText();
        JsonNode inputsArray = jsonMapper.readTree(inputsStr);

        ArrayNode columnsNode = jsonMapper.createArrayNode();
        ArrayNode dataRowNode = jsonMapper.createArrayNode();

        // Map the required inputs to dummy payload values
        for (JsonNode input : inputsArray) {
            String name = input.path("name").asText("unknown_col");
            String type = input.path("type").asText("string").toLowerCase();

            columnsNode.add(name);

            switch (type) {
                case "double":
                case "float":
                    dataRowNode.add(0.0);
                    break;
                case "integer":
                case "long":
                case "int":
                    dataRowNode.add(0);
                    break;
                case "boolean":
                    dataRowNode.add(false);
                    break;
                default:
                    dataRowNode.add("sample_string");
            }
        }

        ArrayNode dataNode = jsonMapper.createArrayNode();
        dataNode.add(dataRowNode);

        ObjectNode dataframeSplit = jsonMapper.createObjectNode();
        dataframeSplit.set("columns", columnsNode);
        dataframeSplit.set("data", dataNode);

        ObjectNode payload = jsonMapper.createObjectNode();
        payload.set("dataframe_split", dataframeSplit);

        // Return the JsonNode object directly instead of a String
        return payload;
    }
}
