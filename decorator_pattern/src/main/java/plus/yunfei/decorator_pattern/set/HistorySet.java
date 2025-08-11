package plus.yunfei.decorator_pattern.set;

import java.util.*;

public class HistorySet<E> implements Set<E> {

    private Set<E> delegate; // The delegate set that this HistorySet wraps around


    public HistorySet(Set<E> delegate) {
        this.delegate = delegate;
    }

    private List<E> history = new ArrayList<>(); // A list to keep track of the history of elements added

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return (T[]) delegate.toArray();
    }

    public boolean add(E e) {
        return delegate.add(e);
    }

    public boolean remove(Object o) {
        if (delegate.remove(o)) {
            history.add((E) o);
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return delegate.contains(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return delegate.addAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    public void clear() {
        delegate.clear();
    }

    @Override
    public String toString() {
        return delegate.toString() + "HistorySet=" + history;
    }
}

class Test {
    public static void main(String[] args) {
        Set<String> historySet = new HistorySet<>(new HashSet<>());
        Set<String> set = new HistorySet<>(historySet);
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.remove("4");
        set.remove("4");
        set.remove("5");
        set.remove("1");
        System.out.println(set);
        Collection<Object> objects = Collections.synchronizedCollection(new ArrayList<>());
        if (objects.isEmpty()) {
            objects.add(1);
        }
    }
}
