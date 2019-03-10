
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Mohamed Elayat and Fatima Mostefai


//Controller method that ties the View and Controller
// classes using an ActionListener.
public class Controller implements ActionListener{

    View v;

    public Controller(  View v  ){
        this.v = v;
        v.addActionListeners(  this  );
    }


    public void actionPerformed(  ActionEvent click  ) {
        if (  click.getSource() == v.button && v.running == false  ) {
            int n = Integer.parseInt(  v.populationField.getText()  );
            int n2 = Integer.parseInt(  v.timeField.getText()  );
            v.m.executeBackgroundTask(  n, n2, v  );
        }
    }


}
