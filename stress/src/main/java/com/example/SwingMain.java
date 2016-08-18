package com.example;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SwingMain extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField field;
    private JButton btn;
    private JButton btnStart;
    private CoreManager coreManager = new CoreManager();

    private boolean isStart = false;
    private  int connections = 0;

    public SwingMain() {
        super("Stress test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 150));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        btn = new JButton("Set");
        btn.setActionCommand("myButton");
        btn.addActionListener(this);
        label = new JLabel("Connections quantity: ");
        field = new JTextField(5);
        add(label);
        add(field);
        add(btn);

        btnStart = new JButton("Start");
        btnStart.setActionCommand("myStartButton");
        btnStart.addActionListener(this);
        add(btnStart);

        pack();
        setLocationRelativeTo(null);


        setVisible(true);
        setResizable(false);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("myButton")) {
            if (!isStart) {
                btn.setText("+");
            }else{
                field.setText(String.valueOf(Integer.parseInt(field.getText())+1));
            }

        }else if (e.getActionCommand().equals("myStartButton")){
            int connections = connections = Integer.parseInt(field.getText());
            if (this.connections==0){
                this.connections = connections;
            }

            if (!isStart) {
                System.out.println("***********************************************************************");
                System.out.println("* STRESS FERMAT - Network Client - Version 1.0 (2016)                            *");
                System.out.println("* www.fermat.org                                                      *");
                System.out.println("***********************************************************************");
                System.out.println("");


//            System.out.println("Ingrese numero de conexiones a usar: ");
//
//            Scanner scanner = new Scanner(System.in);
//            int conections = scanner.nextInt();


                coreManager.setCoreCount(this.connections);
                coreManager.startStressTest();
                isStart = true;
            }else{
                int addConnections = connections-this.connections;
                coreManager.addConnections(addConnections);
            }
        }
    }
    public static void main(String[] args) {
        new SwingMain();
    }
}