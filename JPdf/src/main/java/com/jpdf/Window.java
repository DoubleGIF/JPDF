package com.jpdf;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.jpdf.JPanel.FileDropPanel;
import com.jpdf.core.Pdf2Img;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Window extends JFrame{

    private JFrame window;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public static int WIDTH = 450 ;
    public static int HEIGHT = 300 ;

    /**
     * 初始化界面外观和风格
     */
    static {
        //加载 FlatLightLaf UI
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    public Window(){
        setTitle("JPDF");
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null); // 将窗口居中显示
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        window = this;
        setVisible(true);
    }

    private void initComponents() {
        // 创建卡片布局的面板容器
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 向卡片容器添加不同的面板
        FileDropPanel fileDropPanel = new FileDropPanel();
        cardPanel.add(fileDropPanel,"fileDropPanel");
        fileDropPanel.setFileDropListener(new FileDropPanel.FileDropListener() {
            @Override
            public void onFilesDropped(List<File> files, String fileTpye, int quality,JProgressBar progressBar) {
                for (File path : files){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Pdf2Img.toImgFile(
                                    path,
                                    fileTpye,
                                    quality,
                                    progressBar
                            );
                        }
                    }).start();
                }
            }
        });

        JPanel text2 = new JPanel();
        cardPanel.add(text2,"wd");
        
        JPanel function_JPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton text_b = new JButton("PDF转图片");
        text_b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "fileDropPanel");
            }
        });
//        function_JPanel.add(text_b);

        JButton text_c = new JButton("窗口置顶");
        text_c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                cardLayout.show(cardPanel, "wd");
                window.setAlwaysOnTop(true);
            }
        });
        function_JPanel.add(text_c);
        
        add(function_JPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // 默认显示第一个面板
        cardLayout.show(cardPanel, "fileDropPanel");

        //底部消息展示
        JLabel msg = new JLabel("tip:这是一个测试文本。This is a test text.");
        msg.setFont(new Font(" 仿宋",Font.PLAIN,13));
        add(msg,BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        // 在事件调度线程中创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            new Window().setVisible(true);
        });
    }

}

