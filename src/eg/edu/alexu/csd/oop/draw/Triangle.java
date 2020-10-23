package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends Polygon {
    public Triangle (){
        super();
    }
    Triangle(Point start, Point end, Color color, Color fillColor) {
        this.setColor(color);
        this.setFillColor(fillColor);
        Point thirdPoint = new Point(Math.min(start.x,end.x) , Math.max(start.y,end.y));
        Map<String, Double> properties = new HashMap<>();
        properties.put("startX", (double) start.x);
        properties.put("startY",(double) start.y);
        properties.put("endX",(double) end.x);
        properties.put("endY",(double) end.y);
        properties.put("thirdPointX",(double) thirdPoint.x);
        properties.put("thirdPointY",(double) thirdPoint.y);
        setProperties(properties);
    }

    public void draw(java.awt.Graphics canvas){
        int[] i1 = {getProperties().get("startX").intValue() , getProperties().get("endX").intValue()
                , getProperties().get("thirdPointX").intValue() } ;

        int[] i2 = {getProperties().get("startY").intValue() , getProperties().get("endY").intValue()
                , getProperties().get("thirdPointY").intValue() } ;
        canvas.setColor(this.getFillColor());
        canvas.fillPolygon(i1 , i2 , 3);

        canvas.setColor(this.getColor());
        canvas.drawPolygon(i1 , i2 , 3);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Triangle newOne = new Triangle(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getColor() , getFillColor());

        newOne.setProperties(getProperties());
        return newOne;
    }
}
