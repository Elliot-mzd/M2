package org.example.CallGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallGraph {
    private Set<String> methods = new HashSet<>();
    private Map<String, Set<String>> calls = new HashMap<>();
    private Map<String, String> methodTypes = new HashMap<>();

    public void addMethod(String methodName, String type) {
        methods.add(methodName);
        methodTypes.put(methodName, type);
    }

    public void addCall(String caller, String callee, String callerType, String calleeType) {
        calls.computeIfAbsent(caller, k -> new HashSet<>()).add(callee);
        methodTypes.putIfAbsent(caller, callerType);
        methodTypes.putIfAbsent(callee, calleeType);
    }

    public Set<String> getMethods() {
        return methods;
    }

    public Map<String, Set<String>> getCalls() {
        return calls;
    }

    public String getMethodType(String methodName) {
        return methodTypes.get(methodName);
    }

    public void writeDotFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("digraph CallGraph {\n");
            for (Map.Entry<String, Set<String>> entry : calls.entrySet()) {
                String caller = entry.getKey();
                String callerType = methodTypes.get(caller);
                for (String callee : entry.getValue()) {
                    String calleeType = methodTypes.get(callee);
                    writer.write(String.format("\"%s (type: %s)\" -> \"%s (type: %s)\";\n", caller, callerType, callee, calleeType));
                }
            }
            writer.write("}\n");
        }
    }
}