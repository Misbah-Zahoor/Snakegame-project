import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;

public class Snake {

    private LinkedList<Point> body;
    private HashSet<Point> occupied;

    public Snake() {
        body = new LinkedList<>();
        occupied = new HashSet<>();

        body.addFirst(new Point(10, 10));
        body.addFirst(new Point(10, 9));
        body.addFirst(new Point(10, 8));

        occupied.addAll(body);
    }

    public void move(Point newHead) {
        body.addFirst(newHead);
        occupied.add(newHead);
        Point tail = body.removeLast();
        occupied.remove(tail);
    }

    public void grow(Point newHead) {
        body.addFirst(newHead);
        occupied.add(newHead);
    }

    public boolean collidesWith(Point p) {
        return occupied.contains(p);
    }

    public Point getHead() {
        return body.getFirst();
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public int getSize() {
        return body.size();
    }
}