/**
 * Created by jrmylee on 12/2/17.
 */
public class PrintVisitor implements Visitor<String>{

    @Override
    public void visit(String obj) {
        System.out.println(obj);
    }

}
