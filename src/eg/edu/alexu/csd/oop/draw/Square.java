package eg.edu.alexu.csd.oop.draw;

import java.awt.*;

public class Square extends Rectangle{
    public Square(){
        super();
    }
    Square(Point start, Point end, Color color, Color fillColor) {
        super(start, end, color, fillColor);
        this.setColor(color);
        this.setFillColor(fillColor);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Square newOne = new Square(getPosition() ,
                new Point(getProperties().get("endX").intValue(),getProperties().get("endY").intValue())
                , getColor() , getFillColor());
        newOne.setProperties(getProperties());
        return newOne;
    }
}
