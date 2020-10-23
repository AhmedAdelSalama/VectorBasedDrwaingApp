package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends Polygon {
    public Rectangle() {
        super();
    }

    Rectangle(Point start , Point end , Color color , Color fillColor) {
     //   this.setPosition(start);
        this.setColor(color);
        this.setFillColor(fillColor);
        Double width = (double) end.x - start.x;
        Double height = (double) end.y - start.y;
        Map<String, Double> properties = new HashMap<>();
        properties.put("w",width);
        properties.put("h",height);
        properties.put("startX", (double) start.x);
        properties.put("startY",(double) start.y);
        properties.put("endX",(double) end.x);
        properties.put("endY",(double) end.y);
        setPosition(start);
        this.setProperties(properties);
    }

    public void draw(Graphics canvas) {
        canvas.setColor(this.getFillColor());
        canvas.fillRect(getProperties().get("startX").intValue() , getProperties().get("startY").intValue()
                ,getProperties().get("endX").intValue() - getProperties().get("startX").intValue()
                ,getProperties().get("endY").intValue() - getProperties().get("startY").intValue() );


        canvas.setColor(this.getColor());
        canvas.drawRect(getProperties().get("startX").intValue() , getProperties().get("startY").intValue()
                ,getProperties().get("endX").intValue() - getProperties().get("startX").intValue()
                ,getProperties().get("endY").intValue() - getProperties().get("startY").intValue() );
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Rectangle newOne = new Rectangle(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getColor() , getFillColor());
        newOne.setProperties(getProperties());
        return newOne;
    }
}
