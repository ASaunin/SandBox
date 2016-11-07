package defective;

public class Switch {

    private int a;
    private int b;

    private enum Command {ADD, SUB, MUL};

    public class Data {

        public int process(Command command) {
            switch (command) {
                case ADD:
                    return a + b;
                case SUB:
                    return a - b;
                case MUL:
                    return a * b;
                default:
                    throw new IllegalArgumentException("unknown comamnd " + command);
            }
        }

    }

}
