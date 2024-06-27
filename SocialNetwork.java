import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SocialNetwork
{
    private HashMap<Integer, TreeSet<Integer>> network;

    public SocialNetwork()
    {
        network = new HashMap<>();
    }

    //Part 1 Scanning and Adding into dataset/graph
    public void scanFile(Scanner scin) throws FileNotFoundException
    {
        int i,n,c;
        File readFile;
        do
        {
            System.out.print("Input File Path: ");
            readFile = new File(scin.nextLine());

            if(!readFile.exists())
            {
                System.out.println("File does not exist\n");
            }

        }while(!readFile.exists());

        try(Scanner scanFile = new Scanner(readFile);)
        {
            n = scanFile.nextInt();
            c = scanFile.nextInt();

            for(i=0;i<c;i++)
            {
                createEdge(scanFile.nextInt(),scanFile.nextInt());
            }
            scanFile.close();
        }
    }

    //Helper Function to create an edge for 2 nodes
    private void createEdge(int personA, int personB)
    {
        network.computeIfAbsent(personA, k -> new TreeSet<>()).add(personB);
        network.computeIfAbsent(personB, k -> new TreeSet<>()).add(personA);
    }

    //Part 2 Display User Friends
    public void displayFriends(int ID)
    {
        if(!network.containsKey(ID))
        {
            System.out.println("\nPerson "+ID+" does not exist");
            return;
        }

        //Sorting and Displaying
        System.out.println("\nPerson "+ID+" has "+network.get(ID).size()+" friends");
        System.out.println("List of friends: "+ network.get(ID));
    }

    public void checkConnection(int personA, int personB)
    {
        long i;
        if(!network.containsKey(personA) || !network.containsKey(personB))
        {
            System.out.println("Person A or B is not a real person\n");
            return;
        }

        else if(personA == personB)
        {
            System.out.println("Person A and B are the same people\n");
            return;
        }

        else if(network.get(personA).contains(personB))
        {
            System.out.println("There is a connection from "+personA+" to "+personB+"!");
            System.out.println(personA +" is friends with "+ personB +"\n");
            return;
        }

        List<Integer> path = checkPath(personA, personB, new HashSet<Integer>(), new ArrayList<Integer>());
        if (path.isEmpty()) 
        {
            System.out.println("Cannot find a connection between "+personA+" and "+personB+"\n");
            System.out.println();
        } 
        
        else 
        {
            System.out.println("There is a connection from "+personA+" to "+personB+"!");
            for (i = 0; i < path.size() - 1; i++) 
            {
                System.out.println(path.get((int)i) +" is friends with "+path.get((int) i + 1));
            }
            System.out.println();
        }
    }
    
    //Helper function to check the path (DFS referenced from online)
    private List<Integer> checkPath(int personA, int personB, Set<Integer> visit, List<Integer> path)
    {   
        visit.add(personA);
        path.add(personA);

        if (personA== personB) 
        {
            return new ArrayList<>(path);
        }

        for (int neighbor : network.get(personA)) 
        {
            if (!visit.contains(neighbor)) 
            {
                List<Integer> newPath = checkPath(neighbor, personB, visit, path);
                if (!newPath.isEmpty()) 
                {
                    return newPath;
                }
            }
        }
        path.remove(path.size()-1);
        return new ArrayList<>();
    }

    public static void main(String args[]) throws FileNotFoundException
    {
        int choice=0;
        SocialNetwork graph = new SocialNetwork();
        Scanner scin = new Scanner(System.in);
        graph.scanFile(scin);
        System.out.println("Graph is Loaded!");

        do
        {
            try
            {
                System.out.println("MAIN MENU");
                System.out.println("[1] Get friend list");
                System.out.println("[2] Get connection");
                System.out.println("[3] Exit\n");

                System.out.print("Enter your choice: ");
                choice = scin.nextInt();
            }
            catch(InputMismatchException e)
            {
                String buffer = scin.nextLine();
                System.out.print("Please Enter a number between 1-3\n");
            }

            if(choice == 1)
            {
                System.out.print("Enter ID of person: ");
                graph.displayFriends(scin.nextInt());
            }

            else if(choice == 2)
            {
                System.out.print("Enter ID of first person: ");
                int personA = scin.nextInt();
                System.out.print("Enter ID of second person: ");
                int personB = scin.nextInt();
                graph.checkConnection(personA, personB);
            }

            else if(choice == 3)
            {
                System.out.println("Terminating Program");
            }

            else if(choice < 1 || choice > 3)
            {
                System.out.println("Invalid Input, please try again");
            }
        }while(choice != 3);

        scin.close();
    }
}