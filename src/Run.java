
//Mohamed Elayat and Fatima Mostefai
//method to run the program


public class Run {

    public static void main(  String[] args  ){
        Model m = new Model();
        View v = new View(  m  );
        Controller c = new Controller(  v  );
    }

}
