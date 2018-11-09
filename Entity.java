import java.util.Arrays;

public abstract class Entity
{
    int nodeNumber;
    int[] neighbours;
    // Each entity will have a distance table
    protected int[][] distanceTable = new int[NetworkSimulator.NUMENTITIES]
                                           [NetworkSimulator.NUMENTITIES];

    // The update function.  Will have to be written in subclasses by students
    // public abstract void update(Packet p);
    public void update(Packet p) {
        System.out.println("Distance vector received from node " + p.getSource() + " to node " + p.getDest());
        int currentNode = p.getDest();
        int sourceNode = p.getSource();
        boolean sendFlag = false;
        System.out.println("Current Distance vector of node " + nodeNumber + " is " + Arrays.toString(getDistanceVector()));
        for (int i=0; i < NetworkSimulator.NUMENTITIES; i++) {
            int mincost = p.getMincost(i);
            int viaNeighbourDistance = mincost+NetworkSimulator.cost[sourceNode][currentNode];
            distanceTable[i][sourceNode] = viaNeighbourDistance;
            int oldValue = distanceTable[i][currentNode];
            distanceTable[i][currentNode] = NetworkSimulator.cost[i][currentNode];
            for (int j=0; j < NetworkSimulator.NUMENTITIES; j++) {
                if (distanceTable[i][j] < distanceTable[i][currentNode]) {
                    distanceTable[i][currentNode] = distanceTable[i][j];
                }
            }
            if (distanceTable[i][currentNode] != oldValue) {
                sendFlag = true;
            }
            // if (distanceTable[i][currentNode] > viaNeighbourDistance) {
            //     sendFlag = true;
            //     distanceTable[i][currentNode] = viaNeighbourDistance;
            // }
            
        }
        if (sendFlag) {
            System.out.println("Node " + p.getDest() + " says: my distance vector got updated so sending it to my neighbours");
            System.out.println("Updated Distance vector of node " + nodeNumber + " is " + Arrays.toString(getDistanceVector()));
            sendToNeighbours();
        } else {
            System.out.println("Node " + p.getDest() + " says: my distance vector did not update");
        }
        printDT();
    }
    
    // The link cost change handlder.  Will have to be written in appropriate
    // subclasses by students.  Note that only Entity0 and Entity1 will need
    // this, and only if extra credit is being done
    public abstract void linkCostChangeHandler(int whichLink, int newCost);

    // Print the distance table of the current entity.
    protected abstract void printDT();

    private int[] getDistanceVector() {
        int[] distanceVector = new int[NetworkSimulator.NUMENTITIES];
        for (int i=0; i < distanceTable.length; i++) {
            distanceVector[i] = distanceTable[i][nodeNumber];
        }
        return distanceVector;
    }

    protected void sendToNeighbours() {
        for (int i=0; i < neighbours.length; i++) {
            Packet p = new Packet(nodeNumber, neighbours[i], getDistanceVector());
            NetworkSimulator.toLayer2(p);
        }

    }

    protected void initializeDistanceTable() {
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

}
