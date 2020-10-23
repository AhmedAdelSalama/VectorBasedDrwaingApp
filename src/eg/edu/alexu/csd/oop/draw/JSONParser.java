package eg.edu.alexu.csd.oop.draw;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class JSONParser extends HashMap implements Map {
    private Vector<Shape> shapesInCanvas;

    public JSONParser(Vector<Shape> shapesInCanvas) {
        this.shapesInCanvas = shapesInCanvas;
    }

    public void writeInFile(String path){
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.append("[\n\t");
            for(int i = 0 ; i < shapesInCanvas.size() ; i++){
                if(shapesInCanvas.get(i) instanceof Circle) {
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("circle,\n\t\t");
                }else if(shapesInCanvas.get(i) instanceof Ellipse){
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("ellipse,\n\t\t");
                }else if(shapesInCanvas.get(i) instanceof Line){
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("line,\n\t\t");
                }else if(shapesInCanvas.get(i) instanceof Rectangle){
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("rectangle,\n\t\t");
                }else if(shapesInCanvas.get(i) instanceof Triangle){
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("triangle,\n\t\t");
                }else if(shapesInCanvas.get(i) instanceof Square){
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("square,\n\t\t");
                }else {
                    fileWriter.write("{\n\t\t\"type\": ");
                    fileWriter.write("shape,\n\t\t");
                }
                fileWriter.write("\"fill color\": ");
                fileWriter.write(shapesInCanvas.get(i).getFillColor().toString()+",\n\t\t");
                fileWriter.write("\"color\": ");
                fileWriter.write(shapesInCanvas.get(i).getColor().toString()+",\n\t\t");
                fileWriter.write("\"startX\": ");
                fileWriter.write(shapesInCanvas.get(i).getProperties().get("startX").toString()+",\n\t\t");
                fileWriter.write("\"endX\": ");
                fileWriter.write(shapesInCanvas.get(i).getProperties().get("endX").toString()+",\n\t\t");
                fileWriter.write("\"startY\": ");
                fileWriter.write(shapesInCanvas.get(i).getProperties().get("startY").toString()+",\n\t\t");
                fileWriter.write("\"endY\": ");
                fileWriter.write(shapesInCanvas.get(i).getProperties().get("endY").toString()+",\n\t\t");
                if(!(shapesInCanvas.get(i) instanceof Triangle ||shapesInCanvas.get(i) instanceof Line)) {
                    fileWriter.write("\"width\": ");
                    fileWriter.write(shapesInCanvas.get(i).getProperties().get("w").toString() + ",\n\t\t");
                    //last property
                    fileWriter.write("\"height\": ");
                    fileWriter.write(shapesInCanvas.get(i).getProperties().get("h").toString() + "\n\t}");
                }else if(shapesInCanvas.get(i) instanceof Triangle){
                    fileWriter.write("\"thirdPointX\": ");
                    fileWriter.write(shapesInCanvas.get(i).getProperties().get("thirdPointX").toString() + ",\n\t\t");
                    fileWriter.write("\"thirdPointY\": ");
                    fileWriter.write(shapesInCanvas.get(i).getProperties().get("thirdPointY").toString() + "\n\t}");

                }else {

                }
                if(i == shapesInCanvas.size()-1){
                    if(shapesInCanvas.get(i) instanceof Line){
                        fileWriter.write("\t}\n]");
                    }else
                        fileWriter.write("\n]");
                }else{
                    if(shapesInCanvas.get(i) instanceof Line){
                        fileWriter.write("},\n\t");
                    }else
                        fileWriter.write(",\n\t");
                }

            }
            fileWriter.flush();

        } catch (IOException e) {
            e.getMessage();
        }

    }
    public Vector<Shape> parse(String path){
            try(FileReader fileReader = new FileReader(path)){
            shapesInCanvas.clear();
            Scanner sc = new Scanner(fileReader);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                if (s.contains("type")){
                    Shape shape = new Rectangle();
                    HashMap<String, Double> properties = new HashMap<>();
                    int f=0;
                    String startX="",endX="",startY="" ,endY="" ,thirdPointX="" ,thirdPointY="" ,width="",height="";
                    int r =0 , g=0 , b=0 , r1 = 0 , g1 = 0 , b1 = 0;
                    for (int i = 0; i < 8; i++) {
                        switch (i) {
                            case 0:
                                if (s.contains("circle")) {
                                    f=1;
                                } else if (s.contains("ellipse")) {
                                    f=2;
                                } else if (s.contains("line")) {
                                    f=3;
                                } else if (s.contains("rectangle")) {
                                    f=4;
                                } else if (s.contains("square")) {
                                    f=5;
                                } else if (s.contains("triangle")) {
                                    f=6;
                                }
                                break;
                            case 1:
                                int iterator1 = s.indexOf("[");
                                int iterator2 = s.indexOf("]");
                                String[] sub = s.substring(iterator1, iterator2).split(",");
                                //length or -1
                                r = (int)Double.parseDouble(sub[0].substring(sub[0].indexOf("=") + 1, sub[0].length()));
                                g = (int)Double.parseDouble(sub[1].substring(sub[1].indexOf("=") + 1, sub[1].length()));
                                b = (int)Double.parseDouble(sub[2].substring(sub[2].indexOf("=") + 1, sub[2].length()));
                                break;
                            case 2:
                                int iterator11 = s.indexOf("[");
                                int iterator22 = s.indexOf("]");
                                String[] sub1 = s.substring(iterator11, iterator22).split(",");
                                //length or -1
                                r1 = (int)Double.parseDouble(sub1[0].substring(sub1[0].indexOf("=") + 1, sub1[0].length()));
                                g1 = (int)Double.parseDouble(sub1[1].substring(sub1[1].indexOf("=") + 1, sub1[1].length()));
                                b1 = (int)Double.parseDouble(sub1[2].substring(sub1[2].indexOf("=") + 1, sub1[2].length()));
                                break;
                            case 3:
                                startX = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                break;
                            case 4:
                                endX = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                break;
                            case 5:
                                startY = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                break;
                            case 6:
                                endY = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                break;
                            case 7:
                                if (f==6) {
                                    thirdPointX = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                    s = sc.nextLine();
//                                    thirdPointY = s.substring(s.indexOf(":") + 2, s.indexOf("\n"));

                                } else if(!(f==3)) {
                                    width = s.substring(s.indexOf(":") + 2, s.indexOf(","));
                                    s = sc.nextLine();
//                                    height = s.substring((int) s.indexOf(":") + 2,(int) s.indexOf('\n'));
                                }
                                break;
                        }
                        if(i==7 ||(i==6 && f==3)){
                            Color fillColor = new Color(r,g,b);
                            Color color = new Color(r1,g1,b1);
                            Shape model = new Circle();
                            switch (f){
                                case 1:
                                model = new Circle(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)),
                                            new Point(((int)Double.parseDouble(startX)+(int)Double.parseDouble(width))
                                                    ,((int)Double.parseDouble(startY)+(int)Double.parseDouble(width))),
                                        color,fillColor);
                                    break;
                                case 2:
                                    model = new Ellipse(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)),
                                            new Point((int)Double.parseDouble(endX),(int)Double.parseDouble(endY)),color,fillColor);
                                    break;
                                case 3:
                                    model = new Line(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)) ,
                                            new Point((int)Double.parseDouble(endX),(int)Double.parseDouble(endY)) , fillColor);
                                    break;
                                case 4:
                                    model = new Rectangle(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)),
                                            new Point((int)Double.parseDouble(endX),(int)Double.parseDouble(endY)), color,fillColor);
                                    break;
                                case 5:
                                    model = new Square(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)),
                                            new Point(((int)Double.parseDouble(startX)+(int)Double.parseDouble(width))
                                                    ,((int)Double.parseDouble(startY)+(int)Double.parseDouble(width))),
                                            color,fillColor);
                                    break;
                                case 6:
                                    model = new Triangle(new Point((int)Double.parseDouble(startX),(int)Double.parseDouble(startY)),
                                            new Point((int)Double.parseDouble(endX),(int)Double.parseDouble(endY)), color,fillColor);
                                    break;
                            }
                            shapesInCanvas.add(model);
                        }
                        if(sc.hasNextLine()){
                            s=sc.nextLine();
                        }
                     }
                }
            }
            }catch (FileNotFoundException e1){ e1.getMessage();
            }catch (IOException e2){ e2.getMessage();
            }catch (Exception e4){ e4.getMessage();
            }
        return shapesInCanvas;
    }

}
