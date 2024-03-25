package uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private Map<String, Object> environment;

    public App() {
        environment = new HashMap<>();
        // Agregar funciones predefinidas
        environment.put("+", new AddFunction());
        environment.put("-", new SubtractFunction());
        environment.put("*", new MultiplyFunction());
        environment.put("/", new DivideFunction());
    }

    public void setEnvironment(Map<String, Object> environment) {
        this.environment = environment;
    }

    public Object eval(Object expression) {
        if (expression instanceof Integer) {
            return expression; // Integer, se devuelve tal cual
        } else if (expression instanceof String) {
            String symbol = (String) expression;
            if (environment.containsKey(symbol)) {
                return environment.get(symbol); // Función o variable predefinida
            } else {
                return "ERROR: Símbolo no encontrado";
            }
        } else if (expression instanceof List<?>) {
            List<?> list = (List<?>) expression;
            Object operator = list.get(0);
            if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")) {
                // Para operaciones aritméticas
                if (list.size() == 3) {
                    Object operand1 = eval(list.get(1));
                    Object operand2 = eval(list.get(2));
                    if (operand1 instanceof Integer && operand2 instanceof Integer) {
                        int result;
                        switch ((String) operator) {
                            case "+":
                                result = (int) operand1 + (int) operand2;
                                break;
                            case "-":
                                result = (int) operand1 - (int) operand2;
                                break;
                            case "*":
                                result = (int) operand1 * (int) operand2;
                                break;
                            case "/":
                                if ((int) operand2 != 0) {
                                    result = (int) operand1 / (int) operand2;
                                } else {
                                    return "ERROR: División por cero";
                                }
                                break;
                            default:
                                return "ERROR: Operador aritmético no soportado";
                        }
                        return result;
                    } else {
                        return "ERROR: Operandos inválidos para la operación aritmética";
                    }
                } else {
                    return "ERROR: Número inválido de operandos para la operación aritmética";
                }
            } else if (operator.equals("QUOTE")) {
                if (list.size() >= 2) {
                    return list.get(1);
                } else {
                    return "ERROR: Argumento faltante para QUOTE";
                }
            } else if (operator.equals("DEFUN")) {
                // Definir una función
                if (list.size() >= 4 && list.get(1) instanceof String && list.get(2) instanceof List<?>) {
                    String functionName = (String) list.get(1);
                    Object params = list.get(2);
                    Object body = list.get(3);
                    environment.put(functionName, new Function(params, body, environment));
                    return functionName;
                } else {
                    return "ERROR: Sintaxis incorrecta para DEFUN";
                }
            } else if (operator.equals("COND")) {
                // Evaluación condicional
                for (int i = 1; i < list.size(); i++) {
                    List<?> clause = (List<?>) list.get(i);
                    if (clause.size() >= 2) {
                        Object condition = eval(clause.get(0));
                        if (condition instanceof Boolean && ((Boolean) condition)) {
                            return eval(clause.get(1));
                        }
                    }
                }
                return "ERROR: No hay condiciones válidas en COND";
            } else {
                // Llamada a función
                if (environment.containsKey(operator)) {
                    Function function = (Function) environment.get(operator);
                    List<?> args = (List<?>) list.subList(1, list.size());
                    return function.apply(args);
                } else {
                    return "ERROR: Función no definida";
                }
            }
        }
        return "ERROR: Tipo de expresión no soportado";
    }

    public Map<String, Object> getEnvironment() {
        return environment;
    }

    //Método principal que solicita al usuario ingresar un programa Lisp y lo evalúa.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        App interpreter = new App(); // Crea la instancia del intérprete Lisp

        // Solicita al usuario que ingrese un programa Lisp
        System.out.print("Ingrese un programa Lisp: ");
        String userInput = scanner.nextLine();

        // Evalua la entrada del usuario
        Object result = interpreter.eval(parseInput(userInput));

        // Imprime el resultado
        System.out.println("Resultado: " + result);

        scanner.close();
    }

    // Método auxiliar para analizar la entrada del usuario en una expresión Lisp.
    private static Object parseInput(String userInput) {
        // Divide la cadena de entrada en tokens
        String[] tokens = userInput.split("\\s+");

        // Crea una lista para almacenar la expresión Lisp
        List<Object> expression = new ArrayList<>();

        // Itera a través de los tokens y agregarlos a la lista de expresiones
        for (String token : tokens) {
            if (token.startsWith("(")) {
                // lista anidada
                List<Object> nestedList = new ArrayList<>();
                expression.add(nestedList);
            } else if (token.endsWith(")")) {
                List<Object> lastList = (List<Object>) expression.get(expression.size() - 1);
                lastList.add(token.substring(0, token.length() - 1));
            } else {
                // Token regular (símbolo, número, etc.)
                List<Object> lastList = (List<Object>) expression.get(expression.size() - 1);
                lastList.add(token);
            }
        }
        return expression;
    }
}
