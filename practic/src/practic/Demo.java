package practic;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

public class Demo {
	Thread mainThread;
	JLabel step;
	static int count;
	Semaphore sem;
	JFrame mainWindow;
	JButton next;
	Graph gr;
	
	Demo(){
		mainWindow = new JFrame("Визуализация алгоритма Краскала");		//создается контейнер, содержащий окно
		mainWindow.setSize(1000, 700);									//задается его размер
		mainWindow.setLayout(new BorderLayout());						//расположение объектов на нем
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//чтобы закрывалася на 
		mainWindow.setVisible(true);									//становится видимым
		
		mainThread = Thread.currentThread();
		
		sem = new Semaphore(1);											//создается семафор на 1 разрешение
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gr = new Graph(5,100,sem);									//создается граф
		mainWindow.add(gr, BorderLayout.CENTER);						//добавляется на окно
		
		step = new JLabel("Нажмите кнопку \"Далее\" для следующего шага");	//метка, отображающая номер шага
		
		next = new JButton("Далее");									//оздается кнопка
		next.addActionListener(new ActionListener(){					//задается действие при нажатии на кнопку
			public void actionPerformed(ActionEvent ae){
				if(gr.graphThread.isAlive()){
				count++;												//увеличивается счетчик шагов
				step.setText("Шаг №"+Integer.toString(count));			//записывается номер шага
				sem.release();
				}
				else {
					step.setText("Завершено!");	
				}
			} 
		});
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Граф");
		JMenuItem load = new JMenuItem("Загрузить из файла");
        JMenuItem generate = new JMenuItem("Генерация");
        JMenuItem show = new JMenuItem("Показать ребра");
        menu.add(show);
		//pp.setLayout( new BorderLayout());
		menu.add(load);
        menu.add(generate);
		menubar.add(menu);
		mainWindow.setJMenuBar(menubar);
		
		show.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent AE){
				JFrame edges = new JFrame("Ребра графа");
				edges.setSize(500, 500);									//задается его размер
				edges.setLayout(new BorderLayout());						//расположение объектов на нем
				edges.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);		//чтобы закрывалася на 
				edges.setVisible(true);	
				
				JTextArea list = new JTextArea();
				//JLabel list = new JLabel();
				String res = new String();
				for(int i = 0; i < gr.numE; i++){
					res+=" ИЗ "+gr.e[i].from.num+"     В "+gr.e[i].to.num+"   весом "+gr.e[i].weight+"\n";
				}
				list.setText(res);
				list.setEditable(false);
				//list.setText(res);
				edges.add(list);
				JScrollPane scroll = new JScrollPane(list);
				edges.add(scroll);
				
			}
		});
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				FileDialog fileDialog = new FileDialog(mainWindow, "Выбор файла", FileDialog.LOAD);
				fileDialog.show();
				if(fileDialog.getFile()!=null){
					count = 0;
					step.setText("Нажмите кнопку \"Далее\" для следующего шага");
					File file = new File(fileDialog.getFile());
					gr.graphThread.stop();
					sem = new Semaphore(1);
					try {
						sem.acquire();
					} catch (InterruptedException e) {}
					mainWindow.remove(gr);
					gr = new Graph(file, sem);
					mainWindow.add(gr);
				}
			}
		});
		
		
		generate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String numv =JOptionPane.showInputDialog(mainWindow,"Введите число вершин:","Выбор числа вершин", JOptionPane.PLAIN_MESSAGE);
				String proc = JOptionPane.showInputDialog(mainWindow,"Введите % ребер от 0 до 100:","Выбор % ребер", JOptionPane.PLAIN_MESSAGE);
				if(numv!=null &&proc != null ){
					count = 0;
					step.setText("Нажмите кнопку \"Далее\" для следующего шага");
					gr.graphThread.stop();
					sem = new Semaphore(1);
					try {
						sem.acquire();
					} catch (InterruptedException e) {}
					mainWindow.remove(gr);
					gr = new Graph(Integer.parseInt(numv),Integer.parseInt(proc), sem);
					mainWindow.add(gr);
				}
			}
		});
		
		mainWindow.add(next, BorderLayout.SOUTH);
		mainWindow.add(step, BorderLayout.NORTH);										//добавляем ее в контейнер

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Demo();
			}
		});

	}

}
