package defective;

import model.Person;

public class LongParameterList {

    public static void main(String[] args) {

        final Person person = new Person("John", "Doe", 33, "2-d avenue", "NY", "USA");
        System.out.println(person);

    }

}
