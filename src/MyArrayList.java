import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyArrayList<T> implements MyList<T>, Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int size = 0; // number of the elements in list
    private transient Object[] array; // array that backs MyArrayList (it specifies the capacity)
    private static final int initialCapacity = 1;

    // Constructs an empty list with an initial capacity of ten.
    public MyArrayList() {
        this.array = new Object[initialCapacity];
    }

    // Constructs an empty list with the specified initial capacity.
    public MyArrayList(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Index less than or equal to zero! Can't create MyArrayList!");
        }
        this.array = new Object[initialCapacity];
    }

    // Constructs a list containing all the elements of the specified collection
    public MyArrayList(Collection<? extends T> list) {
        try {
            this.array = new Object[list.size()];
            this.addAll(list);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

    // Appends the specified element to the end of this list.
    @Override
    public boolean add(T element) {
        if (size >= array.length) {
            resize();
        }
        array[size] = element;
        size++;
        return true;
    }

    // Inserts the specified element at the specified position in this list.
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds!"); // we can add elements only in range of elements (no range of capacity)
        } else if (index == size) {
            add(element);
            return;
        }
        Object[] tempArray = new Object[size - index];
        System.arraycopy(array, index, tempArray, 0, size - index);
        if (size >= array.length) {
            resize();
        }
        array[index] = element;
        for (int i = index + 1, j = 0; j < tempArray.length; i++, j++) {
            if (i >= array.length) {
                resize();
            }
            array[i] = tempArray[j];
        }
        size++;
    }

    // Appends all the elements in the specified collection to the end of this list.
    @Override
    public boolean addAll(Collection<? extends T> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (T element : list) {
            add(element);
        }
        return true;
    }

    // Inserts all the elements in the specified collection into this list,
    // starting at the specified position.
    public boolean addAll(int index, Collection<? extends T> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        int initialSize = size;
        for (T element : list) {
            add(index++, element);
        }
        return size > initialSize;
    }

    // Removes the element at the specified position in this list.
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        Object[] tempArray = new Object[size - (index + 1)];
        Object element = array[index];
        int j = 0;
        for (int i = index + 1; i < size; i++) {
            tempArray[j] = array[i];
            j++;
        }
        j = 0;
        this.size--;
        array[this.size] = null;
        for (int i = index; i < this.size; i++) {
            array[i] = tempArray[j];
            j++;
        }
        return (T) element;
    }

    // Removes the first occurrence of the specified element from this list, if it is present.
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(array[i])) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    // Removes from this list all of its elements that are contained in the specified collection.
    @Override
    public boolean removeAll(Collection<?> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        int initialCapacity = size;
        for (int i = this.size - 1; i >= 0; i--) {
            if (list.contains(array[i])) {
                remove(i);
            }
        }
        return initialCapacity > size;
    }

    // resizes the capacity of our array, it grows twice
    private void resize() {
        Object[] tempArray = new Object[array.length * 2];
        System.arraycopy(array, 0, tempArray, 0, size);
        this.array = tempArray;
    }

    // Removes all the elements from this list. The list will be empty after this method.
    @Override
    public void clear() {
//        removeAll(Arrays.asList(this.array));
        array = new Object[initialCapacity];
        size = 0;
    }

    // Returns a shallow copy of this MyArrayList instance.
    @Override
    public Object clone() {
        try {
            MyArrayList<T> clone = (MyArrayList<T>) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
    }

    // Returns true if this list contains the specified element.
    @Override
    public boolean contains(Object element) {
        boolean contains = false;
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) {
                return !contains;
            }
        }
        return contains;
    }

    // Returns true if this ArrayList contains all the elements in the specified collection.
    @Override
    public boolean containsAll(Collection<?> list) {
        int counterOfMatches = 0;
        for (Object element : list) {
            if (this.contains(element)) {
                counterOfMatches++;
                if (list.size() == counterOfMatches) {
                    return true;
                }
            }
        }
        return false;
    }

    // Returns the element at the specified position in this list.
    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        return (T) array[index];
    }

    // Retains only the elements in this ArrayList that are contained in the specified collection
    @Override
    public boolean retainAll(Collection<?> list) {
        if (list == null) {
            throw new NullPointerException("The list is null!");
        } else if (list.isEmpty()) {
            this.clear();
            return true;
        }
        Set<Object> set = new HashSet<>(list);

        for (int i = size - 1; i >= 0; i--) {
            if (!set.contains(array[i])) {
                remove(i);
            }
        }
        return true;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean removed = false;
        for (int i = 0; i < size; i++) {
            if (filter.test((T) array[i])) {
                remove(i);
                i--;
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MyList)) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            MyArrayList<T> tempArrayList = (MyArrayList<T>) obj;
            if (tempArrayList.size() != this.size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!Objects.equals(this.get(i), tempArrayList.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Arrays.hashCode(array);
        result = 31 * result + size;
        return result;
    }

    // Returns the index of the first occurrence of the specified element in this list,
    // or -1 if this list does not contain the element. (lowest index)
    @Override
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    // Returns the index of the last occurrence of the specified element in this list,
    // or -1 if this list does not contain the element. (highest index)
    @Override
    public int lastIndexOf(T element) {
        for (int i = size - 1; i >= 0; i--) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    // Returns true if this list contains no elements.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of elements in this list.
    @Override
    public int size() {
        return size;
    }

    // Replaces the element at the specified position in this list with the specified element.
    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out ouf bounds!");
        }
        array[index] = element;
        return element;
    }

    // Returns an array containing all
    // the elements in this list in proper sequence (from first to last element).
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    // Returns an array of specified type containing all
    // the elements in this list in proper sequence (from first to last element).
    @Override
    public <T1> T1[] toArray(T1[] a) {
        int loopUpperbound = Math.min(size, a.length);
        for (int i = 0; i < loopUpperbound; i++) {
            a[i] = (T1) array[i];
        }
        return a;
    }

    // Trims the capacity of this MyArrayList instance to be the list's current size.
    public void trimToSize() {
        Object[] tab = new Object[size];
        System.arraycopy(array, 0, tab, 0, size);
        array = tab;
    }

    // Increases the capacity of this ArrayList instance,
    // if necessary, to ensure that it can hold at least the number of elements
    // specified by the minimum capacity argument.
    public void ensureCapacity(int minCapacity) {
        if (minCapacity <= 0 || minCapacity < size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        Object[] tab = new Object[minCapacity];
        if (size >= 0) {
            System.arraycopy(array, 0, tab, 0, size);
        }
        array = tab;
    }

    @Override
    public MyList<T> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("Starting index higher than index of end!");
        } else if (fromIndex < 0 || toIndex > this.size) {
            throw new IndexOutOfBoundsException("Index out ouf bounds!");
        }
        return new Sublist<>(this, fromIndex, toIndex);
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (int i = 0; i < size; i++) {
            stringJoiner.add(array[i].toString());
        }
        return "[" + stringJoiner + "]";
    }

    // The iterator() method in MyArrayList returns a new instance of MyArrayListIterator,
    // which allows to iterate over the elements in MyArrayList. (for-each loop)
    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator();
    }

    private class MyArrayListIterator implements Iterator<T> {
        private int cursor = 0;
        private int lastPosition = -1;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Cannot remove element before calling next()");
            }
            MyArrayList.this.remove(lastPosition);
            cursor = lastPosition;
            lastPosition = -1;
            canRemove = false;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the list");
            }
            Object element = array[cursor];
            lastPosition = cursor;
            cursor++;
            canRemove = true;
            return (T) element;
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "The action object is null!");
        for (int i = 0; i < size; i++) {
            action.accept((T) array[i]);
        }
    }

    private static class Sublist<T> extends MyArrayList<T> {
        private final MyArrayList<T> root;
        private int size;
        private final int offset;

        public Sublist(MyArrayList<T> root, int fromIndex, int toIndex) {
            this.root = root;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        @Override
        public boolean add(T element) {
            root.add(size + offset, element);
            size++;
            return true;
        }

        @Override
        public void add(int index, T element) {
            if (checkSublistIndex(index)) {
                root.add(index + offset, element);
                size++;
            }
        }

        @Override
        public T remove(int index) {
            if (checkSublistIndex(index)) {
                T removedElement = root.remove(offset + index);
                size--;
                return removedElement;
            }
            return null;
        }

        private boolean checkSublistIndex(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index out of sublist bounds! Indexes range: [" +
                        0 + "-" + size + "]");
            }
            return true;
        }

        @Override
        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (int i = offset; i < size + offset; i++) {
                stringJoiner.add(root.array[i].toString());
            }
            return "[" + stringJoiner + "]";
        }
    }
}
