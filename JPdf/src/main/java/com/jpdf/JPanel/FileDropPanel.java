package com.jpdf.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

/**
 * 独立封装的文件拖拽面板组件
 */
public class FileDropPanel extends JPanel {

    JComboBox<String> comboBox;
    JSlider slider;
    JProgressBar progressBar;

    // 拖拽事件监听器接口
    public interface FileDropListener {
        void onFilesDropped(List<File> files,
                            String fileTpye,
                            int quality,
                            JProgressBar progressBar);
    }

    private FileDropListener listener;

    public FileDropPanel() {
        initComponents();
        setupDragAndDrop();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel function_JPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        function_JPanel.setBackground(null);

        JLabel imageTpyeJLabel = new JLabel("文件类型:");
        function_JPanel.add(imageTpyeJLabel);

        // combo（连击），String<字符串>
        comboBox=new JComboBox<>();
        comboBox.addItem("JPG");// 下拉框列表添加内容。Item（条款，项）
        comboBox.addItem("PNG");// 下拉框列表添加内容。Item（条款，项）
        function_JPanel.add(comboBox);


        JLabel qualityJLabel = new JLabel("   质量:");
        function_JPanel.add(qualityJLabel);

        slider = new JSlider();
        slider.setMaximum(1000);
        slider.setMinimum(100);
        slider.setValue(300);
        function_JPanel.add(slider);


        add(function_JPanel, BorderLayout.NORTH);


        //进度条
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(progressBar,BorderLayout.SOUTH);

        // 添加提示标签
        setBackground(new Color(51, 51, 51)); // 恢复背景色
        JLabel label = new JLabel("将 PDF 文件拖动至此", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    private void setupDragAndDrop() {
        // 创建拖拽目标
        DropTarget dropTarget = new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    setBackground(new Color(102, 102, 102, 255)); // 拖拽进入时改变背景色
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                // 不需要特别处理
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                setBackground(new Color(51, 51, 51)); // 恢复背景色
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                setBackground(new Color(51, 51, 51)); // 恢复背景色

                try {
                    Transferable transferable = dtde.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);

                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                        // 如果设置了监听器，通知监听器
                        if (listener != null) {
                            listener.onFilesDropped(files,
                                    String.valueOf(comboBox.getSelectedItem()),
                                    slider.getValue(),
                                    progressBar);
                        }

                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.rejectDrop();
                }
            }
        });

        setDropTarget(dropTarget);
    }

    /**
     * 设置文件拖拽监听器
     * @param listener 监听器实例
     */
    public void setFileDropListener(FileDropListener listener) {
        this.listener = listener;
    }
}
