package practic;
import java.io.*;
import java.math.*;

class Vertex{
	public int x, y;	//координаты вершины
	public int r;		//радиус круга
	public Vertex(int x, int y, int r){
		this.x = x;
		this.y = y;
		this.r = r;
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
	public Vertex v[];
	public Edge e[];
	public Graph(){
		try {
			FileInputStream fs = new FileInputStream("in.txt");
			InputStreamReader is = new InputStreamReader(fs);
			BufferedReader br = new BufferedReader(is);
			String str = br.readLine();
			numV = Integer.parseInt(str);
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
				v[i] = new Vertex(x, y, r);
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
}
