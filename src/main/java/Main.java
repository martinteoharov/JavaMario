import jade.Window;

/*
 * ============================================================================================
 * Read:
 *     * ECS: Java has garbage collection which doesn't allow for cool stuff like ECS, we instead
 *     use EC. The following links are for ECS.
 *          - https://gameprogrammingpatterns.com/component.html
 *          - https://medium.com/ingeniouslysimple/entities-components-and-systems-89c31464240d
 *
 * ============================================================================================
 *
*/

public class Main {
    public static void main(String[] args){
        System.out.println("Hello World!");
        Window window = Window.get();
        window.run();
    }
}
