package name.eipi.pacemaker.controllers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

public class TableTest {


    private Table<Object, Object, Object> weightedGraph = HashBasedTable.create();


    @Test
    public void testTables() throws Exception {

        weightedGraph.put("a", 1, 4);
        weightedGraph.put("b", 1, 20);
        weightedGraph.put("v", 1, 5);

        System.out.println(weightedGraph.row("a"));
        System.out.println(weightedGraph.column(1));

    }

}
