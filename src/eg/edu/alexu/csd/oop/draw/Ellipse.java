package eg.edu.alexu.csd.oop.draw;

import java.awt.*;

public class Ellipse extends Rectangle{
    public Ellipse() {
    }

    Ellipse(Point start, Point end, Color color, Color fillColor) {
        super(start, end, color, fillColor);
        Point center = new Point((end.x + start.x)/2 , (end.y + start.y)/2 );
        getProperties().put("centerX",(double) center.x);
        getProperties().put("centerY",(double) center.y);
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

        Ellipse newOne = new Ellipse(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getColor() , getFillColor());
        newOne.setProperties(getProperties());
        return newOne;
    }
}