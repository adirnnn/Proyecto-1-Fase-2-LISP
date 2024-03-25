package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;

public class Function {
    private Object params;
    private Object body;
    public Function(Object params, Object body, Map<String, Object> environment) {
        this.params = params;
        this.body = body;
    }

    public Object apply(Object args) {
        Map<String, Object> localEnv = new HashMap<>();
        if (params instanceof List && args instanceof List) {
            List paramList = (List) params;
            List argList = (List) args;
            if (paramList.size() == argList.size()) {
                for (int i = 0; i < paramList.size(); i++) {
                    if (paramList.get(i) instanceof String) {
                        localEnv.put((String) paramList.get(i), argList.get(i));
                    } else {
                        throw new IllegalArgumentException("Invalid parameter type for function");
                    }
                }
            } else {
                throw new IllegalArgumentException("Mismatched number of arguments for function");
            }
        } else if (params instanceof String) {
            localEnv.put((String) params, args);
        } else {
            throw new IllegalArgumentException("Invalid argument type for function");
        }
        App interpreter = new App();
        interpreter.setEnvironment(localEnv);
        return interpreter.eval(body);
    }                
}