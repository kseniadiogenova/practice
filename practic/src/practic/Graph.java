package practic;
import java.io.*;
import java.math.*;

class Vertex{
	public int x, y;	//координаты вершины
	public int r;		//радиус круга
	public int num;
	public Vertex(int x, int y, int r,int num){
		this.x = x;
		this.y = y;
		this.r = r;
		this.num = num;
	}
}

class Edge{
	Vertex from;
	Vertex to;
	int weight;
	Edge(Vertex from, Vertex to, int weight){
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
}

public class Graph {
	public int numV;
	public int numE;
	public int numM;
	public Vertex v[];
	public Edge e[];
	public Edge MFT[];
	public Graph(){
		try {
			
			FileInputStream fs = new FileInputStream("in.txt");
			InputStreamReader is = new InputStreamReader(fs);
			BufferedReader br = new BufferedReader(is);
			String str = br.readLine();
			numV = Integer.parseInt(str);
			MFT = new Edge[numV-1];
			v = new Vertex[numV];
			double angle = 6.2831853/numV;
			double nextangle = 0;
			int R = 200;
			int r = 50;
			int CentrX=250, CentrY=250;
			int x, y;
			for(int i = 0; i<numV; i++){
				x = (int) (CentrX + Math.cos(nextangle)*R);
				y = (int) (CentrY + Math.sin(nextangle)*R);
				nextangle += angle;
				v[i] = new Vertex(x, y, r, i);
			}
			str = br.readLine();
			numE = Integer.parseInt(str);
			e = new Edge[numE];
			for(int i = 0; i<numE; i++){
				str = br.readLine();
				int from = Integer.parseInt(str);
				str = br.readLine();
				int to = Integer.parseInt(str);
				str = br.readLine();
				int weight = Integer.parseInt(str);
				e[i] = new Edge(v[from], v[to], weight);
			}
			br.close();
			
		}
		catch(IOException e){};
	}
	private void sortEdges(){
		for(int i = 0; i <numE-1; i++){
			for(int j = i+1; j<numE; j++){
				if(e[i].weight >e[j].weight){
					Edge help = e[i];
					e[i]=e[j];
					e[j]=help;
				}
			}
		}
	}
	public String Kruskal(){
		sortEdges();
		String result = new String();
		boolean unions[][]  = new boolean[numV][numV];
		int counter = 0;
		for (int j = 0; j< numV; j++){
			unions[j][j] = true; 
		}
		
		for(int i = 0; i<numE && (counter < numV-1); i++){
			Edge help = e[i];
			int from = e[i].from.num;
			int to = e[i].to.num;
			int h = -1;
			int h2 = -1;
			for(int j = 0; (j < from && h == -1); j++)
				if(unions[from][j])
					h=j;
				
			for(int j = 0; (j<to &&h2==-1); j++)
				if(unions[to][j])
					h2=j;
			if(h2!=-1)to = h2;
			if(h!=-1)from = h;
			if(to<from){
				h = to;
				to = from;
				from = h;
			}
			boolean diffUnions = true;
			for(int j = 0; j< numV && diffUnions; j++){
				if((unions[j][from] == unions[j][to])&&(unions[j][from] == true)){
					diffUnions = false;
				}
			}
			if(diffUnions){
				for(int j = 0; j<numV; j++){
					if(unions[j][to]){
						unions[j][from] = true;
					}
					//unions[j][from] = unions[j][to] = unions[j][from] || unions[j][to];
				}
				MFT[counter] = help;
				counter++;
				result+=help.from.num+"-> "+help.to.num+" ;";
			}
		}
		numM = counter;
		return result;
		
	}
}
