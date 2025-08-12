package plus.yunfei.design.iterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author houyunfei
 */
public class UserFile implements Iterable<User> {

    private final File file;

    public UserFile(File file) {
        this.file = file;
    }

    @Override
    public Iterator<User> iterator() {
        return new UserFileIterator();
    }

    private class UserFileIterator implements Iterator<User> {
        List<User> userList = loadUsers();
        int cursor = 0;

        private List<User> loadUsers() {
            try {
               return Files.readAllLines(file.toPath()).stream().map(line -> {
                    String substring = line.substring(1, line.length() - 1);
                    String[] parts = substring.split(",");
                    return new User(parts[0], Integer.parseInt(parts[1]));
                }).collect(Collectors.toList());
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }

        @Override
        public boolean hasNext() {
            return cursor != userList.size();
        }

        @Override
        public User next() {
            if (cursor >= userList.size()) throw new NoSuchElementException();
            User user = userList.get(cursor);
            cursor++;
            return user;
        }
    }
}

class UserFileTest {
    public static void main(String[] args) {
        File file = new File("users.txt");
        UserFile users = new UserFile(file);
        for (User user : users) {
            System.out.println(user);
        }
    }
}
