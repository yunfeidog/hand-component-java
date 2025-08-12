package plus.yunfei.design.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class User implements Iterable<String> {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Iterator<String> iterator() {
        return new UserIterator();
    }

    class UserIterator implements Iterator<String> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < 2;
        }

        @Override
        public String next() {
            index++;
            if (index == 1) return name;
            if (index == 2) return age + "";
            throw new NoSuchElementException();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class Test {
    public static void main(String[] args) {
        User user = new User("cxk", 18);
        for (String s : user) {
            System.out.println(s);
        }
        Iterator<String> iterator = user.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            System.out.println(s);
        }
    }
}

