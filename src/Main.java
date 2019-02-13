import operator.AbstractComponent;

public class Main {
    public static void main(String[] args) {
        System.out.println(AbstractComponent.parse("(X && Y)"));
        System.out.println(AbstractComponent.parse("((X && (X && Y)) && Y)"));
    }
}
