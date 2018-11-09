public class Entity3 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity3()
    {
        nodeNumber = 3;
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
    }
    
    public void printDT()
    {
        System.out.println("         via");
        System.out.println(" D3 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 3)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j +=2)
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
