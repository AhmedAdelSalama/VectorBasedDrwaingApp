package eg.edu.alexu.csd.oop.draw;

import java.awt.*;

public class Circle extends Ellipse {

    public Circle() {

    }

    public Circle(Point start, Point end, Color color, Color fillColor) {
        super(start, end, color, fillColor);
    }


    public void draw(Graphics canvas) {
        canvas.setColor(getFillColor());
        canvas.fillOval(getProperties().get("startX").intValue() , getProperties().get("startY").intValue()
                ,getProperties().get("endX").intValue() - getProperties().get("startX").intValue()
                ,getProperties().get("endY").intValue() - getProperties().get("startY").intValue());


        canvas.setColor(getColor());
        canvas.drawOval(getProperties().get("startX").intValue() , getProperties().get("startY").intValue()
                ,getProperties().get("endX").intValue() - getProperties().get("startX").intValue()
                ,getProperties().get("endY").intValue() - getProperties().get("startY").intValue());
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Circle newOne = new Circle(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getColor() , getFillColor());
        newOne.setProperties(getProperties());
        return newOne;
    }
}
