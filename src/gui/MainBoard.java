package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainBoard {

    private JPanel asdf;
    private JButton button1;


    public MainBoard() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"keool world");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainBoard");
        frame.setContentPane(new MainBoard().asdf);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
