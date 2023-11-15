package diamondEngine.diaUtils;

public class DiaFIFO {

    // ATTRIBUTES
    private boolean resizeable = true;
    private int size = 256;
    private Object[] list;
    private int first = 0;
    private int last = -1;
    private int stored = 0;

    // CONSTRUCTORS
    public DiaFIFO() {
        this.list = new Object[this.size];
    }

    public DiaFIFO(boolean resizeable) {
        this.resizeable = resizeable;
        this.list = new Object[size];
    }

    public DiaFIFO(int size) {
        this.size = size;
        this.list = new Object[size];
    }

    public DiaFIFO(boolean resizeable, int size) {
        this.resizeable = resizeable;
        this.size = size;
        this.list = new Object[size];
    }

    // GETTERS & SETTERS
    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public int getStored() {
        return stored;
    }

    public Object[] getList() {
        return list;
    }

    public int getSize() {
        return size;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    // METHODS
    public Object pop() {
        if (stored == 0) return null;
        Object popped = list[first];
        list[first] = null;
        first = (first + 1) % size;
        stored--;
        return popped;
    }

    public void push(Object obj) {
        if (stored == size && resizeable) this.resizeList(size * 2);
        if (stored < size) {
            last = (last + 1) % size;
            list[last] = obj;
            stored++;
        }
    }

    public boolean isEmpty() {
        return stored == 0;
    }

    public void resizeList(int newSize) {
        size = newSize;
        Object[] newList = new Object[newSize];
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[(i + first) % list.length];
        }
        list = newList;
        first = 0;
        last = stored - 1;
    }
}
