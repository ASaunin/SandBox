package refactored;

import model.Person;

public class LongParameterList {

    public static void main(String[] args) {

        // Alternatively to solve long parameter problem one can create parameter objects (Name, Address & etc.)
        final Person person = new Person.Builder()
                .setName("John", "Doe")
                .setAge(33)
                .setAddress("2-d avenue", "NY", "USA")
                .createPerson();

        System.out.println(person);
    }

}
