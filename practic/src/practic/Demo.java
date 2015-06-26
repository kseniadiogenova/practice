package practic;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

class PaintPanel extends JPanel{
	Insets ins;
	PaintPanel(){
		setSize(900, 600);
		setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		ins = getInsets();
		//g.drawLine(10, 20, 100, 200);
		Graph gr = new Graph();
		int r = gr.v[0].r;
		g.setColor(Color.BLACK);
		for(int i = 0; i < gr.numE; i++){
			g.drawLine(gr.e[i].from.x+r/2, gr.e[i].from.y+r/2, gr.e[i].to.x+r/2, gr.e[i].to.y+r/2);
		}
		g.setColor(Color.CYAN);
		for(int i = 0; i<gr.numV; i++){
			g.fillOval(gr.v[i].x, gr.v[i].y, gr.v[i].r, gr.v[i].r);
		}
		
	}
}

public class Demo {

	JLabel jlbl;
	PaintPanel pp;
	static int count;
	
	Demo(){
		JFrame jfrm = new JFrame("A Simple Application");		//создается контейнер содержащий окно
		jfrm.setSize(1000, 700);									//Задается его размер
		jfrm.setLayout(new BorderLayout());
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Чтобы закрывалася на Х
		jfrm.setVisible(true);			
		pp = new PaintPanel();
		jfrm.add(pp, BorderLayout.CENTER);
		jlbl = new JLabel("This is a GUI");						//Метка
		JButton btn = new JButton("Далее");
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				count++;
				jlbl.setText(Integer.toString(count));
			}
		});
		jfrm.add(btn, BorderLayout.SOUTH);
		jfrm.add(jlbl, BorderLayout.NORTH);										//Добавляем ее в контейнер
		//jfrm.pack();
								//Делаем окно видимым
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
