package practic;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

class Vertex{
	public int x, y;	//координаты вершины
	public int num;
	public Vertex(int x, int y, int num){
		this.x = x;
		this.y = y;
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

public class Graph extends JPanel implements Runnable{
	public int numV;
	Semaphore sem;
	public int numE;
	public int numM;
	int currentEdge;
	JLabel doings;
	public Thread graphThread;
    public int r;
	public Vertex v[];
	public Edge e[];
	public Edge MFT[];
	public void run(){
		doings.setText("");
		repaint();
		Kruskal();
		
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
        //настройка шрифта
		g.setFont(g.getFont().deriveFont((float)r/2));
		//отрисовка всех ребер графа
		g.setColor(Color.BLACK);
		for(int i = 0; i < numE; i++){
			g.drawLine(e[i].from.x+r/2, e[i].from.y+r/2,e[i].to.x+r/2,e[i].to.y+r/2);        
		}
		//отрисовка ребер мод
		g.setColor(Color.RED);
		for(int i = 0; i < numM; i++){   
			g.drawLine(MFT[i].from.x+r/2, MFT[i].from.y+r/2, MFT[i].to.x+r/2, MFT[i].to.y+r/2);
		}
		//отрисовка весов ребер
		g.setColor(Color.BLUE);
		for(int i = 0; i<numE; i++){
			String weight = Integer.toString(e[i].weight);
			g.drawString(weight, (e[i].from.x + e[i].to.x +r)/2, (e[i].from.y + e[i].to.y +r)/2);
		}
		//отрисовка вершин
		g.setColor(Color.CYAN);
		for(int i = 0; i<numV; i++){
			g.fillOval(v[i].x,v[i].y, r, r);
		}
		if(currentEdge!=-1){
			g.drawLine(e[currentEdge].from.x + r/2, e[currentEdge].from.y + r/2, e[currentEdge].to.x + r/2, e[currentEdge].to.y + r/2);
			
		}
		//отрисовка номеров вершин
		g.setColor(Color.BLACK);
		for(int i = 0; i<numV; i++){
			g.drawString(Integer.toString(i), v[i].x + 3*r/8, v[i].y + 2*r/3);
		}
	}
	private void bothConstr(){
		currentEdge = -1;
		doings = new JLabel("");					//создание метки под вывод последовательности действий
		graphThread = new Thread(this);
		this.setLayout(new BorderLayout());
		this.add(doings,BorderLayout.NORTH);
		double angle = 6.2831853/numV;				//вычиление расположения вершин
        double nextangle = 0;
        int R = 200;
        r = (int)(6.28*R)/(4*numV);
        if(r>50)r = 50;
        if (r < 20){
            //добавить скролл бар
            r = 20;
        }
        int CentrX=250, CentrY=250;
        int x, y;
        for(int i = 0; i<numV; i++){
            x = (int) (CentrX + Math.cos(nextangle)*R);
            y = (int) (CentrY + Math.sin(nextangle)*R);
            nextangle += angle;
            v[i] = new Vertex(x, y, i);
        }
	}
	public Graph(int numvert, int proc, Semaphore sem){
		//инициализация графа по заданым параметрам
		int max = numvert*(numvert-1)/2;			//максимально возможное число верин в графе
		int must = (int) (proc*numvert*(numvert-1)/100)/2;			//необходимое нам число вершин
		int counter = 0;							//счетчик для массива вершин
		numV = numvert;								//инициализация полей графа
		numE = must;
		e = new Edge[must];							//создание массивов под вершины, ребра и мод
		v = new Vertex[numvert];
		MFT = new Edge[numV-1];
		
		this.sem = sem;
		
		bothConstr();
        
		Random rand = new Random(System.currentTimeMillis());			//рандомные значения
		Random weightrand = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < numvert && must > 0; i++){					//идем по вершинам, от которых можно провести ребро
			for(int j = i+1; j < numvert && must > 0; j++){				//к которым можно его провести
				if(rand.nextInt(max)< must){							//ставим ребро между i и j с вероятностью must/max
					must--;												//если ставим, уменьшаем оставшееся число ребер
					e[counter] = new Edge(v[i],v[j],weightrand.nextInt(max)+1);	//ставим ребро
					counter++;											//увеличиваем счетчик массива
				}
				max--;													//уменьшаем максимально возможное оставшееся число ребер
			}
		}
		graphThread.start();
	}
	public Graph(File file, Semaphore sem){
            Scanner in;
            try {
                in = new Scanner(file);
               
                
                
                this.sem = sem;
                doings = new JLabel("");
                
                numV = in.nextInt();
                
                MFT = new Edge[numV-1];
                v = new Vertex[numV];
                numE = in.nextInt();
                bothConstr();
                e = new Edge[numE];
                for(int i = 0; i<numE; i++){
                    int from = in.nextInt();
                    int to = in.nextInt();
                    int weight = in.nextInt();
                                    e[i] = new Edge(v[from], v[to], weight);
                            }
                in.close();
                graphThread.start();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }

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
	public void Kruskal(){
		sortEdges();
		String result = "Последовательность добавления: ";
		doings.setText(result);
		boolean unions[][]  = new boolean[numV][numV];
		int counter = 0;
		for (int j = 0; j< numV; j++){
			unions[j][j] = true; 
		}
		
		for(int i = 0; i<numE && (counter < numV-1); i++){
			
			try {
				sem.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			currentEdge = i;
			doings.setText(result);
			this.repaint();
			try {
				graphThread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				}
				MFT[counter] = help;
				counter++;
				result+=help.from.num+"-> "+help.to.num+" ;";
				doings.setText(result);
				currentEdge = -1;
				this.repaint();
				numM = counter;
			}
			else{
				doings.setText("ЦИКЛ!");
				currentEdge = -1;
				this.repaint();
			}
		}
		
		
	}
}
