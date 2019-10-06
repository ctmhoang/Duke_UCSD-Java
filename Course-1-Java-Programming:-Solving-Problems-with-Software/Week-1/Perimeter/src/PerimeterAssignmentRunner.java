import edu.duke.*;

import java.io.File;

public class PerimeterAssignmentRunner {
    public double getPerimeter(Shape s) {
        // Start with totalPerim = 0
        double totalPerim = 0.0;
        // Start wth prevPt = the last point
        Point prevPt = s.getLastPoint();
        // For each point currPt in the shape,
        for (Point currPt : s.getPoints()) {
            // Find distance from prevPt point to currPt
            double currDist = prevPt.distance(currPt);
            // Update totalPerim by currDist
            totalPerim = totalPerim + currDist;
            // Update prevPt to be currPt
            prevPt = currPt;
        }
        // totalPerim is the answer
        return totalPerim;
    }

    public int getNumPoints(Shape s) {
        // Put code here
//        returns an integer that is the number of points in Shape s
        int qual = 0;
        for (Point p : s.getPoints()) {
            qual += 1;
        }
        return qual;
    }

    public double getAverageLength(Shape s) {
        // Put code here
//        returns a number of type double that is the calculated average of all the sides’ lengths in the Shape S.
        double tot = getPerimeter(s), num = getNumPoints(s);
        return tot / num;
    }

    public double getLargestSide(Shape s) {
        // Put code here
//        returns a number of type double that is the longest side in the Shape S
        double largestSide = 0.0;
        Point prevPt = s.getLastPoint();
        for (Point a : s.getPoints()) {
            if (largestSide < prevPt.distance(a)) largestSide = prevPt.distance(a);
            prevPt = a;
        }
        return largestSide;
    }

    public double getLargestX(Shape s) {
//        returns a number of type double that is the largest x value over all the points in the Shape s.
        double maxVal = 0.0;
        for (Point a : s.getPoints()) {
            if (maxVal < a.getX()) maxVal = a.getX();
        }
        return maxVal;
    }

    public double getLargestPerimeterMultipleFiles() {
        // Put code here
//        return the the largest perimeter over all the shapes in the files you have selected.
        double maxPer = 0.0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            Shape s = new Shape(fr);
            if (maxPer < getPerimeter(s)) maxPer = getPerimeter(s);
        }
        return maxPer;
    }

    public File getFileWithLargestPerimeter() {
        // Put code here
//        returns the File that has the largest such perimeter, so it has return type File
        File maxPerimFile = null;
        double maxPerimVal = 0.0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            Shape s = new Shape(fr);
            if (getPerimeter(s) > maxPerimVal) {
                maxPerimFile = f;
                maxPerimVal = getPerimeter(s);
            }
            ;
        }
        return maxPerimFile;
    }

    public void testPerimeter() {
//        select a data file by using the FileResource class
        FileResource fr = new FileResource();
//        create a shape based on the points from that data file
        Shape s = new Shape(fr);
//        calculate the perimeter of the shape and output its value.
        double length = getPerimeter(s);
        System.out.println("perimeter = " + length);
        System.out.println("Tot Point: " + getNumPoints(s));
        System.out.println("Average Length: " + getAverageLength(s));
        System.out.println("Largest Side: " + getLargestSide(s));
        System.out.println("Largest X: " + getLargestX(s));
        testPerimeterMultipleFiles();
        testFileWithLargestPerimeter();

    }

    public void testPerimeterMultipleFiles() {
        // Put code here
        System.out.println(getLargestPerimeterMultipleFiles());
    }

    public void testFileWithLargestPerimeter() {
        // Put code here
        System.out.println(getFileWithLargestPerimeter().getName());
    }

    // This method creates a triangle that you can use to test your other methods
    public void triangle() {
        Shape triangle = new Shape();
        triangle.addPoint(new Point(0, 0));
        triangle.addPoint(new Point(6, 0));
        triangle.addPoint(new Point(3, 6));
        for (Point p : triangle.getPoints()) {
            System.out.println(p);
        }
        double peri = getPerimeter(triangle);
        System.out.println("perimeter = " + peri);
    }

    // This method prints names of all files in a chosen folder that you can use to test your other methods
    public void printFileNames() {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            System.out.println(f);
        }
    }

    public static void main(String[] args) {
        PerimeterAssignmentRunner pr = new PerimeterAssignmentRunner();
        pr.testPerimeter();
    }
}
