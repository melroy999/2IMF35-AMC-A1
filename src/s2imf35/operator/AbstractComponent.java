package s2imf35.operator;

import s2imf35.PerformanceCounter;
import s2imf35.graph.LTS;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * An abstract class which is the basis of all other component types.
 */
public abstract class AbstractComponent {

    /**
     * Get a string representation of the formula in latex markup.
     *
     * @return A string representation of the tree, using latex symbols.
     */
    public abstract String toLatex();

    /**
     * Check whether the given input formula matches the structure of the operator type.
     *
     * @param input The (sub)formula string.
     * @return Whether the formula can be converted into a node of the desired operator type.
     */
    public abstract boolean isMatch(String input);

    /**
     * Convert the given input formula to a node of the desired operator type and resolve the sub-formulas recursively.
     *
     * @param input The (sub)formula string.
     * @return The formula represented by a parse tree, with as root node the desired operator type.
     */
    public abstract AbstractComponent extract(String input);

    /**
     * Find the set of states S in which the formula holds, using the Emerson-Lei Algorithm.
     *
     * @param graph The graph the formula should be applied to.
     * @param A The current approximations of the recursion variables, represented by a mapping.
     * @param binderStack The recursion variables that are bounded by the parent nodes.
     * @param counter A performance counter that measures the performance of the algorithm.
     * @return The set of states S in which the formula holds.
     */
    public abstract Set<Integer> emersonLei(LTS graph, Map<String, Set<Integer>> A, Stack<AbstractComponent> binderStack, PerformanceCounter counter);

    /**
     * Find the set of states S in which the formula holds, using the naive algorithm.
     *
     * @param graph The graph the formula should be applied to.
     * @param A The current approximations of the recursion variables, represented by a mapping.
     * @param counter A performance counter that measures the performance of the algorithm.
     * @return The set of states S in which the formula holds.
     */
    public abstract Set<Integer> naive(LTS graph, Map<String, Set<Integer>> A, PerformanceCounter counter);

    /**
     * A list of all component types ordered by parsing priority.
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    private static final AbstractComponent[] componentTypes = {
            new AndComponent(),
            new OrComponent(),
            new RecursionVariableComponent(),
            new TrueComponent(),
            new FalseComponent(),
            new DiamondModalityComponent(),
            new BoxModalityComponent(),
            new NuComponent(),
            new MuComponent()
    };

    /**
     * Find the root operator/symbol and parse the given input formula recursively.
     *
     * @param input The (sub)formula string.
     * @return The formula represented by a parse tree.
     */
    public static AbstractComponent parse(String input) {
        // Remove padding spaces.
        input = input.trim();

        // Find the pattern that matches.
        for(AbstractComponent component : componentTypes) {
            if(component.isMatch(input)) {
                return component.extract(input);
            }
        }

        // We might have brackets to worry about. Remove the outer brackets and call parse again.
        if(input.startsWith("(") && input.endsWith(")")) {
            input = input.substring(1, input.length() - 1);
            return parse(input);
        }

        // We failed to parse the input, throw an exception.
        throw new RuntimeException("Failed to parse input \"" + input + "\"");
    }

    /**
     * Walk through the parse tree and ensure that all mu and nu-components have an up-to-date list of their open
     * sub-formulae with the same sign.
     *
     * @return A list of all open sub-formulae with root type mu or nu.
     */
    public abstract List<AbstractComponent> propagateOpenSubFormulae();

    /**
     * Walk through the parse tree and track the open variables that are open.
     *
     * @return The set of variables that are open within the formula represented by the current node and its children.
     */
    public abstract Set<String> propagateOpenVariables();

    /**
     * Find the locations where each recursion variable is defined. This location is a mu-operator or nu-operator when
     * the variable is bound. Otherwise, the binder is a recursion variable symbol.
     *
     * @param boundVariables The recursion variable names that are bounded by one of the parent operators.
     * @return A list of components in which the first occurrence of each recursion variable is captured.
     */
    public abstract List<AbstractComponent> findVariableBindings(Set<String> boundVariables);

    /**
     * The formula can be converted to a string by looking up the latex representation.
     *
     * @return A latex representation of the formula stored within the parse tree.
     */
    @Override
    public String toString() {
        return "$" + toLatex() + "$";
    }

    /**
     * Calculate the nesting depth of the formula.
     *
     * @return The nesting depth, as defined in the lecture slides.
     */
    public abstract int nestingDepth();

    /**
     * Calculate the alternation depth of the formula.
     *
     * @return The alternation depth, as defined in the lecture slides.
     */
    public abstract int alternationDepth();

    /**
     * Calculate the dependent alternation depth of the formula.
     *
     * @return The dependent alternation depth, as defined in the lecture slides.
     */
    public abstract int dependentAlternationDepth();

    /**
     * Get the set of all recursion variable names in the formula.
     *
     * @param variables An input set to which all the variables will be added.
     */
    public abstract void getRecursionVariables(Set<String> variables);

    /**
     * Get all the mu formulae components within the formula.
     *
     * @param components An input list to which all the mu formulae will be added.
     */
    public abstract void getMuFormulae(List<MuComponent> components);

    /**
     * Get all the mu formulae components within the formula.
     *
     * @param components An input list to which all the nu formulae will be added.
     */
    public abstract void getNuFormulae(List<NuComponent> components);
}
