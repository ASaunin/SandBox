package concurrency;

public class Singleton {

    private static Singleton instance = new Singleton();

    static {
        System.out.println("Static initializer");
    }

    {
        System.out.println("Initializer");
    }

    private Singleton() {
        System.out.println("Constructor");
    }

    public static Singleton getInstance() {
        return instance;
    };

}
