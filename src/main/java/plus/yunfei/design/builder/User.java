package plus.yunfei.design.builder;

public class User {
    private String name;
    private int age;

    private User() {

    }

    public User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }


    public static Builder builder() {
        return new Builder();
    }

    private static class Builder {
        private String name;
        private int age;

        public User build() {
            User user = new User(this);
            if (age < 10 && "Tom".equals(name)) {
                throw new IllegalArgumentException();
            }
            if (age > 10 && "Jerry".equals(name)) {
                throw new IllegalArgumentException();
            }
            return user;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }
    }

    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public static void main(String[] args) {
        User user = User.builder().name("Tom").age(10).build();
    }
}
