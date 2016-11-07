package refactored;

public class Switch {

    static abstract class Command {
        public abstract int execute(Data a);
    }

    public static class Add extends Command {
        @Override
        public int execute(Data data) {
            return data.a + data.b;
        }
    }

    public static class Sub extends Command {
        @Override
        public int execute(Data data) {
            return data.a - data.b;
        }
    }

    public static class Mul extends Command {
        @Override
        public int execute(Data data) {
            return data.a * data.b;
        }
    }

    public class Data {

        private int a;
        private int b;

        public int process(Command command) {
            return command.execute(this);
        }

    }

}