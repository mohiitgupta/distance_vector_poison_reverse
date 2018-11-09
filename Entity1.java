public class Entity1 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity1()
    {
        nodeNumber = 1;
        neighbours = new int[]{0,2};
        for (int i=0; i < this.distanceTable.length; i++) {
            for (int j=0; j < this.distanceTable.length; j++) {
                if (j==nodeNumber) {
                    distanceTable[i][j] = NetworkSimulator.cost[j][i];
                } else {
                    distanceTable[i][j] = 999;
                }
            }
        }
        sendToNeighbours(); 
        printDT();
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    // public void update(Packet p)
    // {
    // }
    
     public void linkCostChangeHandler(int whichLink, int newCost)
    {
        int oldValue = distanceTable[whichLink][nodeNumber];
        distanceTable[whichLink][nodeNumber] = newCost;
        distanceTable[whichLink][whichLink] = newCost;
        System.out.println("The link cost between node " + nodeNumber + " and " + whichLink + " changed to new value " + newCost);
        int minValue = 999;
        for (int j=0; j < NetworkSimulator.NUMENTITIES; j++) {
            if (distanceTable[whichLink][j] < minValue) {
                minValue = distanceTable[whichLink][j];
            }
        }
        if (minValue != oldValue) {
            distanceTable[whichLink][nodeNumber] = minValue;
            System.out.println("Node " + nodeNumber + " says: the link cost change affected my distance vector");
            printDT();
            sendToNeighbours();
        } else {
            System.out.println("Node " + nodeNumber + " says: the link cost change did not affect my distance vector");
        }
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("         via");
        System.out.println(" D1 |   0   1   2   3");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 1)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j ++)
            {
            
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
