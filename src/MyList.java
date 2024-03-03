import java.util.Collection;
import java.util.Iterator;

public interface MyList<T> extends Collection<T>, Iterable<T> {

    @Override
    boolean add(T t);

    void add(int index, T element);

    @Override
    boolean addAll(Collection<? extends T> c);

    @Override
    boolean contains(Object o);

    @Override
    boolean containsAll(Collection<?> c);

    @Override
    boolean remove(Object o);

    T remove(int index);

    @Override
    boolean removeAll(Collection<?> c);

    @Override
    boolean retainAll(Collection<?> c);

    @Override
    void clear();

    @Override
    int size();

    @Override
    boolean isEmpty();

    @Override
    Iterator<T> iterator();

    @Override
    Object[] toArray();

    @Override
    <T1> T1[] toArray(T1[] a);

    T get(int index);

    int indexOf(T element);

    int lastIndexOf(T element);

    T set(int index, T element);

    MyList<T> subList(int fromIndex, int toIndex);
}
