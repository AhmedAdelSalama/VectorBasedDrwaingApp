package eg.edu.alexu.csd.oop.draw;

import org.junit.internal.Classes;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Controller extends JFrame implements DrawingEngine , ActionListener , MouseListener , MouseMotionListener{
    private int drawShape = -1;
    private String whatToDoNow = "move";
    private Point start;
    private final Color color = Color.black;
    private Color fillColor= Color.green;
    private Shape created = new Square(new Point(0,0),new Point(0,0),Color.cyan,Color.BLACK);
    private Vector<Shape> shapesInCanvas = new Vector<>();
    private final Canvas canvas ;
    private final JRadioButton circle;
    private final JRadioButton ellipse;
    private final JRadioButton line;
    private final JRadioButton square;
    private final JRadioButton rectangle;
    private final JRadioButton triangle;
    private final JRadioButton delete;
    private final JRadioButton fill;
    private final JRadioButton resize;
    private final JRadioButton move;
    private final JButton colorChooser = new JButton("Change color");
    private final JButton colorTemp = new JButton(" ");
    private final JMenuItem undo, redo, uploadClass;
    private JFileChooser fileChooser;
    private final Stack<Vector<Shape> > undoStack = new Stack<>();
    private final Stack<Vector<Shape> > redoStack = new Stack<>();
    private final List<Class<? extends Shape>> supportedClasses = new LinkedList<>() ;

   Controller() throws HeadlessException {
        super("Paint");
        canvas = new Canvas();
        canvas.setBackground(Color.white);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        Vector<Shape> temp = new Vector<>(shapesInCanvas);
        undoStack.push(temp);
        supportedClasses.add(Rectangle.class);
        supportedClasses.add(Circle.class);
        supportedClasses.add(Ellipse.class);
        supportedClasses.add(Square.class);
        supportedClasses.add(Triangle.class);
        supportedClasses.add(Line.class);
        installPluginShape("RoundRectangle.jar");
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("save");
        JMenuItem load = new JMenuItem("load");
        file.add(save);
        file.add(load);
        JMenu edit = new JMenu("Edit");
        undo = new JMenuItem("undo");
        redo = new JMenuItem("redo");
        uploadClass = new JMenuItem("uploadClass");
        edit.add(undo);
        edit.add(redo);
        edit.add(uploadClass);
        menuBar.add(file);
        menuBar.add(edit);
        save.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save a file");
            //one more step to choose
            FileFilter filter = new FileNameExtensionFilter("Only XML & JSON Files" , "xml" , "json");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(null);
             if(result == JFileChooser.APPROVE_OPTION) {
                save(fileChooser.getSelectedFile().toString());
             }
            refresh(canvas.getGraphics());
        });
        load.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open a file");
            FileFilter filter = new FileNameExtensionFilter("Only XML & JSON Files" , "xml" , "json");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                load(fileChooser.getSelectedFile().toString());
            }
            refresh(canvas.getGraphics());
        });


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(menuBar);
        panel.setPreferredSize(new Dimension(1500 , 30));

        //creating tool bar
        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        JLabel shapes = new JLabel("What to do now ?");
        circle = new JRadioButton("Draw a circle");
        ellipse = new JRadioButton("Draw an ellipse");
        line = new JRadioButton("Draw a line");
        square = new JRadioButton("Draw a square");
        rectangle = new JRadioButton("Draw a rectangle");
        triangle = new JRadioButton("Draw a triangle");
        delete = new JRadioButton("delete");
        move = new JRadioButton("move");
        resize = new JRadioButton("resize");
        fill = new JRadioButton("fill");




       colorChooser.addActionListener(this);

        ButtonGroup groupTools = new ButtonGroup();

        undo.addActionListener(this);
        redo.addActionListener(this);
        uploadClass.addActionListener(this);

        circle.addActionListener(this);
        ellipse.addActionListener(this);
        line.addActionListener(this);
        square.addActionListener(this);
        triangle.addActionListener(this);
        rectangle.addActionListener(this);
        delete.addActionListener(this);
        move.addActionListener(this); move.setSelected(true);
        resize.addActionListener(this);
        fill.addActionListener(this);
        groupTools.add(circle );
        groupTools.add(ellipse);
        groupTools.add(line);
        groupTools.add(square);
        groupTools.add(rectangle);
        groupTools.add(triangle);
        groupTools.add(move);
        groupTools.add(resize);
        groupTools.add(delete);
        groupTools.add(fill);

        //adding components to the tool bar
        toolBar.add(shapes);
        toolBar.add(circle );
        toolBar.add(ellipse);
        toolBar.add(line);
        toolBar.add(square);
        toolBar.add(rectangle);
        toolBar.add(triangle);
        toolBar.add(move);
        toolBar.add(resize);
        toolBar.add(delete);
        toolBar.add(fill);

        colorChooser.setBackground(Color.WHITE);
        colorTemp.setBackground(fillColor);
        colorChooser.setMaximumSize(new Dimension(100 , 30));
        colorTemp.setMaximumSize(new Dimension(15,15));
        toolBar.add(colorChooser);
        toolBar.add(colorTemp);
        JScrollPane scrollPane = new JScrollPane(toolBar , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setPreferredSize(new Dimension(150 , 1500));

        add(panel , BorderLayout.NORTH);
        add(scrollPane , BorderLayout.WEST);
        add(canvas , BorderLayout.CENTER);
        setSize(1500,850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
        setVisible(true);

    }

    @Override
    public void refresh(Graphics canvas) {
        for(int i=0;i<shapesInCanvas.size();i++){
            shapesInCanvas.elementAt(i).draw(canvas);
        }
    }

    @Override
    public void addShape(Shape shape) {
        shapesInCanvas.add(shape);
        Vector<Shape> temp = new Vector<>(shapesInCanvas);
        undoStack.push(temp);
        redoStack.clear();
    }

    @Override
    public void removeShape(Shape shape) {
        shapesInCanvas.remove(shape);
        Vector<Shape> temp = new Vector<>(shapesInCanvas);
        undoStack.push(temp);
        redoStack.clear();
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        shapesInCanvas.set(shapesInCanvas.indexOf(oldShape) , newShape);
        Vector<Shape> temp = new Vector<>(shapesInCanvas);
        undoStack.push(temp);
        redoStack.clear();
    }

    @Override
    public Shape[] getShapes() {
       Shape[] shapes = new Shape[shapesInCanvas.size()];
       for(int i=0;i<shapesInCanvas.size();i++) {
           shapes[i]= shapesInCanvas.get(i);
       }
        return shapes;
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return supportedClasses;
    }

    @Override
    public void undo() {
        if(undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            shapesInCanvas = new Vector<>(undoStack.peek());
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
    //    System.out.println(shapesInCanvas.size() + " " + undoStack.size() + " "+redoStack.size());

    }

    @Override
    public void redo() {
        if(redoStack.size() > 0) {
            undoStack.push(redoStack.pop());
            shapesInCanvas = new Vector<>(undoStack.peek());
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
    //    System.out.println(shapesInCanvas.size() + " " + undoStack.size() + " "+redoStack.size());
    }

    @Override
    public void save(String path) {
        try {
            if(path.endsWith(".xml")) {
                //XML
                FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
                XMLEncoder encoder = new XMLEncoder(fileOutputStream);
                encoder.writeObject(shapesInCanvas);
                encoder.close();
                fileOutputStream.close();
            }        }
        catch (IOException e){
            e.getMessage();
        }
        if(path.endsWith(".json")) {
//        //JSON
                JSONParser jsonParser = new JSONParser(shapesInCanvas);
                jsonParser.writeInFile(path);
        }
        canvas.update(canvas.getGraphics());
        refresh(canvas.getGraphics());

    }

    @Override
    public void load(String path) {
        try {
            if(path.endsWith(".xml")) {
                FileInputStream fileInputStream = new FileInputStream(path);
                XMLDecoder decoder = new XMLDecoder(fileInputStream);
                shapesInCanvas = (Vector<Shape>) decoder.readObject();
                decoder.close();
                fileInputStream.close();
            }

        } catch (IOException e){
            e.getMessage();
        }
//        //JSON
        if(path.endsWith(".json")) {
            JSONParser jsonParser = new JSONParser(shapesInCanvas);
            shapesInCanvas.clear();
            shapesInCanvas = jsonParser.parse(path);
        }
        canvas.update(canvas.getGraphics());
        refresh(canvas.getGraphics());
    }

    private int shapeOwnThisPoint(Point point){
        for(int i=shapesInCanvas.size()-1 ; i>=0 ; i--){
            Shape temp = shapesInCanvas.elementAt(i);
            Point start = new Point( temp.getProperties().get("startX").intValue() ,temp.getProperties().get("startY").intValue() );
            Point end = new Point( temp.getProperties().get("endX").intValue() ,temp.getProperties().get("endY").intValue() );

            if(temp instanceof Ellipse){
                Point center = new Point( temp.getProperties().get("centerX").intValue() ,temp.getProperties().get("centerY").intValue() );
                try {
                    if (Math.pow((double) (point.x - center.x) / (center.x - start.x), 2) +
                            Math.pow((double) (point.y - center.y) / (center.y - start.y), 2) <= 1) return i;
                }
                catch (Exception ignored){

                }
            }

            else if(temp instanceof Rectangle){
                if(point.x >= start.x && point.y >= start.y && point.x<=end.x && point.y<=end.y) return i;
            }


            else if(temp instanceof Line){
                try {
                    if (point.y == start.y + (end.y - start.y) * (point.x - start.x) / (end.x - start.x)) return i;
                }
                catch (Exception ignored) {
                }
            }

            else if (temp instanceof Triangle){
                try {
                    if (point.x >= start.x && point.y >= start.y && point.x <= end.x && point.y <= end.y &&
                            point.y >= start.y + (end.y - start.y) * (point.x - start.x) / (end.x - start.x)) return i;
                }
                catch (Exception ignored){

                }
            }
        }
        return -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index =  shapeOwnThisPoint(e.getPoint());
        if(whatToDoNow.equals("delete") && index != -1){
            shapesInCanvas.remove(index);
            Vector<Shape> temp = new Vector<>(shapesInCanvas);
            undoStack.push(temp);
            redoStack.clear();
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
        else if (whatToDoNow.equals("fill") && index != -1){
            try {
                created = (Shape) shapesInCanvas.elementAt(index).clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
            created.setFillColor(fillColor);
            shapesInCanvas.remove(index);
            shapesInCanvas.add(created);
            Vector<Shape> temp = new Vector<>(shapesInCanvas);
            undoStack.push(temp);
            redoStack.clear();
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
     }

    @Override
    public void mousePressed(MouseEvent e) {
        start = e.getPoint();
        if(whatToDoNow.equals("draw")){
            shapesInCanvas.add(new Square(new Point(0,0),new Point(0,0),Color.white,Color.white));
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(whatToDoNow.equals("draw") || whatToDoNow.equals("move") ||  whatToDoNow.equals("resize")){
            Vector<Shape> temp = new Vector<>(shapesInCanvas);
            undoStack.push(temp);
            redoStack.clear();
        }

        System.out.println(shapesInCanvas.size() + " " + undoStack.size() + " "+redoStack.size());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point end = e.getPoint();
        int index =  shapeOwnThisPoint(start);

        if(whatToDoNow.equals("draw")) {
            int minX = Integer.min(start.x, end.x);
            int minY = Integer.min(start.y, end.y);
            int maxX = Integer.max(start.x, end.x);
            int maxY = Integer.max(start.y, end.y);
            int max = Math.max(maxX - minX, maxY - minY);
            switch (drawShape) {
                case 0: created = new Circle(new Point(minX,minY),new Point(minX+max,minY+ max), color,fillColor); break;
                case 1: created = new Line(start, end,fillColor); break;
                case 2: created = new Ellipse(new Point(minX,minY),new Point(maxX,maxY),color,fillColor); break;
                case 3: created = new Square(new Point(minX,minY),new Point(minX+max,minY+ max), color,fillColor); break;
                case 4: created = new Rectangle(new Point(minX,minY),new Point(maxX,maxY), color,fillColor ); break;
                case 5: created = new Triangle(new Point(minX,minY),new Point(maxX,maxY), color,fillColor ); break;
            }
            if (drawShape <= 5 && drawShape >= 0) {
                shapesInCanvas.remove(shapesInCanvas.lastElement());
                shapesInCanvas.add(created);
                canvas.update(canvas.getGraphics());
                refresh(canvas.getGraphics());
            }
           }

        else if (whatToDoNow.equals("move") && index !=-1) {

            Point oldStart = new Point(shapesInCanvas.elementAt(index).getProperties().get("startX").intValue() ,
                    shapesInCanvas.elementAt(index).getProperties().get("startY").intValue() );

            Point oldEnd = new Point(shapesInCanvas.elementAt(index).getProperties().get("endX").intValue() ,
                    shapesInCanvas.elementAt(index).getProperties().get("endY").intValue() );

            Point newStart = new Point(oldStart.x + end.x - start.x ,oldStart.y + end.y - start.y );
            Point newEnd = new Point(oldEnd.x + end.x - start.x ,oldEnd.y + end.y - start.y );

            Map<String, Double> properties = new HashMap<>();
            properties.put("startX",(double)newStart.x);
            properties.put("startY",(double)newStart.y);
            properties.put("endX",(double)newEnd.x);
            properties.put("endY",(double)newEnd.y);
            Point center = new Point((newEnd.x + newStart.x)/2 , (newEnd.y + newStart.y)/2 );
            properties.put("centerX",(double) center.x);
            properties.put("centerY",(double) center.y);
            Point thirdPoint = new Point(Math.min(newStart.x,newEnd.x) , Math.max(newStart.y,newEnd.y));
            properties.put("thirdPointX",(double) thirdPoint.x);
            properties.put("thirdPointY",(double) thirdPoint.y);
            start.x += end.x-start.x;
            start.y += end.y-start.y;
            try {
                created = (Shape) shapesInCanvas.elementAt(index).clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
            created.setPosition(newStart);
            created.setProperties(properties);
            shapesInCanvas.remove(shapesInCanvas.elementAt(index));
            shapesInCanvas.add(created);
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
        else if (whatToDoNow.equals("resize") && index!=-1) {
            Point oldStart = new Point(shapesInCanvas.elementAt(index).getProperties().get("startX").intValue() ,
                    shapesInCanvas.elementAt(index).getProperties().get("startY").intValue() );

            Point oldEnd = new Point(shapesInCanvas.elementAt(index).getProperties().get("endX").intValue() ,
                    shapesInCanvas.elementAt(index).getProperties().get("endY").intValue() );
            Point newEnd = new Point(oldEnd.x + end.x - start.x ,oldEnd.y + end.y - start.y );
            shapesInCanvas.elementAt(index).setPosition(oldStart);
            Map<String, Double> properties = new HashMap<>();
            properties.put("startX",(double) oldStart.x);
            properties.put("startY",(double) oldStart.y);
            properties.put("endX",(double)newEnd.x);
            properties.put("endY",(double)newEnd.y);
            Point center = new Point((newEnd.x + oldStart.x)/2 , (newEnd.y + oldStart.y)/2 );
            properties.put("centerX",(double) center.x);
            properties.put("centerY",(double) center.y);
            Point thirdPoint = new Point(Math.min(oldStart.x,newEnd.x) , Math.max(oldStart.y,newEnd.y));
            properties.put("thirdPointX",(double) thirdPoint.x);
            properties.put("thirdPointY",(double) thirdPoint.y);
            if(shapesInCanvas.elementAt(index) instanceof Circle || shapesInCanvas.elementAt(index) instanceof Square){
                int minX = Integer.min(oldStart.x,newEnd.x);
                int minY = Integer.min(oldStart.y,newEnd.y);
                int maxX = Integer.max(oldStart.x,newEnd.x);
                int maxY = Integer.max(oldStart.y,newEnd.y);
                int max = Math.max(maxX - minX, maxY - minY);
                properties.put("startX", (double) minX);
                properties.put("startY", (double) minY);
                properties.put("endX", (double) minX+max);
                properties.put("endY", (double) minY+max);
            }
            start.x += end.x-start.x;
            start.y += end.y-start.y;
            try {
                created = (Shape) shapesInCanvas.elementAt(index).clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
            created.setProperties(properties);
            shapesInCanvas.remove(shapesInCanvas.elementAt(index));
            shapesInCanvas.add(created);
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (circle.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 0;
        } else if (line.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 1;
        } else if (ellipse.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 2;
        } else if (square.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 3;
        } else if (rectangle.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 4;
        } else if (triangle.isSelected()) {
            whatToDoNow = "draw";
            drawShape = 5;
        } else if (delete.isSelected()) {
            whatToDoNow = "delete";
        } else if (move.isSelected()) {
            whatToDoNow = "move";
        } else if (resize.isSelected()) {
            whatToDoNow = "resize";
        } else if (fill.isSelected()) {
            whatToDoNow = "fill";
        }if(e.getSource().equals(undo)){
            undo();
        }
        if(e.getSource().equals(redo)){
            redo();
        }

        if(e.getSource().equals(colorChooser)){
            fillColor = JColorChooser.showDialog(this , "Choose a color" , fillColor);
            colorTemp.setBackground(fillColor);
            Vector<Shape> temp = new Vector<>(shapesInCanvas);
            undoStack.push(temp);
            redoStack.clear();
            canvas.update(canvas.getGraphics());
            refresh(canvas.getGraphics());
        }

        if(e.getSource().equals(uploadClass)){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select the class");
            FileFilter filter = new FileNameExtensionFilter("Only Jar Files" , "jar");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(null);
            installPluginShape(fileChooser.getSelectedFile().toString());

        }
    }

    public void installPluginShape(String jarPath) {
        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = { new URL("jar:file:" + jarPath +"!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace('/', '.');
                Class loadClass = cl.loadClass(className);
                if (Shape.class.isAssignableFrom(loadClass))
                {
                    supportedClasses.add((Class<Shape>)loadClass);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

}

