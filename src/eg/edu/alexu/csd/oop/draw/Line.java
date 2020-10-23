package eg.edu.alexu.csd.oop.draw;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Line extends Polygon {
    public Line() {
        super();
    }

    Line(Point start , Point end , Color color) {
        //   this.setPosition(start);
        this.setFillColor(color);
        Map<String, Double> properties = new HashMap<>();
        properties.put("startX", (double) start.x);
        properties.put("startY",(double) start.y);
        properties.put("endX",(double) end.x);
        properties.put("endY",(double) end.y);
        this.setProperties(properties);
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(this.getFillColor());
        canvas.drawLine(getProperties().get("startX").intValue() , getProperties().get("startY").intValue()
                ,getProperties().get("endX").intValue() ,getProperties().get("endY").intValue() );
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Line newOne = new Line(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getFillColor());
        newOne.setProperties(getProperties());
        return newOne;
    }
}