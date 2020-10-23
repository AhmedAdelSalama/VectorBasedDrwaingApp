package eg.edu.alexu.csd.oop.draw;


import java.awt.*;
import java.util.Map;

public class Polygon implements Shape{
    private Point position;
    private Map<String, Double> properties ;
    private Color color,fillColor;
    public Polygon() {
        setPosition(new Point(0,0));
        setProperties(null);
        setColor(Color.white);
        setFillColor(Color.white);
    }

    @Override
    public void setPosition(Point position) {
        this.position= position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return properties;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void draw(Graphics canvas) {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
