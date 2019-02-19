package s2imf35.operator;

import s2imf35.Main;
import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NuComponent extends AbstractComponent {
    // The components of the and operator.
    public final String variable;
    private final AbstractComponent rhs;

    // Is the formula open?
    private boolean isOpen;

    // Which sub-formulae are open and have the same sign?
    private List<NuComponent> openSubFormulae;

    // Regex for labels.
    private static final Pattern p = Pattern.compile("[A-Z]");

    private NuComponent(String variable, AbstractComponent rhs) {
        this.variable = variable;
        this.rhs = rhs;
    }

    public static AbstractComponent extract(String input) {
        // Find the recursion variable.
        String variable = input.substring(3, 4);

        // Find the formula.
        String formula = input.substring(5, input.length());

        // Resolve the sub-components and make a new node.
        return new NuComponent(variable, parse(formula));
    }

    public static Boolean isMatch(String input) {
        if(input.startsWith("nu")) {
            // Find the recursion variable.
            String variable = input.substring(3, 4);

            // Is the label valid?
            return p.matcher(variable).find();
        }
        return false;
    }

    @Override
    public String toLatex() {
        return "\\nu " + variable + ".(" + rhs.toLatex() + ")";
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> evaluate(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter) {
        // Is the surrounding binder a different sign?
        if(!binderStack.isEmpty() && binderStack.peek() instanceof NuComponent) {
            // Reset the recursion variable of all open sub-formulae bound by a nu statement.
            for(NuComponent c : openSubFormulae) {
                A.put(c.variable, graph.S());
                counter.resets++;
            }
        }

        // Add the binder to the binder stack.
        binderStack.push(this);

        // Print the formula and current evaluation.
        Main.print("\t" + this.toLatex(), 2);
        Main.print("\t\t" + Arrays.toString(A.get(variable).toArray()), 2);

        // Continue evaluating until A remains unchanged.
        Set<Integer> X;
        do {
            X = A.get(variable);
            A.put(variable, rhs.evaluate(graph, A, binderStack, counter));
            counter.iterations++;

            // Print the current evaluation.
            Main.print("\t\t" + Arrays.toString(A.get(variable).toArray()), 2);

        } while (!X.equals(A.get(variable)));

        // Remove the binder from the stack.
        binderStack.pop();

        // Print a finishing line.
        Main.print("\t--------------------", 2);

        return A.get(variable);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Set<Integer> naiveEvaluate(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter) {
        // Start by filling A.
        A.put(variable, graph.S());
        counter.resets++;

        // Print the formula and current evaluation.
        Main.print("\t" + this.toLatex(), 2);
        Main.print("\t\t" + Arrays.toString(A.get(variable).toArray()), 2);

        // Continue evaluating until A remains unchanged.
        Set<Integer> X;
        do {
            X = A.get(variable);
            A.put(variable, rhs.naiveEvaluate(graph, A, counter));
            counter.iterations++;

            // Print the current evaluation.
            Main.print("\t\t" + Arrays.toString(A.get(variable).toArray()), 2);

        } while (!X.equals(A.get(variable)));

        // Print a finishing line.
        Main.print("\t--------------------", 2);

        return A.get(variable);
    }

    @Override
    public List<AbstractComponent> propagateOpenSubFormulae() {
        // First, get the open sub-formulae.
        List<AbstractComponent> openFormulae = rhs.propagateOpenSubFormulae();

        // Create the collection of open sub-formulae with the same sign.
        Stream<AbstractComponent> openSubFormulae = openFormulae.stream().filter(e -> e instanceof NuComponent);
        this.openSubFormulae = openSubFormulae.map(NuComponent.class::cast).collect(Collectors.toList());

        // Add the binder if it is open.
        if(isOpen) {
            openFormulae.add(this);
        }

        // Pass on the list of open formulae.
        return openFormulae;
    }

    @Override
    public Set<String> propagateOpenVariables() {
        // First, get the open variables of the sub-formula.
        Set<String> openVariables = rhs.propagateOpenVariables();

        // Remove the variable enclosed by this binder.
        openVariables.remove(variable);

        // Set whether the formula is open or not.
        isOpen = !openVariables.isEmpty();

        // Propagate the variables list.
        return openVariables;
    }

    @Override
    public List<AbstractComponent> findVariableBindings(Set<String> boundVariables) {
        // The variable of this binding is bound.
        boundVariables.add(variable);

        // First, get the variable bindings of the sub-formula and add itself.
        List<AbstractComponent> bindings = rhs.findVariableBindings(boundVariables);
        bindings.add(this);

        return bindings;
    }

}
